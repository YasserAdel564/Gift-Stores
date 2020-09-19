package com.gift.app.ui.intro;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gift.app.ui.intro.introOne.IntroOneFragment;
import com.gift.app.ui.intro.introTwo.IntroTwoFragment;

import org.jetbrains.annotations.NotNull;

class ViewPagerAdapter extends FragmentPagerAdapter {


    private static int NUM_ITEMS = 2;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return IntroOneFragment.newInstance();
            case 1:
                return IntroTwoFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }


}
