package com.chatapp.UI.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.R;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.UI.Activity.ChatScreenActivity;
import com.chatapp.UI.Activity.CreateUserListActivity;
import com.chatapp.Utils.ImageUtils;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.PreferenceUtils;
import com.chatapp.model.UserMetadata;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.User;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    String TAG = getClass().getSimpleName();
    List<User> userlist = new ArrayList<>();
    ViewGroup parent;
    List<String> userid = new ArrayList<>();
    int type;
    UserMetadata userMetadata = new UserMetadata();
    List<Member> memberList = new ArrayList<>();

    public UserListAdapter(int type) {
        this.type = type;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_user_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        if (type == IntegerConstant.GROUP_INFO)
            holder.bindGroupInfo(memberList.get(position));
        else
            holder.bind(userlist.get(position));
    }

    @Override
    public int getItemCount() {
        if (userlist != null && type != IntegerConstant.GROUP_INFO)
            return userlist.size();
        else if (memberList != null)
            return memberList.size();
        return 0;
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView userProfile;
        TextView username;
        RadioButton selecteduser;
        RadioGroup radioGroup;

        public UserViewHolder(View itemView) {
            super(itemView);
            userProfile = itemView.findViewById(R.id.iv_userimage);
            username = itemView.findViewById(R.id.tv_username);
            selecteduser = itemView.findViewById(R.id.rb_selected);
            radioGroup = itemView.findViewById(R.id.radiogroup);
        }

        public void bind(final User user) {
            if (user != null) {
                ImageUtils.displayRoundImageFromUrl(parent.getContext(), user.getProfileUrl(), userProfile);
                username.setText(user.getNickname());
                if (type == IntegerConstant.CREATE_GROUP || type == IntegerConstant.ADD_USERS) {
                    radioGroup.setVisibility(View.VISIBLE);
                    radioGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (selecteduser.isChecked()) {
                                radioGroup.clearCheck();
                                userid.remove(user.getUserId());
                            } else {
                                selecteduser.setChecked(true);
                                userid.add(user.getUserId());
                            }
                        }
                    });
                } else if (type == IntegerConstant.START_CHAT) {
                    radioGroup.setVisibility(View.GONE);
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            userMetadata.setUserId(user.getUserId());
                            ClientFactory.getGroupChannelClientInstance().createDirectMessageChannelApiCall(parent.getContext(), new ResponseCallback<GroupChannel>() {
                                @Override
                                public void responseCallback(GroupChannel groupChannel) {
                                    if (groupChannel != null) {
                                        BaseApplication.groupChannel = groupChannel;
                                        parent.getContext().startActivity(new Intent(parent.getContext(), ChatScreenActivity.class));
                                        ((CreateUserListActivity) parent.getContext()).finish();
                                    }
                                }
                            }, userMetadata);
                        }
                    });
                }
            }
        }

        public void bindGroupInfo(Member member) {
            ImageUtils.displayRoundImageFromUrl(parent.getContext(), member.getProfileUrl(), userProfile);
            if (member.getUserId().equals(PreferenceUtils.getInstance().getUserId()))
                username.setText("You");
            else username.setText(member.getNickname());
            radioGroup.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rb_selected:
                    break;
            }
        }
    }

    public void setUserlist(List<User> userlist) {
        if (userlist != null) {
            this.userlist = userlist;
            notifyDataSetChanged();
        }
    }

    public void setMenberList(List<Member> menberList) {
        if (menberList != null) {
            this.memberList = menberList;
        }
    }


    public List<String> getUserid() {
        return userid;
    }
}
