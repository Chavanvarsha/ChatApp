package com.chatapp.UI.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.R;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.UI.Activity.ChatScreenActivity;
import com.chatapp.UI.Activity.EditActivity;
import com.chatapp.UI.Activity.MainActivity;
import com.chatapp.UI.Activity.Registration_loginActivity;
import com.chatapp.Utils.CommonUtils;
import com.chatapp.Utils.FileUtils;
import com.chatapp.Utils.ImageUtils;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.PreferenceUtils;
import com.chatapp.Utils.StringConstants;
import com.chatapp.database.helpher.DatabaseHelpher;
import com.sendbird.android.SendBird;

import java.io.File;
import java.util.Hashtable;

public class MoreFragment extends Fragment implements View.OnClickListener {

    String TAG = getClass().getSimpleName();
    Button logout;
    Button status, username, changedp;
    ImageView profilepic;
    File file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        initializeView(view);
        return view;
    }

    public void initializeView(View view) {
        status = view.findViewById(R.id.btn_status);
        username = view.findViewById(R.id.btn_username);
        logout = view.findViewById(R.id.btn_logout);
        profilepic = view.findViewById(R.id.iv_profilepic);
        changedp = view.findViewById(R.id.btn_changeprofilepic);
        changedp.setOnClickListener(this);
        logout.setOnClickListener(this);
        status.setOnClickListener(this);
        username.setOnClickListener(this);
        logout.setBackground(CommonUtils.changeDrawableColor(getContext(), R.drawable.button_rounded_shape, Color.RED));
        updateProfilepic();
    }

    private void updateProfilepic() {
        if (BaseApplication.isConnectedtoNetwork && SendBird.getCurrentUser() != null)
            ImageUtils.displayRoundImageFromUrl(getContext(), SendBird.getCurrentUser().getProfileUrl(), profilepic);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                if (BaseApplication.isConnectedtoNetwork) {
                    ClientFactory.getConnectionManagerClientInstance().disconnect();
                    PreferenceUtils.getInstance().emptyPreferences();
                    DatabaseHelpher.getInstance().emptyDatabase();
                    startActivity(new Intent(getActivity(), Registration_loginActivity.class));
                    getActivity().finish();
                }
                break;
            case R.id.btn_status:
                if (BaseApplication.isConnectedtoNetwork)
                    startActivity(new Intent(getActivity(), EditActivity.class).putExtra(StringConstants.TYPE, IntegerConstant.EDIT_STATUS).putExtra(StringConstants.ACTIVITY_TYPE,IntegerConstant.CURRENT_USER));
                break;
            case R.id.btn_username:
                if (BaseApplication.isConnectedtoNetwork) {
                    startActivity(new Intent(getActivity(), EditActivity.class).putExtra(StringConstants.TYPE, IntegerConstant.EDIT_USERNAME).putExtra(StringConstants.ACTIVITY_TYPE,IntegerConstant.CURRENT_USER));
                }
                break;
            case R.id.btn_changeprofilepic:
                if (BaseApplication.isConnectedtoNetwork) {
                    ((MainActivity) getActivity()).requestMedia();
                    break;
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntegerConstant.INTENT_REQUEST_CHOOSE_MEDIA && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            Hashtable<String, Object> info = FileUtils.getFileInfo(getContext(), uri);

            if (info != null) {
                final String path = (String) info.get("path");
                file = new File(path);
                file = FileUtils.compressFile(file);
                ClientFactory.getUpdateUserDetailsClientInstance().updateProfilePic(file, new ResponseCallback<Boolean>() {
                    @Override
                    public void responseCallback(Boolean data) {
                        if (data) {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            updateProfilepic();
                        } else Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }
    }
}
