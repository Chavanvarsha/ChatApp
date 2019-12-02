package com.chatapp.listner;

import android.util.Log;

import com.chatapp.Callbacks.IChannelListner;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.Utils.CommonUtils;
import com.chatapp.Utils.StringConstants;
import com.chatapp.database.helpher.DatabaseHelpher;
import com.chatapp.fcm.SendNotification;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.SendBird;
import com.sendbird.android.User;
import com.sendbird.android.UserMessage;

import java.util.List;

public class ChannelListner {

    String TAG = getClass().getSimpleName();
    public static ChannelListner channelListner = null;

    private ChannelListner() {

    }

    public static ChannelListner getInstance() {
        if (channelListner == null)
            channelListner = new ChannelListner();
        return channelListner;
    }

    public void channelListner() {
        if (channelListner != null)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SendBird.addChannelHandler(StringConstants.CHANNEL_HANDLER_ID, new SendBird.ChannelHandler() {
                        @Override
                        public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                            if (baseMessage instanceof UserMessage) {
                                if (BaseApplication.groupChannel == null) {
                                    if (((GroupChannel) baseChannel).isDistinct())
                                        SendNotification.getInstance()
                                                .sendNotification(BaseApplication.getInstance, ((UserMessage) baseMessage).getMessage(), baseChannel.getUrl(), getGroupName(baseChannel), "");
                                    else
                                        SendNotification.getInstance()
                                                .sendNotification(BaseApplication.getInstance, ((UserMessage) baseMessage).getMessage(), baseChannel.getUrl(), getGroupName(baseChannel), ((UserMessage) baseMessage).getSender().getNickname());
                                } else if (baseMessage != null && baseMessage instanceof UserMessage && baseChannel != null && !BaseApplication.groupChannel.getUrl().equals(baseMessage.getChannelUrl())) {
                                    SendNotification.getInstance().sendNotification(BaseApplication.getInstance, ((UserMessage) baseMessage).getMessage(), baseChannel.getUrl(), getGroupName(baseChannel), ((UserMessage) baseMessage).getSender().getNickname());
                                }
                            }
                            DatabaseHelpher.getInstance().insertBaseMessage(baseMessage);
                        }

                        @Override
                        public void onChannelChanged(BaseChannel channel) {
                            super.onChannelChanged(channel);
                        }


                        @Override
                        public void onMentionReceived(BaseChannel channel, BaseMessage message) {
                            super.onMentionReceived(channel, message);
                            Log.i(TAG, "Channnel Listner");
                        }

                        @Override
                        public void onMessageUpdated(BaseChannel channel, BaseMessage message) {
                            super.onMessageUpdated(channel, message);
                            DatabaseHelpher.getInstance().insertBaseMessage(message);
                        }

                        @Override
                        public void onTypingStatusUpdated(GroupChannel channel) {
                            if (BaseApplication.groupChannel != null) {
                                if (BaseApplication.groupChannel.getUrl().equals(channel.getUrl())) {
                                    List<Member> members = channel.getTypingMembers();
                                    if (BaseApplication.chatScreenActivity != null && CommonUtils.getInstance().checkContextExistOrNot(BaseApplication.chatScreenActivity)) {
                                        BaseApplication.chatScreenActivity.refreshTypingStatus(members);
                                    }
                                }
                            }
                        }
                    });

                }
            }).start();


    }

    public String getGroupName(BaseChannel baseChannel) {
        if (baseChannel != null)
            if (((GroupChannel) baseChannel).isDistinct()) {
                return ((GroupChannel) baseChannel).getMembers().get(CommonUtils.getInstance().getUSerId((GroupChannel) baseChannel)).getNickname();
            } else {
                return baseChannel.getName();
            }
        else return "";
    }


}
