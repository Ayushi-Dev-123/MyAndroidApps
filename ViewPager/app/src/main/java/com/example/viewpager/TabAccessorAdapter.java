package com.example.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessorAdapter extends FragmentPagerAdapter {

    public TabAccessorAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position == 0)
            fragment = new ChatFragment();
        else if(position == 1)
            fragment = new StatusFragment();
        else if(position == 0)
            fragment = new CallsFragment();
        return fragment;
    }

    public CharSequence getPageTitle(int position) {
        String title = "";
        if(position == 0)
            title = "CHAT";
        else if(position == 1)
            title = "STATUS";
        else if(position == 2)
            title = "CALLS";
        return title;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
