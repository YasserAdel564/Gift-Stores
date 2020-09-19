package com.gift.app.ui.intro.introOne;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gift.app.R;


public class IntroOneFragment extends Fragment {


    public static IntroOneFragment newInstance() {
        return new IntroOneFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro_one, container, false);
    }
}