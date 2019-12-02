package com.chatapp.Clients.Clients;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.sql.Timestamp;

import com.chatapp.Callbacks.IResponseCallback;
import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.Clients.IClients.IGroupChannelClient;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.OfflineDataStorage.CachingData;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.Utils.DateUtils;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.PreferenceUtils;
import com.chatapp.Utils.StringConstants;
import com.chatapp.database.helpher.DatabaseHelpher;
import com.chatapp.model.GroupMetadata;
import com.chatapp.model.UserMetadata;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.GroupChannelParams;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupChannelClient implements IGroupChannelClient {
    String TAG = getClass().getSimpleName();
    Bundle bundle = new Bundle();
    List<String> userIdList;
    GroupChannelParams params;

    @Override
    public void createDirectMessageChannelApiCall(final Context context, final ResponseCallback<GroupChannel> responseCallback, final UserMetadata userMetadata) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (bundle != null) {
                    bundle.putInt(StringConstants.TYPE, IntegerConstant.DIRECT_MESSAGE_CHANNEL_API_CALL);
                    if (responseCallback != null && userMetadata != null && userMetadata.getUserId() != null) {
                        userIdList = new ArrayList<>();
                        userIdList.add(PreferenceUtils.getInstance().getUserId());
                        userIdList.add(userMetadata.getUserId());

                        params = new GroupChannelParams()
                                .setPublic(false)
                                .setDistinct(true)
                                .addUserIds(userIdList)
                                .setChannelUrl(userMetadata.getProfile_url())
                                .setName(userMetadata.getNickname());

                        GroupChannel.createChannel(params, new GroupChannel.GroupChannelCreateHandler() {
                            @Override
                            public void onResult(GroupChannel groupChannel, SendBirdException exception) {
                                releaseMemory();
                                if (exception != null) {
                                    responseCallback.responseCallback(null);
                                    return;
                                }
                                responseCallback.responseCallback(groupChannel);


                            }
                        });

                    }
                }
            }
        }).start();

    }

    @Override
    public void createGroupMessageChannelApiCall(final Context context, final IResponseCallback responseCallback, final GroupMetadata groupMetadata) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (bundle == null)
                    bundle = new Bundle();
                bundle.putInt(StringConstants.TYPE, IntegerConstant.GROUP_MESSAGE_CHANNEL_API_CALL);
                userIdList = new ArrayList<>();
                if (groupMetadata != null && groupMetadata.getDescription() != null && groupMetadata.getGroupname() != null && groupMetadata.getUseridlist() != null && groupMetadata.getUseridlist().size() >= 2) {
                    userIdList.add(SendBird.getCurrentUser().getUserId());
                    userIdList = groupMetadata.getUseridlist();
                    userIdList.add(PreferenceUtils.getInstance().getUserId());
                    params = new GroupChannelParams()
                            .setPublic(true)
                            .setDistinct(false)
                            .setData(groupMetadata.getDescription())
                            .addUserIds(userIdList)
                            .setName(groupMetadata.getGroupname());
///.setChannelUrl(groupMetadata.getGrouppic_url())
                    GroupChannel.createChannel(params, new GroupChannel.GroupChannelCreateHandler() {
                        @Override
                        public void onResult(GroupChannel groupChannel, SendBirdException exception) {
                            if (exception != null) {
                                bundle.putString(StringConstants.ERROR, exception.getMessage());
                                responseCallback.onFailure(bundle);
                                return;
                            }

                            responseCallback.onSuccess(bundle);
                        }
                    });
                }
            }
        }).start();

    }


    @Override
    public void getGroupChannelList() {
        GroupChannelListQuery channelListQuery = GroupChannel.createMyGroupChannelListQuery();
        channelListQuery.setIncludeEmpty(true);
        channelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
            @Override
            public void onResult(List<GroupChannel> list, SendBirdException e) {
                if (e != null) {    // Error.
                    return;
                }
                DatabaseHelpher.getInstance().insertGroupChannelList(list);

            }
        });

    }

    public void releaseMemory() {
        userIdList = null;
        params = null;
    }

    public void getPreviousMessage(GroupChannel mChannel) {
        mChannel.getPreviousMessagesByTimestamp(DateUtils.getCurrentTimeStamp(), false, 200, true,
                BaseChannel.MessageTypeFilter.USER, null, new BaseChannel.GetMessagesHandler() {
                    @Override
                    public void onResult(List<BaseMessage> list, SendBirdException e) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }
                        DatabaseHelpher.getInstance().insertAllMessagesFromserver(list);

                    }
                });
    }

    public void addUserToGroup(GroupChannel groupChannel, List<String> userIds) {
        groupChannel.inviteWithUserIds(userIds, new GroupChannel.GroupChannelInviteHandler() {
            @Override
            public void onResult(SendBirdException e) {
                if (e != null) {    // Error.
                    return;
                }
            }
        });

    }

    @Override
    public void updateGroupData(GroupChannel groupChannel, String groupname, String description, File imageurl, final ResponseCallback<Boolean> iResponseCallback) {
        if (groupChannel != null) {
            params = new GroupChannelParams();
            if (groupname != null) {
                params.setName(groupname);
            } else if (description != null) {
                params.setData(description);
            } else if (imageurl != null) {
                ClientFactory.getConnectionManagerClientInstance().connect(PreferenceUtils.getInstance().getUserId(), null);
                params.setCoverImage(imageurl);
            }
            if (params != null) {
                groupChannel.updateChannel(params, new GroupChannel.GroupChannelUpdateHandler() {
                    @Override
                    public void onResult(GroupChannel groupChannel, SendBirdException e) {
                        if (e != null) {
                            Log.e(TAG, e.getMessage());
                            iResponseCallback.responseCallback(false);
                            return;
                        }
                        BaseApplication.groupChannel = groupChannel;
                        iResponseCallback.responseCallback(true);
                    }
                });
            }
        }
    }


}
