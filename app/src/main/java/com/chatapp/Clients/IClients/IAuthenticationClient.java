package com.chatapp.Clients.IClients;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.chatapp.Callbacks.IResponseCallback;
import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.model.UserDetails;

public interface IAuthenticationClient {

    public void getRegistrationApiCall(Context context, IResponseCallback responseCallback, UserDetails userDetails, String paassword);

    public void getLoginApiCall(Context context, IResponseCallback responseCallback, String emailid, String password);

    public void checkUserExitOrNot(final String emailId, final ResponseCallback<Boolean> finishcallback);
}
