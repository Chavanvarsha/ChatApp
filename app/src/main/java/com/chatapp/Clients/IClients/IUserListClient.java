package com.chatapp.Clients.IClients;

import android.content.Context;

import com.chatapp.Callbacks.IResponseCallback;
import com.chatapp.Callbacks.ResponseCallback;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.User;

import java.util.List;

public interface IUserListClient {

    public void getUserListApiCall(Context context, ResponseCallback<List<User>> userList);

    public void getGroupListApiCall(Context context, ResponseCallback<List<GroupChannel>> groupList);
}
