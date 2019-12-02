package com.chatapp.UI.Fragments;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.R;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.UI.adapter.ChatRowAdapter;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.database.Entity.GroupList;
import com.chatapp.database.ViewModel.GroupListViewModel;
import com.chatapp.database.ViewModel.MessageViewModel;
import com.chatapp.database.helpher.DatabaseHelpher;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.User;

import java.util.ArrayList;
import java.util.List;


public class ChatsRowFragment extends Fragment {

    public int SCREEN_TYPE;
    RecyclerView chatrowsRecycle;
    LinearLayoutManager linearLayoutManager;
    ChatRowAdapter chatRowAdapter;
    ProgressBar progressBar;
    GroupListViewModel groupListViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupListViewModel = ViewModelProviders.of(this).get(GroupListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats_row, container, false);
        chatrowsRecycle = view.findViewById(R.id.recyclerview);
        progressBar = view.findViewById(R.id.progressbar);
        initializeview();
        return view;
    }

    public void getList() {
        displayProgress(false);
        if (SCREEN_TYPE == IntegerConstant.CHAT_FRAGMENT) {
            chatRowAdapter = new ChatRowAdapter(IntegerConstant.CHAT_FRAGMENT);
            if (groupListViewModel != null)
                groupListViewModel.getLiveGroupList(true).observe(this, new Observer<List<GroupList>>() {
                    @Override
                    public void onChanged(@Nullable List<GroupList> groupLists) {
                        if (groupLists != null) {
                            List<GroupChannel> baseChannelList = new ArrayList<>();
                            for (int i = 0; i < groupLists.size(); i++) {
                                baseChannelList.add((GroupChannel) GroupChannel.buildFromSerializedData(groupLists.get(i).getGroupchannel()));
                            }
                            chatRowAdapter.setUserList(baseChannelList);
                        }
                    }
                });

        } else {
            if (groupListViewModel != null)
                groupListViewModel.getLiveGroupList(false).observe(this, new Observer<List<GroupList>>() {
                    @Override
                    public void onChanged(@Nullable List<GroupList> groupLists) {
                        if (groupLists != null) {
                            List<GroupChannel> baseChannelList = new ArrayList<>();
                            for (int i = 0; i < groupLists.size(); i++) {
                                baseChannelList.add((GroupChannel) GroupChannel.buildFromSerializedData(groupLists.get(i).getGroupchannel()));
                            }
                            chatRowAdapter.setUserList(baseChannelList);
                        }
                    }
                });

            chatRowAdapter = new ChatRowAdapter(IntegerConstant.GROUP_FRAGMENT);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void displayProgress(boolean flag) {
        if (flag) {
            chatrowsRecycle.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            chatrowsRecycle.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void initializeview() {
        if (linearLayoutManager == null)
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        chatrowsRecycle.setLayoutManager(linearLayoutManager);
        getList();
        chatrowsRecycle.setAdapter(chatRowAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }

}
