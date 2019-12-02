package com.chatapp.database.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.sendbird.android.User;

@Entity(tableName = "userlist")
public class UserList {

    @PrimaryKey
    @NonNull
    private String userid;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] user;

    public String getUserid() {
        return userid;
    }

    public User getUsers() {
        return User.buildFromSerializedData(user);
    }

    public byte[] getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user.serialize();
    }

    public void setUserid(String userid) {
        this.userid = userid;

    }


    public UserList(@NonNull String userid, @NonNull byte[] user) {
        this.userid = userid;
        this.user = user;
    }

    public UserList(@NonNull String userid, User user) {
        this.userid = userid;
        this.user = user.serialize();
    }
}
