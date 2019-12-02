package com.chatapp.Clients.IClients;

import android.content.Context;

import com.chatapp.Callbacks.IResponseCallback;
import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.model.GroupMetadata;
import com.chatapp.model.UserMetadata;
import com.sendbird.android.GroupChannel;

import java.io.File;
import java.util.List;

public interface IGroupChannelClient {

    public void createDirectMessageChannelApiCall(Context context, ResponseCallback<GroupChannel> groupChannelResponseCallback, UserMetadata userMetadata);

    public void createGroupMessageChannelApiCall(Context context, IResponseCallback responseCallback, GroupMetadata groupMetadata);

    public void getGroupChannelList();

    public void getPreviousMessage(GroupChannel mChannel);

    public void addUserToGroup(GroupChannel groupChannel, List<String> userIds);

    public void updateGroupData(GroupChannel groupChannel, String groupname, String description, File imageurl,ResponseCallback<Boolean> iResponseCallback);
}
