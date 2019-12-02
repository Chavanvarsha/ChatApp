package com.chatapp.Clients.Clients;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.Clients.IClients.IUserListClient;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.Utils.CommonUtils;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.PreferenceUtils;
import com.chatapp.Utils.StringConstants;
import com.chatapp.database.helpher.DatabaseHelpher;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserListQuery;

import java.util.List;

public class UserListClient implements IUserListClient {

    String TAG = getClass().getSimpleName();
    Bundle bundle = new Bundle();
    GroupChannelListQuery mChannelListQuery;

    @Override
    public void getUserListApiCall(final Context context, final ResponseCallback<List<User>> userList) {
        bundle.putInt(StringConstants.TYPE, IntegerConstant.USER_LIST_API_CALL);
        UserListQuery query = SendBird.createUserListQuery();
        query.next(new UserListQuery.UserListQueryResultHandler() {
            @Override
            public void onResult(List<User> list, SendBirdException e) {
                if (e != null) {
                    if (CommonUtils.getInstance().checkContextExistOrNot(context))
                        userList.responseCallback(null);
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getUserId().equals(PreferenceUtils.getInstance().getUserId())) {
                        list.remove(i);
                        break;
                    }
                }
                if (CommonUtils.getInstance().checkContextExistOrNot(context))
                    userList.responseCallback(list);
            }
        });
    }

    @Override
    public void getGroupListApiCall(final Context context, final ResponseCallback<List<GroupChannel>> responseCallback) {
        if (context != null && responseCallback != null) {
            mChannelListQuery = GroupChannel.createMyGroupChannelListQuery();
            mChannelListQuery.setLimit(IntegerConstant.CHANNEL_LIST_LIMIT);
            mChannelListQuery.setIncludeEmpty(true);
            mChannelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
                @Override
                public void onResult(List<GroupChannel> list, SendBirdException e) {
                    if (e != null) {
                        // Error!
                        e.printStackTrace();
                        return;
                    }
                    DatabaseHelpher.getInstance().insertGroupChannelList(list);
                    for (int i = 0; i < list.size(); i++) {
                        ClientFactory.getGroupChannelClientInstance().getPreviousMessage(list.get(i));
                    }
                    responseCallback.responseCallback(list);


                }
            });
        }
    }


}
