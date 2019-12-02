package com.chatapp.Clients.IClients;

import com.chatapp.Callbacks.ResponseCallback;

import java.io.File;

public interface IUpdateUserDetailsClient {

    public void updateUserName(String username, final String profileurl, ResponseCallback<Boolean> responseCallback);

    public void updateUserStatus(String status, ResponseCallback<Boolean> responseCallback);

    public boolean checkUsernameisEmpty();

    public void updateProfilePic(File file,ResponseCallback<Boolean> flag);
}
