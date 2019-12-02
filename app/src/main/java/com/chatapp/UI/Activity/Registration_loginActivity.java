package com.chatapp.UI.Activity;

import android.content.Intent;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.chatapp.Callbacks.IResponseCallback;
import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.R;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.Utils.CommonUtils;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.PreferenceUtils;
import com.chatapp.Utils.StringConstants;
import com.chatapp.model.UserDetails;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.sendbird.android.GroupChannel;

import java.util.List;

import rx.Observer;

public class Registration_loginActivity extends BaseActivity implements IResponseCallback, View.OnClickListener {

    String TAG = getClass().getSimpleName();
    EditText username, password, emailid, confirmpassword, register_password;
    Button login, register;
    TextView type_register_login, type_prefix;
    ConstraintLayout loginLayout, registerLayout;
    boolean isRegisterLayoutVisible = false;
    UserDetails userDetails = new UserDetails();
    Switch rememberme;
    boolean isEmailValid, isPasswordValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initializeView();
        dismissKeyboard();
    }

    public void initializeView() {
        rememberme = findViewById(R.id.switch_remember_me);
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.btn_login);
        loginLayout = findViewById(R.id.layout_login);
        registerLayout = findViewById(R.id.layout_register);
        type_register_login = findViewById(R.id.tv_register);
        type_prefix = findViewById(R.id.tv_prefix);
        emailid = findViewById(R.id.et_registerusername);
        confirmpassword = findViewById(R.id.et_registerConfirmPassword);
        register_password = findViewById(R.id.et_registerpassword);
        register = findViewById(R.id.btn_register);

        //onClickListner
        type_register_login.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);

       /* login.setAlpha(0.5f);
        validateViews();*/
    }

    @Override
    public void onSuccess(final Bundle bundle) {

        if (bundle != null) {
            switch (bundle.getInt(StringConstants.TYPE)) {
                case IntegerConstant.REGISTRATION_API_CALL:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displaytoast(bundle.getString(StringConstants.SUCCESS));
                            emailid.setText("");
                            register_password.setText("");
                            confirmpassword.setText("");
                        }
                    });

                    break;
                case IntegerConstant.LOGIN_API_CALL:
                    PreferenceUtils.getInstance().setRememberme(rememberme.isChecked());
                    displayToast(bundle.getString(StringConstants.SUCCESS));
                    startActivity(new Intent(this, InitializingActivity.class));
                    finish();
                    break;
            }
        }

    }

    @Override
    public void onFailure(Bundle bundle) {
        if (bundle != null) {
            switch (bundle.getInt(StringConstants.TYPE)) {
                case IntegerConstant.REGISTRATION_API_CALL:
                    displaytoast(bundle.getString(StringConstants.ERROR));
                    break;
                case IntegerConstant.LOGIN_API_CALL:
                    password.setText("");
                    displaytoast(bundle.getString(StringConstants.ERROR));
                    break;
            }
        }
        Log.i(TAG, StringConstants.ERROR);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                if (isRegisterLayoutVisible)
                    setLoginlayoutVisible(true);
                else
                    setLoginlayoutVisible(false);
                break;
            case R.id.btn_login:
                clearFocus();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        checkLoginCredentials();
                    }
                }).start();

                break;
            case R.id.btn_register:
                clearFocus();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        checkRegistrationCredentials();
                    }
                }).start();
                break;
        }

    }

    public void checkLoginCredentials() {

        switch (1) {
            case 1:
                if (username != null && username.getText().toString().equals("") && password != null && password.getText().toString().equals("")) {
                    displaytoast(getString(R.string.enter_email_and_password));
                    break;
                }
            case 2:
                if (username != null && username.getText().toString().equals("")) {
                    displaytoast(getString(R.string.enter_emailid));
                    break;
                }
            case 3:
                if (password != null && password.getText().toString().equals("")) {
                    displaytoast(getString(R.string.enter_password));
                    break;
                }
            case 4:
                if (!CommonUtils.isEmailIdValid(username.getText().toString())) {
                    displaytoast(getString(R.string.email_not_valid));
                    break;
                }
            case 5:
                dismissKeyboard();
                if (BaseApplication.isConnectedtoNetwork) {
                    enableButtons(false);
                    loginApiCall();
                } else displayToast(getString(R.string.no_internet_connection));
                break;
        }
    }

    public void checkRegistrationCredentials() {
        switch (1) {
            case 1:
                if (emailid != null && emailid.getText().toString().equals("") &&
                        register_password != null && register_password.getText().toString().equals("") &&
                        confirmpassword != null && confirmpassword.getText().toString().equals("")) {
                    displaytoast(getString(R.string.enter_all_fields));
                    break;
                }
            case 2:
                if (emailid != null && emailid.getText().toString().equals("")) {
                    displaytoast(getString(R.string.enter_emailid));
                    break;
                }
            case 3:
                if (!CommonUtils.isEmailIdValid(emailid.getText().toString())) {
                    displaytoast(getString(R.string.email_not_valid));
                    break;
                }
            case 4:
                if (register_password != null && register_password.getText().toString().equals("")) {
                    displaytoast(getString(R.string.enter_password));
                    break;
                }
            case 5:
                if (confirmpassword != null && confirmpassword.getText().toString().equals("")) {
                    displaytoast(getString(R.string.enter_confirmpassword));
                    break;
                }
            case 6:
                if (!register_password.getText().toString().equals(confirmpassword.getText().toString())) {
                    displaytoast(getString(R.string.password_not_match));
                    break;
                }
            case 7:
                dismissKeyboard();
                if (BaseApplication.isConnectedtoNetwork) {
                    enableButtons(false);
                    registrationApiCall();
                } else
                    displayToast(getString(R.string.no_internet_connection));
                break;

        }
    }


    public void enableButtons(final boolean flag) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag) {
                    register.setAlpha(1f);
                    login.setAlpha(1f);
                    register.setClickable(true);
                    login.setClickable(true);
                    login.setVisibility(View.VISIBLE);
                    register.setVisibility(View.VISIBLE);
                    type_register_login.setClickable(true);
                    rememberme.setClickable(true);
                } else {
                    clearFocus();
                    register.setAlpha(0.2f);
                    login.setAlpha(0.2f);
                    register.setClickable(false);
                    login.setClickable(false);
                    login.setVisibility(View.GONE);
                    register.setVisibility(View.GONE);
                    type_register_login.setClickable(false);
                    rememberme.setClickable(false);
                }
            }
        });

    }

    public void clearFocus() {
        username.clearFocus();
        password.clearFocus();
        emailid.clearFocus();
        register_password.clearFocus();
        confirmpassword.clearFocus();
    }

    public void setLoginlayoutVisible(boolean flag) {
        dismissKeyboard();
        emailid.setText("");
        password.setText("");
        confirmpassword.setText("");
        register_password.setText("");
        username.setText("");
        if (flag) {
            isRegisterLayoutVisible = false;
            loginLayout.setVisibility(View.VISIBLE);
            registerLayout.setVisibility(View.GONE);
            type_register_login.setText(getString(R.string.register));
            type_prefix.setText(getString(R.string.don_t_have_an_acccount));
        } else {
            isRegisterLayoutVisible = true;
            loginLayout.setVisibility(View.GONE);
            registerLayout.setVisibility(View.VISIBLE);
            type_register_login.setText(R.string.login);
            type_prefix.setText(getString(R.string.already_have_an_account));
        }
    }

    public void registrationApiCall() {
        userDetails.setEmail_id(emailid.getText().toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientFactory.getAuthenticationClientInstance().getRegistrationApiCall(Registration_loginActivity.this, Registration_loginActivity.this, userDetails, register_password.getText().toString());
            }
        }).start();
    }

    public void loginApiCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientFactory.getAuthenticationClientInstance().getLoginApiCall(Registration_loginActivity.this, Registration_loginActivity.this, username.getText().toString(), password.getText().toString());
            }
        }).start();
    }

    private void displaytoast(CharSequence charSequence) {
        enableButtons(true);
        displayToast(charSequence);
    }

    //Using RxJava
    public void validateViews() {
        RxTextView.textChanges(username).skip(1).subscribe(new Observer<CharSequence>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CharSequence charSequence) {
                if (isEmailidValid(String.valueOf(charSequence))) {
                    username.setError(null);
                    isEmailValid = true;
                } else {
                    username.setError("Invalid Email ID");
                    isEmailValid = false;
                }
                enableButton();
            }
        });
        RxTextView.textChanges(password).skip(1).subscribe(new Observer<CharSequence>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CharSequence charSequence) {
                if (charSequence.length() > 5) {
                    password.setError(null);
                    isPasswordValid = true;
                } else {
                    password.setError("Password Should be greater than 5");
                    isPasswordValid = false;
                }
                enableButton();
            }
        });
    }

    public void enableButton() {
        if (isEmailValid && isPasswordValid) {
            login.setAlpha(1f);
        } else {
            login.setAlpha(0.5f);
        }
    }

    public boolean isEmailidValid(String text) {
        if (text != null && !text.isEmpty()) {
            // return Patterns.PHONE.matcher(text).matches();
            return Patterns.EMAIL_ADDRESS.matcher(text).matches();
        }
        return false;
    }

}