package com.chatapp.UI.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chatapp.Callbacks.IResponseCallback;
import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.R;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.Utils.CommonUtils;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.PreferenceUtils;
import com.chatapp.Utils.StringConstants;
import com.sendbird.android.SendBird;

public class EditActivity extends BaseActivity implements View.OnClickListener {

    Button cancel, save;
    EditText data;
    int type, ACTIVITY_TYPE;
    String name = "", status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra(StringConstants.TYPE, 0);
        ACTIVITY_TYPE = getIntent().getIntExtra(StringConstants.ACTIVITY_TYPE, 0);
        setContentView(R.layout.activity_edit);

        cancel = findViewById(R.id.btn_cancel);
        save = findViewById(R.id.btn_save);
        data = findViewById(R.id.et_message);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        cancel.setBackground(CommonUtils.changeDrawableColor(this, R.drawable.button_rounded_shape, Color.GRAY));
        if (ACTIVITY_TYPE == IntegerConstant.CURRENT_USER) {
            if (type == IntegerConstant.EDIT_STATUS) {
                data.setHint(R.string.enterstatus);
                status = SendBird.getCurrentUser().getMetaData(StringConstants.STATUS);
                data.setText(status);
            } else if (type == IntegerConstant.EDIT_USERNAME) {
                name = PreferenceUtils.getInstance().getUserNickName();
                data.setHint(R.string.enter_username);
                data.setText(name);
            }
        } else if (ACTIVITY_TYPE == IntegerConstant.GROUP) {
            if (type == IntegerConstant.EDIT_STATUS && BaseApplication.groupChannel != null) {
                data.setHint(R.string.enterDiscription);
                status = BaseApplication.groupChannel.getData();
                data.setText(status);
            } else if (type == IntegerConstant.EDIT_USERNAME) {
                data.setHint(R.string.enter_group_name);
                name = BaseApplication.groupChannel.getName();
                data.setText(name);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_save:
                dismissKeyboard();
                if (BaseApplication.isConnectedtoNetwork) {
                    if (type == IntegerConstant.EDIT_USERNAME && data.getText().toString() != null) {
                        if (data.getText().toString().length() > 4) {
                            if (ACTIVITY_TYPE == IntegerConstant.CURRENT_USER)
                                ClientFactory.getUpdateUserDetailsClientInstance().updateUserName(data.getText().toString(), null, new ResponseCallback<Boolean>() {
                                    @Override
                                    public void responseCallback(Boolean flag) {
                                        if (flag) {
                                            setToast(getString(R.string.usernameupdated));
                                            PreferenceUtils.getInstance().setUserNickName(data.getText().toString());
                                            finish();
                                        } else {
                                            setToast(StringConstants.ERROR);
                                        }
                                    }
                                });
                            else if (ACTIVITY_TYPE == IntegerConstant.GROUP)
                                ClientFactory.getGroupChannelClientInstance().updateGroupData(BaseApplication.groupChannel, data.getText().toString(), null, null, new ResponseCallback<Boolean>() {
                                    @Override
                                    public void responseCallback(Boolean data) {
                                        if (data)
                                            finish();
                                        else
                                            setToast(getString(R.string.someerror));
                                    }
                                });
                        } else {
                            displayToast(getString(R.string.username_length));
                        }
                    } else {
                        if (type == IntegerConstant.EDIT_STATUS) {
                            if (data.getText().toString().length() > 4) {
                                if (ACTIVITY_TYPE == IntegerConstant.CURRENT_USER)
                                    ClientFactory.getUpdateUserDetailsClientInstance().updateUserStatus(data.getText().toString(), new ResponseCallback<Boolean>() {
                                        @Override
                                        public void responseCallback(Boolean flag) {
                                            if (flag) {
                                                setToast(getString(R.string.status_updated));
                                                finish();
                                            } else {
                                                setToast(StringConstants.ERROR);
                                            }
                                        }
                                    });
                                else if (ACTIVITY_TYPE == IntegerConstant.GROUP)
                                    ClientFactory.getGroupChannelClientInstance().updateGroupData(BaseApplication.groupChannel, null, data.getText().toString(), null, new ResponseCallback<Boolean>() {
                                        @Override
                                        public void responseCallback(Boolean data) {
                                            if (data)
                                                finish();
                                            else
                                                setToast(getString(R.string.someerror));
                                        }
                                    });
                            } else {
                                displayToast(getString(R.string.username_length));
                            }
                        }
                    }
                } else setToast(getString(R.string.no_internet_connection));
                break;
        }
    }

    public void setToast(CharSequence charSequence) {
        displayToast(charSequence);
    }

}
