package com.chatapp.Callbacks;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.User;

import java.util.List;

public interface IChannelListner {

    void onChangeListner(BaseChannel baseChannel, BaseMessage baseMessage);

    void onChangeListner(BaseChannel baseChannel);

    interface IUserListner {
        void onUserChangeListner(List<User> list);
    }
}
