package com.gift.app.ui.language;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gift.app.App;
import com.gift.app.R;
import com.gift.app.databinding.LanguageFragmentBinding;
import com.gift.app.ui.MainActivity;
import com.gift.app.utils.Constants;

public class LanguageFragment extends Fragment {

    private LanguageFragmentBinding binding;

    @Override
    public void onStart() {
        super.onStart();
        if (App.getPreferencesHelper().getLanguage() != null)
            Navigation.findNavController(getActivity(), R.id.host_fragment).navigate(
                    R.id.IntroFragment, null, new NavOptions.Builder().setPopUpTo(R.id.LanguageFragment,
                            true).build());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.language_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClickListeners();
    }


    private void onClickListeners() {
        binding.englishTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getPreferencesHelper().setLanguage(Constants.ENGLISH);
                refreshApp();
            }
        });

        binding.arabicTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getPreferencesHelper().setLanguage(Constants.ARABIC);
                refreshApp();
            }
        });

    }

    private void refreshApp() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        requireActivity().finish();
    }
}