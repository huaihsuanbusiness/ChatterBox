package com.huaihsuanhuang.chatterbox.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.huaihsuanhuang.chatterbox.Mainpage.FriendFragment;
import com.huaihsuanhuang.chatterbox.Mainpage.MessagelistFragment;
import com.huaihsuanhuang.chatterbox.Mainpage.RequestFragment;

public class SectionPagerAdapter extends FragmentPagerAdapter {


    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return new FriendFragment();
            case 1:
                return new MessagelistFragment();
            case 2:
                return new RequestFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return "Friends";
            case 1:
                return "Messages";
            case 2:
                return "Requests";
            default:
                return null;
        }

    }
}
