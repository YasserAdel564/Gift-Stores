package com.gift.app.ui.intro;

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


public class IntroFragment extends Fragment {

    FragmentIntroHostBinding binding;
    private ViewPagerAdapter adapter;

    @Override
    public void onStart() {
        super.onStart();
        if (App.getPreferencesHelper().getShowIntro())
            Navigation.findNavController(getActivity(), R.id.host_fragment).navigate(
                    R.id.SplashFragment, null, new NavOptions.Builder().setPopUpTo(R.id.IntroFragment,
                            true).build());
        else
            App.getPreferencesHelper().setShowIntro(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro_host, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager();
        onClickListeners();
    }


    private void initViewPager() {
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        binding.introVp.setAdapter(adapter);
        binding.pageIndicator.setViewPager(binding.introVp);
    }

    private void onClickListeners() {
        binding.skipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.host_fragment).navigate(
                        R.id.SplashFragment, null, new NavOptions.Builder().setPopUpTo(R.id.IntroFragment,
                                true).build());

                App.getPreferencesHelper().setShowIntro(true);
            }
        });
    }


}