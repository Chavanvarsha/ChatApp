package com.chatapp.database.helpher;

import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.database.Entity.MessageTable;
import com.chatapp.database.Entity.UserList;
import com.chatapp.database.OfflineRepository;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.User;
import com.sendbird.android.UserMessage;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelpher {

    private static DatabaseHelpher instance;

    private static OfflineRepository offlineRepository;

    public static DatabaseHelpher getInstance() {
        if (instance == null)
            instance = new DatabaseHelpher();
        if (offlineRepository == null)
            offlineRepository = new OfflineRepository(BaseApplication.getInstance);
        return instance;
    }

    public void insertUserList(List<User> userList) {
        offlineRepository.insertUserData(userList);
    }

    public void getAllUserList(final ResponseCallback<List<User>> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<UserList> userLists = offlineRepository.getAllUserDatalist();
                List<User> userlist = new ArrayList<>();
                for (int i = 0; i < userLists.size(); i++) {
                    userlist.add(userLists.get(i).getUsers());
                }
                callback.responseCallback(userlist);
            }
        }).start();

    }

    public void updateUserdata(User user) {
        offlineRepository.updateUserData(user.getUserId(), user);
    }

    public void getParticularUserList(final String userid, final ResponseCallback<UserList> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                callback.responseCallback(offlineRepository.getParticularUserData(userid));
            }
        }).start();
    }

    public void insertBaseMessage(BaseMessage baseMessage) {
        if (baseMessage != null) {
            MessageTable messageTable = new MessageTable(baseMessage.getMessageId(), baseMessage.getChannelUrl(), baseMessage.getCreatedAt(), baseMessage.serialize());
            offlineRepository.insertBaseMessage(messageTable);
        }
    }


    public void insertUserMessage(UserMessage userMessage) {
        if (userMessage != null) {
            MessageTable messageTable = new MessageTable(userMessage.getMessageId(), userMessage.getChannelUrl(), userMessage.getCreatedAt(), userMessage.serialize());
            offlineRepository.insertBaseMessage(messageTable);
        }
    }

    public void insertAllMessagesFromserver(List<BaseMessage> list) {
        MessageTable messageTable;
        MessageTable[] messagelist = new MessageTable[list.size()];
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                messageTable = new MessageTable(list.get(i).getMessageId(), list.get(i).getChannelUrl(), list.get(i).getCreatedAt(), list.get(i).serialize());
                messagelist[i] = messageTable;
            }
            offlineRepository.insertBaseMessagelist(messagelist);
        }

    }

    /*public void getMessages(final String channelUrl, final ResponseCallback<List<BaseMessage>> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<byte[]> bytesList = offlineRepository.getMessagesOfParticularChannel(channelUrl);
                List<BaseMessage> baseMessageList = new ArrayList<>();
                BaseMessage baseMessage;
                for (int i = 0; i < bytesList.size(); i++) {
                    baseMessage = BaseMessage.buildFromSerializedData(bytesList.get(i));
                    baseMessageList.add(baseMessage);
                }
                if (callback != null)
                    callback.responseCallback(baseMessageList);

            }
        }).start();
    }*/

    public void insertGroupChannelList(List<GroupChannel> list) {
        offlineRepository.insertGroupChannel(list);
    }

    public void getGroupChannelList(final boolean isDistinct, final ResponseCallback<List<GroupChannel>> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                callback.responseCallback(offlineRepository.getBaseChannelList(isDistinct));
            }
        }).start();

    }

    public void emptyDatabase() {
        offlineRepository.emptyTables();
    }

}
