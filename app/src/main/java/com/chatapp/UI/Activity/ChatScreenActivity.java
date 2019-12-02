package com.chatapp.UI.Activity;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chatapp.Callbacks.IChannelListner;
import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.R;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.UI.adapter.MessageAdapter;
import com.chatapp.Utils.CommonUtils;
import com.chatapp.Utils.FileUtils;
import com.chatapp.Utils.ImageUtils;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.PreferenceUtils;
import com.chatapp.Utils.StringConstants;
import com.chatapp.database.ViewModel.MessageViewModel;
import com.chatapp.database.helpher.DatabaseHelpher;
import com.chatapp.listner.ChannelListner;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatScreenActivity extends BaseActivity implements View.OnClickListener, IChannelListner {

    RecyclerView recyclerView;
    EditText message;
    ConstraintLayout rootlayout;
    Button send, add_files;
    ImageView back, userpic;
    LinearLayoutManager linearLayoutManager;
    MessageAdapter messageAdapter;
    GroupChannel mChannel;
    TextView groupname, groupdescription, othertypingstatus;
    Intent intent;
    int id;
    String groupDescription = "";
    MessageViewModel messageViewModel;
    boolean isUserTyping = false;
    private HashMap<BaseChannel.SendFileMessageWithProgressHandler, FileMessage> mFileProgressHandlerMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        back = findViewById(R.id.iv_backbutton);
        mChannel = BaseApplication.groupChannel;
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        ChannelListner.getInstance().channelListner();
        BaseApplication.chatScreenActivity = this;
        initializeView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initializeView() {
        // othertypingstatus = findViewById(R.id.tv_typing);
        userpic = findViewById(R.id.iv_userpic);
        groupdescription = findViewById(R.id.tv_groupdescrption);
        rootlayout = findViewById(R.id.rootlayout);
        groupname = findViewById(R.id.tv_chatScreenname);
        recyclerView = findViewById(R.id.recycler_chat);
        add_files = findViewById(R.id.btn_addfiles);
        message = findViewById(R.id.edittext_chat);
        send = findViewById(R.id.button_chat_send);
        send.setOnClickListener(this);
        groupname.setOnClickListener(this);
        add_files.setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (mChannel != null) {
            messageAdapter = new MessageAdapter(mChannel);
        }
        setAdapter();

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isUserTyping) {
                    setTypingStatus(true);
                }
                setTypingStatus(true);
                if (s.length() == 0) {
                    setTypingStatus(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setTypingStatus(boolean typing) {
        if (mChannel == null) {
            return;
        }

        if (typing) {
            isUserTyping = true;
            mChannel.startTyping();
        } else {
            isUserTyping = false;
            mChannel.endTyping();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mChannel != null) {
            id = CommonUtils.getInstance().getUSerId(mChannel);
            if (mChannel.isDistinct()) {
                ImageUtils.displayRoundImageFromUrl(this, mChannel.getMembers().get(id).getProfileUrl(), userpic);
                groupname.setText(mChannel.getMembers().get(id).getNickname());
                groupDescription = mChannel.getMembers().get(id).getMetaData(StringConstants.STATUS);
                groupdescription.setText(groupDescription);
            } else {
                ImageUtils.displayRoundImageFromUrl(this, mChannel.getCoverUrl(), userpic);
                groupname.setText(mChannel.getName());
                //groupdescription.setText(mChannel.getMemberCount() + " members");
                groupDescription = mChannel.getData();
                groupdescription.setText(groupDescription);
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BaseApplication.groupChannel = null;
        finish();
    }

    public void setAdapter() {
        messageViewModel.getLiveMessage(mChannel.getUrl()).observe(this, new Observer<List<byte[]>>() {
            @Override
            public void onChanged(@Nullable List<byte[]> list) {
                if (BaseApplication.isConnectedtoNetwork)
                    mChannel.markAsRead();
                List<BaseMessage> baseMessageList = new ArrayList<>();
                BaseMessage baseMessage;
                for (int i = 0; i < list.size(); i++) {
                    baseMessage = BaseMessage.buildFromSerializedData(list.get(i));
                    baseMessageList.add(baseMessage);
                }
                messageAdapter.loadPreviousMessages(baseMessageList);
            }
        });
        recyclerView.setAdapter(messageAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_chat_send:
                if (messageAdapter != null && BaseApplication.isConnectedtoNetwork) {
                    messageAdapter.sendMessage(message.getText().toString());
                    message.setText("");
                } else {
                    displayToast(getString(R.string.send_message_error));
                }
                break;
            case R.id.btn_addfiles:
                requestMedia();
                break;
            case R.id.tv_chatScreenname:
                if (mChannel != null && !mChannel.isDistinct())
                    startActivity(new Intent(this, GroupInfoActivity.class));
                break;

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        setTypingStatus(false);
        SendBird.removeChannelHandler(StringConstants.CHANNEL_HANDLER_ID);
    }

    @Override
    public void onChangeListner(BaseChannel baseChannel, BaseMessage baseMessage) {
        if (baseChannel != null && baseMessage != null && baseChannel.getUrl().equals(mChannel.getUrl()) && baseMessage instanceof UserMessage && messageAdapter != null) {
            messageAdapter.appendMessage((UserMessage) baseMessage);
        }
    }

    @Override
    public void onChangeListner(BaseChannel baseChannel) {

    }

    public void requestMedia() {
        if (intent == null)
            intent = new Intent();

        // Pick images or videos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType("*/*");
            String[] mimeTypes = {"image/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        } else {
            intent.setType("image/*");
        }

        intent.setAction(Intent.ACTION_GET_CONTENT);

        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Media"), IntegerConstant.INTENT_REQUEST_CHOOSE_MEDIA);
        SendBird.setAutoBackgroundDetection(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Set this as true to restore background connection management.
        SendBird.setAutoBackgroundDetection(true);

        if (requestCode == IntegerConstant.INTENT_REQUEST_CHOOSE_MEDIA && resultCode == Activity.RESULT_OK) {
            // If user has successfully chosen the image, show a dialog to confirm upload.
            if (data == null) {
                return;
            }
            sendFileWithThumbnail(data.getData());

            // sendFileWithThumbnail(data.getData());
        }
    }

    private void sendFileWithThumbnail(Uri uri) {
        // Specify two dimensions of thumbnails to generate
        List<FileMessage.ThumbnailSize> thumbnailSizes = new ArrayList<>();
        thumbnailSizes.add(new FileMessage.ThumbnailSize(240, 240));
        thumbnailSizes.add(new FileMessage.ThumbnailSize(320, 320));

        Hashtable<String, Object> info = FileUtils.getFileInfo(this, uri);

        if (info == null) {
            Toast.makeText(this, "Extracting file information failed.", Toast.LENGTH_LONG).show();
            return;
        }

        final String path = (String) info.get("path");
        Log.i(TAG, path);
        File file = new File(path);
        file = FileUtils.compressFile(file);
        final String name = file.getName();
        final String mime = (String) info.get("mime");
        final int size = (Integer) info.get("size");

        if (path.equals("")) {
            Toast.makeText(this, "File must be located in local storage.", Toast.LENGTH_LONG).show();
        } else {


            BaseChannel.SendFileMessageWithProgressHandler progressHandler = new BaseChannel.SendFileMessageWithProgressHandler() {
                @Override
                public void onProgress(int bytesSent, int totalBytesSent, int totalBytesToSend) {
                    FileMessage fileMessage = mFileProgressHandlerMap.get(this);
                    if (fileMessage != null && totalBytesToSend > 0) {
                        int percent = (totalBytesSent * 100) / totalBytesToSend;
                        messageAdapter.setFileProgressPercent(fileMessage, percent);

                    }
                }

                @Override
                public void onSent(FileMessage fileMessage, SendBirdException e) {
                    if (e != null) {
                        Toast.makeText(ChatScreenActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //CommonUtils.getInstance().downloadFile(ChatScreenActivity.this,fileMessage.getUrl(), fileMessage.getName());
                    Log.i(TAG, String.valueOf("send success"));
                    messageAdapter.markMessageSent(fileMessage);
                    DatabaseHelpher.getInstance().insertBaseMessage(fileMessage);
                }
            };

            // Send image with thumbnails in the specified dimensions
            FileMessage tempFileMessage = mChannel.sendFileMessage(file, name, mime, size, "", null, thumbnailSizes, progressHandler);

            mFileProgressHandlerMap.put(progressHandler, tempFileMessage);

            messageAdapter.addTempFileMessageInfo(tempFileMessage, uri);
            messageAdapter.addFirst(tempFileMessage);
        }
    }

    public void setDownloadingUrlProgress(String url, int percent) {
        if (messageAdapter != null) {
            messageAdapter.setFileDownloadProgress(url, percent);
        }
    }

    public void removeUrl(String url) {
        if (messageAdapter != null && url != null) {
            messageAdapter.removeFileDownloaded(url);
        }
    }

    public void refreshTypingStatus(List<Member> typingUsers) {

        if (typingUsers.size() > 0) {
            String string;

            if (typingUsers.size() == 1) {
                string = typingUsers.get(0).getNickname() + " is typing";
            } else if (typingUsers.size() == 2) {
                string = typingUsers.get(0).getNickname() + " " + typingUsers.get(1).getNickname() + " is typing";
            } else {
                string = "Multiple users are typing";
            }
            groupdescription.setText(string);
        } else {
            // othertypingstatus.setVisibility(View.GONE);
            groupdescription.setText(groupDescription);
        }


    }

}
