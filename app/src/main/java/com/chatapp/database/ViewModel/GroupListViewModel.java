package com.chatapp.database.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.chatapp.database.Entity.GroupList;
import com.chatapp.database.OfflineRepository;

import java.util.List;

public class GroupListViewModel extends AndroidViewModel {

    private OfflineRepository offlineRepository;
    private LiveData<List<GroupList>> listLiveData;

    public GroupListViewModel(@NonNull Application application) {
        super(application);
        if (offlineRepository == null)
            offlineRepository = new OfflineRepository(application);
    }

    public LiveData<List<GroupList>> getLiveGroupList(boolean isDistinct) {
        listLiveData = offlineRepository.getGroupLiveData(isDistinct);
        return listLiveData;
    }
}
