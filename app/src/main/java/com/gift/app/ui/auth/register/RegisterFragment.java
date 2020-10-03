package com.gift.app.ui.auth.register;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import com.gift.app.databinding.RegisterFragmentBinding;
import com.gift.app.utils.Extensions;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    RegisterFragmentBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        clickListeners();

        onRegisterResponse();
    }


    private void clickListeners() {
        binding.goToLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(), R.id.host_fragment).navigate(R.id.action_from_registerFragment_to_loginFragment);

            }
        });

        binding.backImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(), R.id.host_fragment).navigateUp();

            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation())
                    mViewModel.register();
            }
        });


    }


    private void goToActivation() {
        Navigation.findNavController(requireActivity(), R.id.host_fragment).navigate(R.id.action_from_registerFragment_to_otpFragment);
    }


    private Boolean validation() {
        if (binding.UsrePhoneEt.getText().toString().trim().isEmpty()) {
            binding.UsrePhoneEt.setError(requireActivity().getString(R.string.phone_empty));
            return false;
        }
        if (binding.userAddressEt.getText().toString().trim().isEmpty()) {
            binding.userAddressEt.setError(requireActivity().getString(R.string.address_empty));
            return false;
        }
        if (binding.userNameEt.getText().toString().trim().isEmpty()) {
            binding.userNameEt.setError(requireActivity().getString(R.string.name_empty));
            return false;
        } else if (binding.UsrePhoneEt.getText().toString().trim().length() != 11) {
            binding.UsrePhoneEt.setError(requireActivity().getString(R.string.phone_short_length));
            return false;
        } else {
            mViewModel.name = binding.userNameEt.getText().toString();
            mViewModel.mobile = binding.UsrePhoneEt.getText().toString();
            App.getPreferencesHelper().setUserAddress(binding.userAddressEt.getText().toString().trim());
            return true;
        }


    }


    private void onRegisterResponse() {
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
                            Extensions.Success(binding.registerRoot, mViewModel.response.getMsg());
                        goToActivation();
                        App.getPreferencesHelper().setUserMobile(mViewModel.response.getData().getMobile());
                        App.getPreferencesHelper().setUserName(mViewModel.response.getData().getName());
                        break;

                    case "onEmpty":
                    case "onError":
                        binding.loading.setVisibility(View.GONE);
                        Extensions.Success(binding.registerRoot, mViewModel.response.getMsg());
                        break;

                    case "onNoConnection":
                        binding.loading.setVisibility(View.GONE);
                        Extensions.noInternetSnakeBar(binding.registerRoot);
                        break;

                    default:

                }
            }
        });


    }
}