package com.chatapp.UI.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.R;
import com.chatapp.Utils.SharedPref;
import com.chatapp.Utils.StringConstants;
import com.sendbird.android.GroupChannel;

import java.util.List;

public class InitializingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initializing);
        ClientFactory.getUserListClientInstance().getGroupListApiCall(this, new ResponseCallback<List<GroupChannel>>() {
            @Override
            public void responseCallback(List<GroupChannel> data) {
                if (data != null) {
                    setUsernameIfEmpty();
                }
            }
        });
    }

    public void setUsernameIfEmpty() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ClientFactory.getUpdateUserDetailsClientInstance().checkUsernameisEmpty())
                    ClientFactory.getUpdateUserDetailsClientInstance().updateUserName(SharedPref.read(StringConstants.PREFERENCE_KEY_EMAIL_ID), null, new ResponseCallback<Boolean>() {
                        @Override
                        public void responseCallback(Boolean data) {
                            startActivity(new Intent(InitializingActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                else {
                    startActivity(new Intent(InitializingActivity.this, MainActivity.class));
                    finish();
                }
            }
        }).start();
    }
}
