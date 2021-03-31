package com.ayushi.apnachating.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ayushi.apnachating.fragments.ChatFragment;
import com.ayushi.apnachating.fragments.ContactFragment;
import com.ayushi.apnachating.fragments.GroupFragment;
import com.ayushi.apnachating.fragments.RequestFragment;
import com.ayushi.apnachating.fragments.StoryFragment;

public class TabAccessorAdapter extends FragmentStatePagerAdapter {

    public TabAccessorAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0: fragment = new ChatFragment();break;
            case 1: fragment = new GroupFragment();break;
            case 2: fragment = new StoryFragment();break;
            case 3: fragment = new RequestFragment();break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String tabTitle = "";
        if(position == 0)
            tabTitle = "CHAT";
        else if(position == 1)
            tabTitle = "GROUP";
        else if(position == 2)
            tabTitle = "STORY";
        else if(position == 3)
            tabTitle = "REQUEST";
        return tabTitle;
    }
}
