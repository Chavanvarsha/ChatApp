package com.chatapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.sendbird.android.GroupChannel;

import java.io.Serializable;
import java.util.List;

public class CustomGroupChannel implements Parcelable {

    private GroupChannel groupChannel;

    public CustomGroupChannel(GroupChannel groupChannel) {
        this.groupChannel = groupChannel;
    }

    protected CustomGroupChannel(Parcel in) {
    }

    public static final Creator<CustomGroupChannel> CREATOR = new Creator<CustomGroupChannel>() {
        @Override
        public CustomGroupChannel createFromParcel(Parcel in) {
            return new CustomGroupChannel(in);
        }

        @Override
        public CustomGroupChannel[] newArray(int size) {
            return new CustomGroupChannel[size];
        }
    };

    public GroupChannel getGroupChannel() {
        return groupChannel;
    }

    public void setGroupChannel(GroupChannel groupChannel) {
        this.groupChannel = groupChannel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
