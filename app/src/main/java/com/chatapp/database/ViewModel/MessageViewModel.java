package com.chatapp.database.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.chatapp.database.OfflineRepository;
import com.sendbird.android.BaseMessage;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {

    OfflineRepository offlineRepository;
    private LiveData<List<byte[]>> listLiveData;

    public MessageViewModel(@NonNull Application application) {
        super(application);
        if (offlineRepository == null)
            offlineRepository = new OfflineRepository(application);
    }

    public LiveData<List<byte[]>> getLiveMessage(String channelurl) {
        if (channelurl != null)
            listLiveData = offlineRepository.getMessagesOfParticularChannel(channelurl);
        return listLiveData;
    }
}
