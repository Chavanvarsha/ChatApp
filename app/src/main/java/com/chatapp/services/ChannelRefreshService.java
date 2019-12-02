package com.chatapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.database.helpher.DatabaseHelpher;
import com.chatapp.listner.ChannelListner;

import java.util.Timer;
import java.util.TimerTask;

public class ChannelRefreshService extends Service {
    TimerTask timerTask;
    Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (BaseApplication.isConnectedtoNetwork) {
                    ClientFactory.getGroupChannelClientInstance().getGroupChannelList();
                }
            }
        };
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, 5000, 5000);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
