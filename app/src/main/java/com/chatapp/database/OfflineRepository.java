package com.chatapp.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.database.Dao.GroupListDao;
import com.chatapp.database.Dao.MessageDao;
import com.chatapp.database.Dao.UserListDao;
import com.chatapp.database.Entity.GroupList;
import com.chatapp.database.Entity.MessageTable;
import com.chatapp.database.Entity.UserList;
import com.chatapp.database.OfflineDataBase;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.User;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class OfflineRepository {

    private UserListDao userListDao;
    private MessageDao messageDao;
    private GroupListDao groupListDao;
    static OfflineDataBase offlineDataBase;
    GroupList groupList;


    public OfflineRepository(Application application) {
        if (offlineDataBase == null)
            offlineDataBase = OfflineDataBase.getInstance(application);
        userListDao = offlineDataBase.userListDao();
        messageDao = offlineDataBase.messageDao();
        groupListDao = offlineDataBase.groupListDao();
    }

    public void insertUserData(final List<User> userlist) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (userListDao != null) {
                    UserList list;
                    UserList userList[] = new UserList[userlist.size()];
                    for (int i = 0; i < userList.length; i++) {
                        list = new UserList(userlist.get(i).getUserId(), userlist.get(i).serialize());
                        userList[i] = list;
                    }
                    userListDao.insertUserData(userList);
                }
            }
        }).start();
    }

    public List<UserList> getAllUserDatalist() {
        if (userListDao != null) {
            return userListDao.getAllUserList();
        } else
            return null;
    }

    public void updateUserData(final String userid, final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (userListDao != null && userid != null && user != null) {
                    userListDao.updateUserData(user.serialize(), userid);
                }
            }
        }).start();

    }

    public UserList getParticularUserData(String userid) {
        if (userListDao != null) {
            return userListDao.getParticularUSerData(userid);
        }
        return null;
    }

    public void insertBaseMessage(final MessageTable messageTable) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (messageDao != null)
                    messageDao.insertMessages(messageTable);
            }
        }).start();
    }

    public void insertBaseMessagelist(final MessageTable[] messageTable) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (messageDao != null)
                    messageDao.insertMessagesList(messageTable);
            }
        }).start();
    }

    public LiveData<List<byte[]>> getMessagesOfParticularChannel(String channel_url) {
        if (channel_url != null)
            return messageDao.getLiveMessage(channel_url);
        return null;
    }

    public void insertGroupChannel(final List<GroupChannel> list) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (list != null) {
                    GroupList[] groupLists = new GroupList[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        groupList = new GroupList(list.get(i).getUrl(), list.get(i).serialize(), list.get(i).isDistinct() ? 1 : 0);
                        if (list.get(i).getLastMessage() != null)
                            groupList.setLastmessageTimeStamp(list.get(i).getLastMessage().getCreatedAt());
                        else
                            groupList.setLastmessageTimeStamp(list.get(i).getCreatedAt());
                        groupLists[i] = groupList;
                    }
                    groupListDao.insertMessagesList(groupLists);
                }
            }
        }).start();
    }

    public List<GroupChannel> getBaseChannelList(boolean isDistinct) {
        List<GroupList> groupList;
        if (isDistinct) {
            groupList = groupListDao.getDirectMessageGroupChannelList();

        } else {
            groupList = groupListDao.getGroupMessageGroupChannelList();
        }

        List<GroupChannel> baseChannelList = new ArrayList<>();
        for (int i = 0; i < groupList.size(); i++) {
            baseChannelList.add((GroupChannel) GroupChannel.buildFromSerializedData(groupList.get(i).getGroupchannel()));
        }
        return baseChannelList;
    }

    public LiveData<List<GroupList>> getGroupLiveData(boolean isDistinct) {
        if (isDistinct) {
            return groupListDao.getDirectMessageGroupChannelLiveList();
        } else {
            return groupListDao.getGroupMessageGroupChannelLiveList();
        }
    }

    public void emptyTables() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                groupListDao.emptyTable();
                messageDao.emptyTable();
                userListDao.emptyTable();
            }
        }).start();

    }


}
