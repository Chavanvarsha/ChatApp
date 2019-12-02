package com.chatapp.database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.chatapp.database.Entity.MessageTable;

import java.util.List;

@Dao
public interface MessageDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessages(MessageTable messageTable);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessagesList(MessageTable... messageTable);


    @Query("Select data from MessageTable where channel_url Like:channelurl ORDER BY message_ts DESC")
    List<byte[]> getMessage(String channelurl);


    @Query("Select data from MessageTable where channel_url Like:channelurl ORDER BY message_ts DESC")
    LiveData<List<byte[]>> getLiveMessage(String channelurl);

    @Query("DELETE FROM MessageTable")
    public void emptyTable();

}
