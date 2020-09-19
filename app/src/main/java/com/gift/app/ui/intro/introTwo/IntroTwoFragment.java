package com.gift.app.ui.intro.introTwo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gift.app.App;
import com.gift.app.R;
import com.gift.app.databinding.FragmentIntroHostBinding;
import com.gift.app.databinding.FragmentIntroTwoBinding;
import com.gift.app.ui.intro.introOne.IntroOneFragment;


public class IntroTwoFragment extends Fragment {

    public static IntroTwoFragment newInstance() {
        return new IntroTwoFragment();
    }

    FragmentIntroTwoBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro_two, container, false);
        return binding.getRoot();


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onClickListeners();
    }

    private void onClickListeners() {
        binding.nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startApp();
            }
        });
    }


    public void startApp() {
        Navigation.findNavController(getActivity(), R.id.host_fragment).navigate(
                R.id.DepartmentsFragment, null, new NavOptions.Builder().setPopUpTo(R.id.IntroFragment,
                        false).build());
        App.getPreferencesHelper().setShowIntro(true);

    }
}