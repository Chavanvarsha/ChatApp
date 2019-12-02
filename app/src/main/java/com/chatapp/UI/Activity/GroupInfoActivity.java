package com.chatapp.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.R;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.UI.adapter.UserListAdapter;
import com.chatapp.Utils.FileUtils;
import com.chatapp.Utils.ImageUtils;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.StringConstants;
import com.chatapp.database.Entity.UserList;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.User;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

public class GroupInfoActivity extends BaseActivity implements View.OnClickListener {

    UserListAdapter userListAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    GroupChannel groupChannel;
    TextView groupname, no_of_participants, groupdescription;
    ImageView back, pic;
    Button descriptionView, groupnameView, grouppicView, addParticipants;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        groupChannel = BaseApplication.groupChannel;
        initializeView();
    }

    public void initializeView() {
        addParticipants = findViewById(R.id.btn_addparicipants);
        descriptionView = findViewById(R.id.btn_status);
        groupnameView = findViewById(R.id.btn_username);
        grouppicView = findViewById(R.id.btn_changeprofilepic);
        groupdescription = findViewById(R.id.tv_groupdescrption);
        no_of_participants = findViewById(R.id.tv_no_of_participants);
        pic = findViewById(R.id.iv_grouppic);
        back = findViewById(R.id.iv_backbutton);
        groupname = findViewById(R.id.tv_chatScreenname);
        back.setOnClickListener(this);
        addParticipants.setOnClickListener(this);
        descriptionView.setOnClickListener(this);
        groupnameView.setOnClickListener(this);
        grouppicView.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerview);
        userListAdapter = new UserListAdapter(IntegerConstant.GROUP_INFO);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        userListAdapter.setMenberList(groupChannel.getMembers());
        recyclerView.setAdapter(userListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (groupChannel != null) {
            groupname.setText(groupChannel.getName());
            groupdescription.setText(groupChannel.getData());
            no_of_participants.setText(groupChannel.getMemberCount() + " participants");
            ImageUtils.displayImageFromUrl(this, groupChannel.getCoverUrl(), pic);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addparicipants:
                if (BaseApplication.isConnectedtoNetwork)
                    startActivity(new Intent(this, CreateUserListActivity.class).putExtra(StringConstants.TYPE, IntegerConstant.ADD_USERS));
                break;
            case R.id.iv_backbutton:
                finish();
                break;
            case R.id.btn_status:
                if (BaseApplication.isConnectedtoNetwork)
                    startActivity(new Intent(this, EditActivity.class).putExtra(StringConstants.TYPE, IntegerConstant.EDIT_STATUS).putExtra(StringConstants.ACTIVITY_TYPE, IntegerConstant.GROUP));
                break;
            case R.id.btn_username:
                if (BaseApplication.isConnectedtoNetwork) {
                    startActivity(new Intent(this, EditActivity.class).putExtra(StringConstants.TYPE, IntegerConstant.EDIT_USERNAME).putExtra(StringConstants.ACTIVITY_TYPE, IntegerConstant.GROUP));
                }
                break;
            case R.id.btn_changeprofilepic:
                if (BaseApplication.isConnectedtoNetwork) {
                    requestMedia();
                    break;
                }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntegerConstant.INTENT_REQUEST_CHOOSE_MEDIA && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            Hashtable<String, Object> info = FileUtils.getFileInfo(this, uri);

            if (info != null) {
                final String path = (String) info.get("path");
                file = new File(path);
                file = FileUtils.compressFile(file);
                ClientFactory.getGroupChannelClientInstance().updateGroupData(groupChannel, null, null, file, new ResponseCallback<Boolean>() {
                    @Override
                    public void responseCallback(Boolean data) {
                        if (data) {
                            Toast.makeText(GroupInfoActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            ImageUtils.displayImageFromUrl(GroupInfoActivity.this, groupChannel.getCoverUrl(), pic);
                        } else
                            Toast.makeText(GroupInfoActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }
    }
}
