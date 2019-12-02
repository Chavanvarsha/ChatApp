package com.chatapp.Callbacks;

import android.os.Bundle;

public interface IResponseCallback {

    void onSuccess(Bundle bundle);

    void onFailure(Bundle bundle);
}
