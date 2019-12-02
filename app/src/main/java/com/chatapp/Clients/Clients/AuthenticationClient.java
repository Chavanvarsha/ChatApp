package com.chatapp.Clients.Clients;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.chatapp.Callbacks.IResponseCallback;
import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.Clients.IClients.IAuthenticationClient;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.R;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.UI.Activity.Registration_loginActivity;
import com.chatapp.Utils.CommonUtils;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.PreferenceUtils;
import com.chatapp.Utils.StringConstants;
import com.chatapp.model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sendbird.android.SendBird;

public class AuthenticationClient implements IAuthenticationClient {

    String TAG = getClass().getSimpleName();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    Bundle bundle = new Bundle();
    Query query;

    @Override
    public void getRegistrationApiCall(final Context context, final IResponseCallback responsecallback, final UserDetails userDetails, final String password) {
        if (bundle != null) {
            bundle.putInt(StringConstants.TYPE, IntegerConstant.REGISTRATION_API_CALL);
            if (userDetails != null && userDetails.getEmail_id() != null && password != null && BaseApplication.isConnectedtoNetwork)
                checkUserExitOrNot(userDetails.getEmail_id(), new ResponseCallback<Boolean>() {
                    @Override
                    public void responseCallback(Boolean flag) {
                        if (flag) {
                            Log.i(TAG, "user already exit");
                            bundle.putString(StringConstants.ERROR, context.getResources().getString(R.string.user_already_exit));
                            responsecallback.onFailure(bundle);

                        } else {
                            if (auth == null)
                                auth = FirebaseAuth.getInstance();
                            //Adding user email_id and password in firebase Authentication
                            auth.createUserWithEmailAndPassword(userDetails.getEmail_id(), password).addOnCompleteListener(((Registration_loginActivity) context), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //Adding user data in Firebase Database
                                        if (databaseReference == null)
                                            databaseReference = FirebaseDatabase.getInstance().getReference(StringConstants.USERS);
                                        databaseReference.child(databaseReference.push().getKey()).setValue(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                if (CommonUtils.getInstance().checkContextExistOrNot(context)) {
                                                    bundle.putString(StringConstants.SUCCESS, context.getResources().getString(R.string.registration_successful));
                                                    responsecallback.onSuccess(bundle);
                                                }
                                            }
                                        });

                                    } else {
                                        if (CommonUtils.getInstance().checkContextExistOrNot(context)) {
                                            bundle.putString(StringConstants.ERROR, task.getException().getMessage());
                                            responsecallback.onFailure(bundle);
                                        }
                                    }
                                }
                            });


                        }
                    }
                });
        }

    }

    @Override
    public void getLoginApiCall(final Context context, final IResponseCallback responseCallback, final String emailid, String password) {
        if (bundle != null)
            bundle.putInt(StringConstants.TYPE, IntegerConstant.LOGIN_API_CALL);
        if (auth != null && emailid != null && password != null && context != null && BaseApplication.isConnectedtoNetwork) {
            auth.signInWithEmailAndPassword(emailid, password)
                    .addOnCompleteListener((Registration_loginActivity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (auth.getUid() != null && BaseApplication.isConnectedtoNetwork)
                                    ClientFactory.getConnectionManagerClientInstance().connect(auth.getUid() + "", new ResponseCallback<Boolean>() {
                                        @Override
                                        public void responseCallback(Boolean flag) {
                                            if (CommonUtils.getInstance().checkContextExistOrNot(context)) {
                                                if (flag) {
                                                    PreferenceUtils.getInstance().setUserEmailID(emailid);
                                                    PreferenceUtils.getInstance().setUserId(auth.getUid());
                                                    PreferenceUtils.getInstance().setUserNickName(SendBird.getCurrentUser().getNickname());
                                                    bundle.putString(StringConstants.SUCCESS, context.getString(R.string.login_successful));
                                                    responseCallback.onSuccess(bundle);
                                                } else {
                                                    bundle.putString(StringConstants.ERROR, context.getString(R.string.cant_connect_to_server));
                                                    responseCallback.onFailure(bundle);
                                                }
                                            }

                                        }
                                    });
                            } else {
                                if (CommonUtils.getInstance().checkContextExistOrNot(context)) {
                                    bundle.putString(StringConstants.ERROR, context.getString(R.string.invalid_username_or_password));
                                    responseCallback.onFailure(bundle);
                                }
                            }

                        }
                    });
        } else {
            bundle.putString(StringConstants.ERROR, context.getString(R.string.cant_connect_to_server));
            responseCallback.onFailure(bundle);
        }

    }

    @Override
    public void checkUserExitOrNot(final String emailId, final ResponseCallback<Boolean> finishcallback) {
        if (emailId != null) {
            if (query == null)
                query = FirebaseDatabase.getInstance().getReference().child(StringConstants.USERS);
            if (query != null && finishcallback != null)
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null)
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserDetails user = snapshot.getValue(UserDetails.class);
                                if (user != null && user.getEmail_id() != null)
                                    if (emailId.equalsIgnoreCase(user.getEmail_id())) {
                                        finishcallback.responseCallback(true);
                                        return;
                                    }
                            }
                        finishcallback.responseCallback(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        finishcallback.responseCallback(false);
                    }
                });
        }
    }
}
