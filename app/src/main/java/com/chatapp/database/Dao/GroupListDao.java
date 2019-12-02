package com.chatapp.database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.chatapp.database.Entity.GroupList;
import com.chatapp.database.Entity.MessageTable;

import java.util.List;

@Dao
public interface GroupListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessages(GroupList grouplist);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessagesList(GroupList... grouplist);


    @Query("Select * from GroupList where isDistinct = 1")
    List<GroupList> getDirectMessageGroupChannelList();

    @Query("Select * from GroupList where isDistinct = 0")
    List<GroupList> getGroupMessageGroupChannelList();

    @Query("Select * from GroupList where isDistinct = 1 order by lastmessageTimeStamp DESC")
    LiveData<List<GroupList>> getDirectMessageGroupChannelLiveList();

    @Query("Select * from GroupList where isDistinct = 0 order by lastmessageTimeStamp DESC")
    LiveData<List<GroupList>> getGroupMessageGroupChannelLiveList();


    @Update
    void updateGroupChannel(GroupList groupList);

    @Update
    void updateGroupChannel(GroupList... groupList);

    @Query("DELETE FROM GroupList")
    public void emptyTable();
}
