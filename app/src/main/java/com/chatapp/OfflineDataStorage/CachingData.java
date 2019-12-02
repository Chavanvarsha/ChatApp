package com.chatapp.OfflineDataStorage;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.chatapp.Utils.FileUtils;
import com.chatapp.Utils.StringConstants;
import com.chatapp.Utils.TextUtils;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.UserMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CachingData {

    String TAG = getClass().getSimpleName();
    List<GroupChannel> mChannelList = new ArrayList<>();
    private static CachingData cachingData = null;

    private CachingData() {
    }

    public static CachingData getInstance() {
        if (cachingData == null)
            cachingData = new CachingData();
        return cachingData;
    }

    public void saveGroupChannelList(List<GroupChannel> mChannelList, Context context) {
        try {
            StringBuilder sb = new StringBuilder();
            if (mChannelList != null && mChannelList.size() > 0) {
                // Convert current channel into a String.
                GroupChannel channel = null;
                for (int i = 0; i < Math.min(mChannelList.size(), 100); i++) {
                    channel = mChannelList.get(i);
                    sb.append("\n");
                    sb.append(Base64.encodeToString(channel.serialize(), Base64.DEFAULT | Base64.NO_WRAP));
                }
                // Remove first newline.
                sb.delete(0, 1);

                String data = sb.toString();
                String md5 = TextUtils.generateMD5(data);

                // Save the data into file.
                File appDir = new File(context.getCacheDir(), SendBird.getApplicationId());
                appDir.mkdirs();

                File hashFile = new File(appDir, TextUtils.generateMD5(SendBird.getCurrentUser().getUserId() + StringConstants.channel_list) + ".hash");
                File dataFile = new File(appDir, TextUtils.generateMD5(SendBird.getCurrentUser().getUserId() + StringConstants.channel_list) + ".data");

                try {
                    String content = FileUtils.loadFromFile(hashFile);
                    // If data has not been changed, do not save.
                    if (md5.equals(content)) {
                        return;
                    }
                } catch (IOException e) {
                    // File not found. Save the data.
                }
                FileUtils.saveToFile(dataFile, data);
                FileUtils.saveToFile(hashFile, md5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGroupChannelList(Context context) {
        try {
            File appDir = new File(context.getCacheDir(), SendBird.getApplicationId());
            appDir.mkdirs();

            File dataFile = new File(appDir, TextUtils.generateMD5(SendBird.getCurrentUser().getUserId() + StringConstants.channel_list) + ".data");

            String content = FileUtils.loadFromFile(dataFile);
            String[] dataArray = content.split("\n");

            // Add the loaded channels to the currently displayed channel list.
            for (int i = 0; i < dataArray.length; i++) {
                mChannelList.add((GroupChannel) BaseChannel.buildFromSerializedData(Base64.decode(dataArray[i], Base64.DEFAULT | Base64.NO_WRAP)));
            }
            //Testing purpose
            /*for (int i = 0; i < mChannelList.size(); i++) {
                Log.i(TAG, ((UserMessage) mChannelList.get(i).getLastMessage()).getMessage());
            }*/


        } catch (Exception e) {
            // Nothing to load.
        }
    }

}
