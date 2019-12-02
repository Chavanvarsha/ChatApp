package com.chatapp.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chatapp.Callbacks.IResponseCallback;
import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.R;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.UI.adapter.UserListAdapter;
import com.chatapp.Utils.CommonUtils;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.StringConstants;
import com.chatapp.database.helpher.DatabaseHelpher;
import com.chatapp.model.GroupMetadata;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.User;

import java.util.ArrayList;
import java.util.List;

public class CreateUserListActivity extends BaseActivity implements View.OnClickListener, IResponseCallback {

    RecyclerView recyclerView;
    UserListAdapter userListAdapter;
    LinearLayoutManager linearLayoutManager;
    TextView appbarname, nointernet;
    ImageView tick, back;
    EditText groupname, groupdescription;
    Button cancel, creategroup;
    String gname, gdescription;
    ConstraintLayout createGrouplayout;
    int type;
    ProgressBar progressBar;
    GroupMetadata groupMetadata = new GroupMetadata();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        type = getIntent().getIntExtra(StringConstants.TYPE, 6);
        recyclerView = findViewById(R.id.recyclerview);
        cancel = findViewById(R.id.btn_cancel);
        nointernet = findViewById(R.id.tv_nointernet);
        progressBar = findViewById(R.id.progressbar);
        groupname = findViewById(R.id.et_groupname);
        groupdescription = findViewById(R.id.et_groupdescription);
        creategroup = findViewById(R.id.btn_creategroup);
        appbarname = findViewById(R.id.tv_chatScreenname);
        tick = findViewById(R.id.iv_tick);
        back = findViewById(R.id.iv_backbutton);
        createGrouplayout = findViewById(R.id.create_group_layout);
        if (type == IntegerConstant.CREATE_GROUP) {
            tick.setVisibility(View.VISIBLE);
            tick.setOnClickListener(this);
            appbarname.setText(R.string.creategroup);
        } else if (type == IntegerConstant.START_CHAT) {
            tick.setVisibility(View.GONE);
            appbarname.setText(R.string.startchat);
        } else if (type == IntegerConstant.ADD_USERS) {
            tick.setVisibility(View.VISIBLE);
            tick.setOnClickListener(this);
            appbarname.setText(R.string.adduser);
        }
        setProgressBar(true);
        tick.setOnClickListener(this);
        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        creategroup.setOnClickListener(this);
        userListAdapter = new UserListAdapter(type);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        getUserList();
        recyclerView.setAdapter(userListAdapter);
    }

    public void getUserList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientFactory.getUserListClientInstance().getUserListApiCall(CreateUserListActivity.this, new ResponseCallback<List<User>>() {
                    @Override
                    public void responseCallback(final List<User> userlist) {
                        if (userlist != null && userListAdapter != null) {
                            if (type == IntegerConstant.CHAT_FRAGMENT) {
                                DatabaseHelpher.getInstance().getGroupChannelList(true, new ResponseCallback<List<GroupChannel>>() {
                                    @Override
                                    public void responseCallback(List<GroupChannel> list) {
                                        for (int i = 0; i < userlist.size(); i++) {
                                            for (int j = 0; j < list.size(); j++) {
                                                if (userlist.get(i).getUserId().equals(list.get(j).getMembers().get(CommonUtils.getInstance().getUSerId(list.get(j))).getUserId())) {
                                                    userlist.remove(i);
                                                    i--;
                                                    break;
                                                }
                                            }
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                userListAdapter.setUserlist(userlist);
                                                setProgressBar(false);
                                            }
                                        });

                                    }
                                });
                            } else if (type == IntegerConstant.START_CHAT) {
                                userListAdapter.setUserlist(userlist);
                                setProgressBar(false);
                            } else if (type == IntegerConstant.ADD_USERS) {
                                GroupChannel groupChannel = BaseApplication.groupChannel;
                                List<Member> list = new ArrayList<>();
                                list = groupChannel.getMembers();
                                for (int i = 0; i < userlist.size(); i++) {
                                    for (int j = 0; j < list.size(); j++) {
                                        if (userlist.get(i).getUserId().equals(list.get(j).getUserId())) {
                                            userlist.remove(i);
                                            i--;
                                            break;
                                        }
                                    }
                                }
                                userListAdapter.setUserlist(userlist);
                                setProgressBar(false);
                            } else {
                                userListAdapter.setUserlist(userlist);
                                setProgressBar(false);
                            }

                        } else {
                            displayToast(getString(R.string.no_internet_connection));
                            progressBar.setVisibility(View.GONE);
                            nointernet.setVisibility(View.VISIBLE);
                        }
                    }

                });
            }
        }).start();
    }

    public void setProgressBar(boolean flag) {
        if (flag) {
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_tick:
                if (type == IntegerConstant.CREATE_GROUP) {
                    dismissKeyboard();
                    if (userListAdapter.getUserid().size() > 1) {
                        recyclerView.setVisibility(View.GONE);
                        createGrouplayout.setVisibility(View.VISIBLE);
                        tick.setVisibility(View.GONE);
                    } else {
                        displayToast(getString(R.string.atleast_3_members));
                    }
                } else {
                    ClientFactory.getGroupChannelClientInstance().addUserToGroup(BaseApplication.groupChannel, userListAdapter.getUserid());
                    startActivity(new Intent(this, ChatScreenActivity.class));
                    finishAndRemoveTask();
                }
                break;
            case R.id.iv_backbutton:
                dismissKeyboard();
                if (createGrouplayout.getVisibility() == View.VISIBLE) {
                    if (!(tick.getVisibility() == View.VISIBLE)) {
                        tick.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        createGrouplayout.setVisibility(View.GONE);
                    } else
                        finish();
                } else
                    finish();
                break;
            case R.id.btn_cancel:
                dismissKeyboard();
                tick.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                createGrouplayout.setVisibility(View.GONE);
                break;
            case R.id.btn_creategroup:
                dismissKeyboard();
                checkValidation();
                break;

        }
    }

    public void checkValidation() {
        gname = groupname.getText().toString();
        gdescription = groupdescription.getText().toString();
        switch (1) {
            case 1:
                if (gname != null && gname.equals("") && gdescription != null && gdescription.equals("")) {
                    displayToast(getString(R.string.enter_all_fields));
                    break;
                }
            case 2:
                if (gname != null && gname.equals("")) {
                    displayToast(getString(R.string.enter_group_name));
                    break;
                }
            case 3:
                if (gdescription != null && gdescription.equals("")) {
                    displayToast(getString(R.string.entergroupdiscription));
                    break;
                }
            case 4:
                if (userListAdapter != null) {
                    userListAdapter.getUserid();
                    groupMetadata.setGroupname(gname);
                    groupMetadata.setUseridlist(userListAdapter.getUserid());
                    groupMetadata.setDescription(gdescription);
                    ClientFactory.getGroupChannelClientInstance().createGroupMessageChannelApiCall(CreateUserListActivity.this, CreateUserListActivity.this, groupMetadata);
                }
                break;


        }
    }

    @Override
    public void onSuccess(Bundle bundle) {
        finish();
    }

    @Override
    public void onFailure(Bundle bundle) {

    }
}
