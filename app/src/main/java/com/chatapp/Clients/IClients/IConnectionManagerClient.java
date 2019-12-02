package com.chatapp.Clients.IClients;

import com.chatapp.Callbacks.IResponseCallback;
import com.chatapp.Callbacks.ResponseCallback;

public interface IConnectionManagerClient {

    public void connect(String userId, ResponseCallback<Boolean> flag);


    public void disconnect();

}
