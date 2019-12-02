package com.chatapp.Clients.Clients;

import android.util.Log;

import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.Clients.IClients.IUpdateUserDetailsClient;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.Utils.PreferenceUtils;
import com.chatapp.Utils.StringConstants;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UpdateUserDetailsClient implements IUpdateUserDetailsClient {
    String TAG = getClass().getSimpleName();
    HashMap<String, String> data;

    @Override
    public void updateUserName(final String username, final String profileurl, final ResponseCallback<Boolean> responseCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SendBird.updateCurrentUserInfo(username, "https://www.gettyimages.ca/gi-resources/images/Homepage/Hero/UK/CMS_Creative_164657191_Kingfisher.jpg", new SendBird.UserInfoUpdateHandler() {
                    @Override
                    public void onUpdated(SendBirdException e) {
                        if (e != null) {
                            Log.i(TAG, e.getMessage());
                            responseCallback.responseCallback(false);
                            return;
                        }
                        if (responseCallback != null)
                            responseCallback.responseCallback(true);
                    }
                });
            }
        }).start();

    }

    @Override
    public void updateUserStatus(final String status, final ResponseCallback<Boolean> responseCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (data == null)
                    data = new HashMap<String, String>();
                data.put(StringConstants.STATUS, status);

                SendBird.getCurrentUser().createMetaData(data, new User.MetaDataHandler() {
                    @Override
                    public void onResult(Map<String, String> map, SendBirdException e) {
                        if (e != null) {    // Error.
                            SendBird.getCurrentUser().updateMetaData(data, new User.MetaDataHandler() {
                                @Override
                                public void onResult(Map<String, String> map, SendBirdException e) {
                                    if (e != null) {
                                        responseCallback.responseCallback(false);
                                        return;
                                    }
                                    if (responseCallback != null)
                                        responseCallback.responseCallback(true);
                                }
                            });

                            return;
                        }
                        responseCallback.responseCallback(true);
                    }
                });
            }
        }).start();

    }

    @Override
    public boolean checkUsernameisEmpty() {
        if (SendBird.getCurrentUser() != null && SendBird.getCurrentUser().getNickname() != null && SendBird.getCurrentUser().getNickname().equals(""))
            return true;
        else return false;
    }

    @Override
    public void updateProfilePic(final File file, final ResponseCallback<Boolean> flag) {
        ClientFactory.getConnectionManagerClientInstance().connect(PreferenceUtils.getInstance().getUserId(), new ResponseCallback<Boolean>() {
            @Override
            public void responseCallback(Boolean data) {
                if (data)
                    SendBird.updateCurrentUserInfoWithProfileImage(PreferenceUtils.getInstance().getUserNickName(), file, new SendBird.UserInfoUpdateHandler() {
                        @Override
                        public void onUpdated(SendBirdException e) {
                            if (e != null) {
                                Log.e(TAG, e.getMessage());
                                flag.responseCallback(false);
                                return;
                            }
                            flag.responseCallback(true);
                        }
                    });
            }
        });

    }
}
