package com.chatapp.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.chatapp.R;
import com.chatapp.UI.Activity.ChatScreenActivity;
import com.chatapp.database.helpher.DatabaseHelpher;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;
import com.sendbird.android.shadow.com.google.gson.JsonElement;
import com.sendbird.android.shadow.com.google.gson.JsonObject;
import com.sendbird.android.shadow.com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String SenderName = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // String message = remoteMessage.getData().get("message");
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        JsonElement payload = new JsonParser().parse(remoteMessage.getData().get("sendbird"));
        JsonObject jsonObject = payload.getAsJsonObject();
        String message = jsonObject.get("message").getAsString();
        JsonObject jsonObjectChannel = jsonObject.get("channel").getAsJsonObject();
        JsonObject jsonObjectSender = jsonObject.get("sender").getAsJsonObject();
        String name = jsonObjectChannel.get("name").getAsString();
        if (name.equals("Group Channel")) {
            name = jsonObjectSender.get("name").getAsString();
        } else {
            SenderName = jsonObjectSender.get("name").getAsString();
        }
        SendNotification.getInstance().sendNotification(getApplicationContext(), message, null, name + "", SenderName);
        SenderName = "";
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String token = s;

        SendBird.registerPushTokenForCurrentUser(token, new SendBird.RegisterPushTokenWithStatusHandler() {
            @Override
            public void onRegistered(SendBird.PushTokenRegistrationStatus ptrs, SendBirdException e) {
                if (e != null) {
                    return;
                }

                if (ptrs == SendBird.PushTokenRegistrationStatus.PENDING) {
                    // Try registering the token after a connection has been successfully established.
                }
            }
        });
    }
}