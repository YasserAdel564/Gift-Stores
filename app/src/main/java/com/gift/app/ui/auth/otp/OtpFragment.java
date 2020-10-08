package com.gift.app.ui.auth.otp;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.KeyboardUtils;
import com.gift.app.App;
import com.gift.app.R;
import com.gift.app.databinding.OtpFragmentBinding;
import com.gift.app.ui.Home.SharedViewModel;
import com.gift.app.utils.Extensions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class OtpFragment extends Fragment {

    private OtpViewModel mViewModel;
    private OtpFragmentBinding binding;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private SharedViewModel mSharedViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.otp_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OtpViewModel.class);
        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        clickListeners();
        sendVerificationCode(App.getPreferencesHelper().getUserMobile());

    }



    private void validation() {
        String code = binding.CodeEt.getText().toString();
        KeyboardUtils.hideSoftInput(requireActivity());
        if (code.isEmpty()) {
            binding.CodeEt.setError(requireActivity().getString(R.string.otp_empty));
            return;
        }
        if (code.length() < 6) {
            binding.CodeEt.setError(requireActivity().getString(R.string.otp_short_length));
            return;
        }
        verifyCode(code);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NotNull PhoneAuthCredential credential) {
            String code = credential.getSmsCode();
            if (code != null) {
                binding.CodeEt.setText(code);
            } else
                Extensions.generalErrorSnakeBar(binding.otpLayout);

        }

        @Override
        public void onVerificationFailed(@NotNull FirebaseException e) {
            Extensions.generalErrorSnakeBar(binding.otpLayout);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            mVerificationId = verificationId;
            Extensions.generalSuccess(binding.otpLayout);
        }
    };


    private void sendVerificationCode(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+2" + phone,
                60,
                TimeUnit.SECONDS,
                requireActivity(),
                mCallbacks);
    }

    private void verifyCode(String code) {
        signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(mVerificationId, code));
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Extensions.generalSuccess(binding.otpLayout);
                            App.getPreferencesHelper().setIsLogin(true);
                            Navigation.findNavController(requireActivity(), R.id.host_fragment)
                                    .navigate(R.id.action_from_otpFragment_to_homeFragment);
                        } else
                            Extensions.generalErrorSnakeBar(binding.otpLayout);
                    }
                });
    }


    private void clickListeners() {
        binding.backImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(), R.id.host_fragment).navigateUp();

            }
        });

        binding.sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });

    }

}