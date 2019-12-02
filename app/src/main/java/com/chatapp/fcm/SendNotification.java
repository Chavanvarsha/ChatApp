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

import com.chatapp.R;
import com.chatapp.UI.Activity.ChatScreenActivity;

public class SendNotification {

    private static SendNotification sendNotification;

    public static SendNotification getInstance() {
        if (sendNotification == null)
            sendNotification = new SendNotification();
        return sendNotification;
    }

    public void sendNotification(Context context, String messageBody, String channelUrl, String name, String othername) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        final String CHANNEL_ID = "CHANNEL_ID";
        if (Build.VERSION.SDK_INT >= 26) {  // Build.VERSION_CODES.O
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        /*Intent intent = new Intent(context, ChatScreenActivity.class);
        //intent.putExtra("groupChannelUrl", channelUrl);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 *//* Request code *//*, intent, PendingIntent.FLAG_UPDATE_CURRENT);
*/
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.chat_colored_icon)
                .setColor(Color.parseColor("#7469C4"))  // small icon background color
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_right))
                .setContentTitle(name)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL);

        if (true) {
            if (othername.equals(""))
                notificationBuilder.setContentText(messageBody);
            else
                notificationBuilder.setContentText(othername + " : " + messageBody);
        } else {
            notificationBuilder.setContentText("Somebody sent you a message.");
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
