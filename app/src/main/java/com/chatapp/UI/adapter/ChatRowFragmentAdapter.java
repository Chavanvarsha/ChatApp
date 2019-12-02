package com.chatapp.UI.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chatapp.UI.Fragments.ChatsRowFragment;
import com.chatapp.UI.Fragments.MoreFragment;
import com.chatapp.Utils.IntegerConstant;

public class ChatRowFragmentAdapter extends FragmentPagerAdapter {

    public ChatsRowFragment userChatsFragment;
    public ChatsRowFragment groupChatsFragment;
    public MoreFragment moreFragment;

    public ChatRowFragmentAdapter(FragmentManager fm) {
        super(fm);
        if (userChatsFragment == null)
            userChatsFragment = new ChatsRowFragment();
        userChatsFragment.SCREEN_TYPE = IntegerConstant.CHAT_FRAGMENT;
        if (groupChatsFragment == null)
            groupChatsFragment = new ChatsRowFragment();
        groupChatsFragment.SCREEN_TYPE = IntegerConstant.GROUP_FRAGMENT;
        moreFragment=new MoreFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return userChatsFragment;
            case 1:
                return groupChatsFragment;
            case 2:
                return moreFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
