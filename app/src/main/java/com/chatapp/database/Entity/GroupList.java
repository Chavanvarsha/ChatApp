package com.chatapp.database.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class GroupList {

    @PrimaryKey
    @NonNull
    String group_url;
    byte[] groupchannel;
    int isDistinct;
    long lastmessageTimeStamp;

    public long getLastmessageTimeStamp() {
        return lastmessageTimeStamp;
    }

    public void setLastmessageTimeStamp(long lastmessageTimeStamp) {
        this.lastmessageTimeStamp = lastmessageTimeStamp;
    }


    public String getGroup_url() {
        return group_url;
    }

    public void setGroup_url(String group_url) {
        this.group_url = group_url;
    }

    public byte[] getGroupchannel() {
        return groupchannel;
    }

    public void setGroupchannel(byte[] groupchannel) {
        this.groupchannel = groupchannel;
    }

    public GroupList(@NonNull String group_url, byte[] groupchannel, Integer isDistinct) {
        this.group_url = group_url;
        this.groupchannel = groupchannel;
        this.isDistinct = isDistinct;
    }

    public int getIsDistinct() {
        return isDistinct;
    }

    public void setIsDistinct(int isDistinct) {
        this.isDistinct = isDistinct;
    }
}
