package com.gift.app.ui.auth.login;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gift.app.App;
import com.gift.app.R;
import com.gift.app.databinding.LoginFragmentBinding;
import com.gift.app.utils.Extensions;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private LoginFragmentBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        App.getPreferencesHelper().setUserName(null);
        App.getPreferencesHelper().setIsLogin(false);

        clickListeners();
        onLoginResponse();
    }


    private void clickListeners() {
        binding.goToRegisterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(), R.id.host_fragment).navigate(R.id.action_from_loginFragment_to_registerFragment);

            }
        });

        binding.backImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(), R.id.host_fragment).navigateUp();

            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation())
                    login();

            }
        });


    }

    private Boolean validation() {
        if (binding.UsrePhoneEt.getText().toString().trim().isEmpty()) {
            binding.UsrePhoneEt.setError(requireActivity().getString(R.string.phone_empty));
            return false;
        } else if (binding.UsrePhoneEt.getText().toString().trim().length() != 11) {
            binding.UsrePhoneEt.setError(requireActivity().getString(R.string.phone_short_length));
            return false;
        } else {
            mViewModel.phoneNumber = binding.UsrePhoneEt.getText().toString();
            return true;
        }
    }

    private void login() {
        mViewModel.login();
    }

    private void onLoginResponse() {

        mViewModel.liveState.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String state) {
                switch (state) {
                    case "onLoading":
                        binding.loading.setVisibility(View.VISIBLE);
                        break;

                    case "onSuccess":
                        binding.loading.setVisibility(View.GONE);
                        if (!mViewModel.response.getMsg().isEmpty())
                            Extensions.generalMessage(binding.loginRoot, mViewModel.response.getMsg());
                        Navigation.findNavController(requireActivity(), R.id.host_fragment)
                                .navigate(R.id.action_from_loginFragment_to_otpFragment);
                        App.getPreferencesHelper().setUserMobile(mViewModel.response.getData().getMobile());
                        App.getPreferencesHelper().setUserName(mViewModel.response.getData().getName());
                        break;

                    case "onEmpty":
                    case "onError":
                        binding.loading.setVisibility(View.GONE);
                        Extensions.generalMessage(binding.loginRoot, mViewModel.response.getMsg());
                        break;

                    case "onNoConnection":
                        binding.loading.setVisibility(View.GONE);
                        Extensions.noInternetSnakeBar(binding.loginRoot);
                        break;

                    default:
                }
            }
        });


    }

    @Override
    public void onStop() {
        mViewModel.liveState.postValue("");
        super.onStop();
    }
}