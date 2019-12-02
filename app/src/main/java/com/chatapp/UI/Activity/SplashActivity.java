package com.chatapp.UI.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.R;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.Utils.CommonUtils;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.PreferenceUtils;

public class SplashActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashTheme);
        CommonUtils.getInstance().updateNetworkConnection(this, null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!PreferenceUtils.getInstance().getRememberme()) {
                    intent = new Intent(SplashActivity.this, Registration_loginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ClientFactory.getConnectionManagerClientInstance().connect(PreferenceUtils.getInstance().getUserId(), new ResponseCallback<Boolean>() {
                        @Override
                        public void responseCallback(Boolean data) {
                        }
                    });

                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, IntegerConstant.SPLASH_TIME_OUT);
    }
}
