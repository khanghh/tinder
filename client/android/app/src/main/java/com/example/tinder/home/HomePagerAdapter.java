package com.example.tinder.home;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;


public class HomePagerAdapter extends FragmentPagerAdapter {

    private Fragment [] listFragment;

    public HomePagerAdapter(FragmentManager fm, Fragment [] listFragment) {
        super(fm);
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int i) {
        return this.listFragment[i];
    }

    @Override
    public int getCount() {
        return listFragment != null ? listFragment.length : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

}
