package com.chatapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.chatapp.database.Dao.GroupListDao;
import com.chatapp.database.Dao.MessageDao;
import com.chatapp.database.Dao.UserListDao;
import com.chatapp.database.Entity.GroupList;
import com.chatapp.database.Entity.MessageTable;
import com.chatapp.database.Entity.UserList;

@Database(entities = {UserList.class, MessageTable.class, GroupList.class}, version = 1)
public abstract class OfflineDataBase extends RoomDatabase {

    private static OfflineDataBase instance;

    public abstract UserListDao userListDao();

    public abstract MessageDao messageDao();

    public abstract GroupListDao groupListDao();

    public static synchronized OfflineDataBase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    OfflineDataBase.class, "offline_db")
                    .fallbackToDestructiveMigration()
                    .build();
        return instance;
    }
}
