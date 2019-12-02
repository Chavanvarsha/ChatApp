package com.chatapp.UI.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.UI.Activity.ChatScreenActivity;
import com.chatapp.R;
import com.chatapp.UI.dialog.ProfileDialog;
import com.chatapp.Utils.CommonUtils;
import com.chatapp.Utils.DateUtils;
import com.chatapp.Utils.ImageUtils;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.PreferenceUtils;
import com.chatapp.Utils.StringConstants;
import com.chatapp.model.CustomGroupChannel;
import com.chatapp.model.UserMetadata;
import com.google.gson.Gson;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.SendBird;
import com.sendbird.android.User;

import java.util.ArrayList;
import java.util.List;

public class ChatRowAdapter extends RecyclerView.Adapter<ChatRowAdapter.ChatRowsViewHolder> {

    String TAG = getClass().getSimpleName();
    List<GroupChannel> groupList;
    ViewGroup parent;
    UserMetadata userMetadata = new UserMetadata();
    int screentype;
    Intent intent;


    public ChatRowAdapter(int type) {
        this.screentype = type;
    }

    @NonNull
    @Override
    public ChatRowsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainscreen_row, parent, false);
        return new ChatRowsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatRowsViewHolder holder, final int position) {
        if (screentype == IntegerConstant.CHAT_FRAGMENT && groupList.size() > 0 && position > -1) {
            for (int i = 0; i < groupList.get(position).getMembers().size(); i++) {
                if (!PreferenceUtils.getInstance().getUserId().equals(groupList.get(position).getMembers().get(i).getUserId())) {
                    holder.bindDirectChat(groupList.get(position).getMembers().get(i), groupList.get(position));
                    break;
                }
            }

        } else
            holder.bindGroupChat(groupList.get(position));
    }

    @Override
    public int getItemCount() {
        if (groupList != null)
            return groupList.size();
        else return 0;
    }

    class ChatRowsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView username, status, unreadmessage;
        ImageView userprofileimage, statusicon;
        View view;

        public ChatRowsViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            username = itemView.findViewById(R.id.tv_username);
            status = itemView.findViewById(R.id.tv_userstatus);
            userprofileimage = itemView.findViewById(R.id.iv_userprofileimage);
            statusicon = itemView.findViewById(R.id.iv_status_color);
            unreadmessage = itemView.findViewById(R.id.tv_unread_message_count);
        }

        void bindDirectChat(final Member member, final GroupChannel groupChannel) {
            unreadmessage.setVisibility(View.VISIBLE);
            username.setText(member.getNickname());
            if (member.getConnectionStatus() == User.ConnectionStatus.ONLINE) {
                statusicon.setBackground(CommonUtils.changeDrawableColor(parent.getContext(), R.drawable.circle, Color.GREEN));
                status.setText(parent.getResources().getString(R.string.online));
            } else {
                statusicon.setBackground(CommonUtils.changeDrawableColor(parent.getContext(), R.drawable.circle, Color.RED));
                if (DateUtils.isToday(member.getLastSeenAt()))
                    status.setText(parent.getContext().getString(R.string.lastseen_today) + " " + DateUtils.formatDateTime(member.getLastSeenAt()));
                else
                    status.setText(parent.getContext().getString(R.string.lastseen) + " " + DateUtils.formatDateTime(member.getLastSeenAt()));
            }
            ImageUtils.displayRoundImageFromUrl(parent.getContext(), member.getProfileUrl(), userprofileimage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseApplication.groupChannel = groupChannel;

                    parent.getContext().startActivity(new Intent(parent.getContext(), ChatScreenActivity.class));
                }
            });
            if (groupChannel.getUnreadMessageCount() > 0) {
                unreadmessage.setText(groupChannel.getUnreadMessageCount() + "");
                unreadmessage.setVisibility(View.VISIBLE);
            } else {
                unreadmessage.setVisibility(View.GONE);
            }
            userprofileimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unreadmessage.setVisibility(View.GONE);
                    new ProfileDialog(parent.getContext(), member.getProfileUrl(), member.getNickname(), member.getMetaData(StringConstants.STATUS) + "").show();
                }
            });
        }

        void bindGroupChat(final GroupChannel groupChannel) {
            unreadmessage.setVisibility(View.VISIBLE);
            username.setText(groupChannel.getName());
            ImageUtils.displayRoundImageFromUrl(parent.getContext(), groupChannel.getCoverUrl(), userprofileimage);
            status.setText(groupChannel.getMemberCount() + " members");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseApplication.groupChannel = groupChannel;
                    parent.getContext().startActivity(new Intent(parent.getContext(), ChatScreenActivity.class));
                }
            });
            if (groupChannel.getUnreadMessageCount() > 0) {
                unreadmessage.setText(groupChannel.getUnreadMessageCount() + "");
                unreadmessage.setVisibility(View.VISIBLE);
            } else {
                unreadmessage.setVisibility(View.GONE);
            }
            userprofileimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unreadmessage.setVisibility(View.GONE);
                    new ProfileDialog(parent.getContext(), groupChannel.getCoverUrl(), groupChannel.getName(), groupChannel.getData() + "").show();
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_userprofileimage:
                    break;
            }
        }
    }

    public void setUserList(List<GroupChannel> groupList) {
        this.groupList = groupList;
        notifyDataSetChanged();
    }


    public UserMetadata setuserMetadata(String userId) {
        userMetadata.setUserId(userId);
        return userMetadata;
    }

}
