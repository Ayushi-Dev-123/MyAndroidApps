package com.mychat.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.mychat.fragments.ChatFragment;
import com.mychat.fragments.ContactFragment;
import com.mychat.fragments.GroupFragment;
import com.mychat.fragments.RequestFragment;

public class TabAccessorAdapter extends FragmentStatePagerAdapter {

    public TabAccessorAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0: fragment = new ChatFragment();
            case 1: fragment = new GroupFragment();
            case 2: fragment = new ContactFragment();
            case 3: fragment = new RequestFragment();
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
            tabTitle = "CONTACT";
        else if(position == 3)
            tabTitle = "REQUEST";
        return tabTitle;
    }
}
