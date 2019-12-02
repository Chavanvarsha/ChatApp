package com.chatapp.database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.chatapp.database.Entity.UserList;
import com.sendbird.android.User;

import java.util.List;

@Dao
public interface UserListDao {

    @Query("select * from userlist")
    List<UserList> getAllUserList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertUserData(UserList[] userList);

    @Query("select * from userlist where userid like :userId")
    UserList getParticularUSerData(String userId);

    @Query("update userlist set  user=:user where userid=:userid")
    long updateUserData(byte[] user, String userid);

    @Query("DELETE FROM userlist")
    public void emptyTable();
}
