package com.chatapp.Scope;

import android.app.Application;

import com.chatapp.UI.Activity.ChatScreenActivity;
import com.chatapp.Utils.SharedPref;
import com.chatapp.Utils.StringConstants;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.SendBird;

import net.hockeyapp.android.CrashManager;

import java.util.List;

public class BaseApplication extends Application {

    public static boolean isConnectedtoNetwork = false;
    public static BaseApplication getInstance;
    public static GroupChannel groupChannel;
    public static List<Member> typingMember;
    public static ChatScreenActivity chatScreenActivity;


    @Override
    public void onCreate() {
        super.onCreate();
        CrashManager.register(this);
        getInstance = this;
        PRDownloader.initialize(getApplicationContext());
        initializeCustomizePRDownloader();
        SendBird.init(StringConstants.APP_ID, getApplicationContext());
        SharedPref.init(this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        SendBird.setAutoBackgroundDetection(true);
    }

    public void initializeCustomizePRDownloader() {
        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);
    }


}
