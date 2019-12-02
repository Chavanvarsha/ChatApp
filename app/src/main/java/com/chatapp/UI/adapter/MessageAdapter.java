package com.chatapp.UI.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chatapp.R;
import com.chatapp.UI.Activity.ChatScreenActivity;
import com.chatapp.UI.Activity.MultipleImageViewerActivity;
import com.chatapp.UI.dialog.CustomImageViewer;
import com.chatapp.Utils.CommonUtils;
import com.chatapp.Utils.DateUtils;
import com.chatapp.Utils.ImageUtils;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.PreferenceUtils;
import com.chatapp.Utils.StringConstants;
import com.chatapp.database.helpher.DatabaseHelpher;
import com.chatapp.service.DownloadHelpher;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.downloader.PRDownloader;
import com.downloader.Status;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseMessage> mMessageList = new ArrayList<>();
    View view;
    GroupChannel mChannel;
    ViewGroup parent;
    UserMessage userMessage;
    private Hashtable<String, Uri> mTempFileMessageUriTable = new Hashtable<>();
    private HashMap<FileMessage, CircleProgressBar> mFileMessageMap = new HashMap<>();
    private HashMap<String, CircleProgressBar> mFileDownloadingMap = new HashMap<>();
    boolean isNewDay, isSameUserMessage, isGridView;
    CustomImageViewer customImageViewer;


    public MessageAdapter(GroupChannel mChannel) {
        this.mChannel = mChannel;
        if (customImageViewer == null)
            customImageViewer = new CustomImageViewer();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        switch (viewType) {
            case IntegerConstant.VIEW_TYPE_USER_MESSAGE_ME:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received_layout, parent, false);
                return new ReceivedMessageHolder(view);
            case IntegerConstant.VIEW_TYPE_USER_MESSAGE_OTHER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_send_layout, parent, false);
                return new SentMessageHolder(view);
            case IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_ME:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.group_chat_file_image_me, parent, false);
                return new MyImageFileMessageHolder(view);
            case IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_OTHER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.group_chat_file_image_other, parent, false);
                return new OtherImageFileMessageHolder(view);
            case IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_GRID_OTHER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_single_image_layout, parent, false);
                return new OtherImageFileGridMessageHolder(view);
            case IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_GRID_ME:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_user_images_grid_view, parent, false);
                return new MeImageFileGridMessageHolder(view);


            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.group_created, parent, false);
                return new DefaultViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        /*BaseMessage baseMessage = mMessageList.get(position);
        if (baseMessage instanceof UserMessage) {
            userMessage = (UserMessage) mMessageList.get(position);
            if (position < mMessageList.size() - 1) {
                BaseMessage prevMessage = mMessageList.get(position + 1);
                if (prevMessage instanceof UserMessage)
                    if (userMessage.getSender().getProfileUrl().equals(((UserMessage) prevMessage).getSender().getProfileUrl())) {
                        isSameUserMessage = true;
                    } else {
                        isSameUserMessage = false;
                    }
                if (!DateUtils.hasSameDate(userMessage.getCreatedAt(), prevMessage.getCreatedAt())) {
                    isNewDay = true;
                } else {
                    isNewDay = false;
                }
            } else if (position == mMessageList.size() - 1) {
                isNewDay = true;
                isSameUserMessage = false;
            }

        }*/
        BaseMessage baseMessage = customBaseMessagesList.get(position).getBaseMessage();
        if (baseMessage != null && baseMessage instanceof UserMessage) {
            userMessage = (UserMessage) customBaseMessagesList.get(position).getBaseMessage();
            if (userMessage != null) {
                if (position < customBaseMessagesList.size() - 1) {
                    BaseMessage prevMessage = customBaseMessagesList.get(position + 1).getBaseMessage();
                    if (prevMessage != null && prevMessage instanceof UserMessage) {
                        if (userMessage.getSender().getProfileUrl().equals(((UserMessage) prevMessage).getSender().getProfileUrl())) {
                            isSameUserMessage = true;
                        } else {
                            isSameUserMessage = false;
                        }
                        if (!DateUtils.hasSameDate(userMessage.getCreatedAt(), prevMessage.getCreatedAt())) {
                            isNewDay = true;
                        } else {
                            isNewDay = false;
                        }
                    }
                } else if (position == customBaseMessagesList.size() - 1) {
                    isNewDay = true;
                    isSameUserMessage = false;
                }
            } else {

            }

        } else {
            customBaseMessagesList.get(position).getFileMessageList();
        }
        /*if (baseMessage instanceof FileMessage) {
            //TODO:GRID_VIEW
            FileMessage fileMessage = (FileMessage) mMessageList.get(position);
            if (!fileMessage.getSender().getUserId().equals(PreferenceUtils.getInstance().getUserId())) {
                positionValue.add(position);
                for (int i = 1; i < 6; i++) {
                    if (mMessageList.get(position + i) instanceof FileMessage) {
                        FileMessage fileMessage1 = (FileMessage) mMessageList.get(position);
                        if (!fileMessage1.getSender().getUserId().equals(PreferenceUtils.getInstance().getUserId())) {
                            imagesMap.put(position + i, fileMessage1.getUrl());
                            mMessageList.remove(position + i);
                        }
                    } else {
                        break;
                    }
                }
                if (imagesMap.size() > 3) {
                    isGridView = true;
                } else {
                    isGridView = false;
                }
            } else {
                imagesMap.clear();
                positionValue.clear();
            }
        }*/
        /*if (baseMessage instanceof FileMessage) {
            fileMessage = (FileMessage) mMessageList.get(position);
            if (position < mMessageList.size() - 1 && mMessageList.get(position + 1) instanceof FileMessage) {
                FileMessage nextFileMessage = (FileMessage) mMessageList.get(position + 1);
                if (fileMessage.getSender().getUserId().equals(nextFileMessage.getSender().getUserId())) {
                    imagesarray = new ArrayList<>();
                    imagesarray.add(fileMessage.getUrl());
                } else {
                    imagesarray = null;
                }
            }
        }*/
        switch (holder.getItemViewType()) {
            case IntegerConstant.VIEW_TYPE_USER_MESSAGE_OTHER:
                ((SentMessageHolder) holder).bind(userMessage, isNewDay);
                break;
            case IntegerConstant.VIEW_TYPE_USER_MESSAGE_ME:
                ((ReceivedMessageHolder) holder).bind(userMessage, isNewDay, isSameUserMessage);
                break;
            case IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_ME:
                ((MyImageFileMessageHolder) holder).bind((FileMessage) customBaseMessagesList.get(position).getBaseMessage(), null);
                break;
            case IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_OTHER:
                ((OtherImageFileMessageHolder) holder).bind(position, (FileMessage) customBaseMessagesList.get(position).getBaseMessage(), null);
                break;
            case IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_GRID_OTHER:
                ((OtherImageFileGridMessageHolder) holder).bind(customBaseMessagesList.get(position).getFileMessageList());
                break;
            case IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_GRID_ME:
                ((MeImageFileGridMessageHolder) holder).bind(customBaseMessagesList.get(position).getFileMessageList());
                break;

        }
    }

    @Override
    public int getItemCount() {
       /* if (mMessageList != null && mMessageList.size() > 0)
            return mMessageList.size();
        return 0;*/
        if (customBaseMessagesList != null && customBaseMessagesList.size() > 0) {
            return customBaseMessagesList.size();
        } else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        /*BaseMessage message = mMessageList.get(position);
        if (message != null && message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage) message;
            if (userMessage.getSender().getUserId().equals(PreferenceUtils.getInstance().getUserId())) {
                return IntegerConstant.VIEW_TYPE_USER_MESSAGE_OTHER;
            } else {
                return IntegerConstant.VIEW_TYPE_USER_MESSAGE_ME;
            }
        } else if (message instanceof FileMessage) {
            FileMessage fileMessage = (FileMessage) message;
            if (fileMessage.getType().toLowerCase().startsWith("image")) {
                // If the sender is current user
                if (fileMessage.getSender().getUserId().equals(PreferenceUtils.getInstance().getUserId())) {
                    return IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_ME;
                } else {
                    return IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_OTHER;
                }
            }
        }*/

        if (customBaseMessagesList.get(position).getBaseMessage() != null) {
            BaseMessage message = customBaseMessagesList.get(position).baseMessage;
            if (message != null && message instanceof UserMessage) {
                UserMessage userMessage = (UserMessage) message;
                if (userMessage.getSender().getUserId().equals(PreferenceUtils.getInstance().getUserId())) {
                    return IntegerConstant.VIEW_TYPE_USER_MESSAGE_OTHER;
                } else {
                    return IntegerConstant.VIEW_TYPE_USER_MESSAGE_ME;
                }
            } else if (message instanceof FileMessage) {
                FileMessage fileMessage = (FileMessage) message;
                if (fileMessage.getType().toLowerCase().startsWith("image")) {
                    // If the sender is current user
                    if (fileMessage.getSender().getUserId().equals(PreferenceUtils.getInstance().getUserId())) {
                        return IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_ME;
                    } else {
                        return IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_OTHER;
                    }
                }
            }
        } else {
            if (customBaseMessagesList.get(position).getFileMessageList() != null) {
                if (customBaseMessagesList.get(position).getFileMessageList().get(0).getSender().getUserId().equals(PreferenceUtils.getInstance().getUserId()))
                    return IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_GRID_ME;
                else
                    return IntegerConstant.VIEW_TYPE_FILE_MESSAGE_IMAGE_GRID_OTHER;
            }
        }

        return -1;
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, date;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            date = itemView.findViewById(R.id.tv_date);
        }

        void bind(UserMessage message, boolean isNewDay) {
            if (message != null) {
                if (isNewDay) {
                    date.setText(DateUtils.formatDate(message.getCreatedAt()));
                    date.setVisibility(View.VISIBLE);
                } else {
                    date.setVisibility(View.GONE);
                }
                messageText.setText(message.getMessage());
                // Format the stored timestamp into a readable String using method.
                timeText.setText(DateUtils.formatTime(message.getCreatedAt()));
            }
        }
    }

    private class DefaultViewHolder extends RecyclerView.ViewHolder {

        public DefaultViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText, date;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
            date = itemView.findViewById(R.id.tv_date);
        }

        void bind(UserMessage message, boolean isNewDay, boolean isSameUserMessage) {
            if (message != null) {
                if (isNewDay) {
                    date.setText(DateUtils.formatDate(message.getCreatedAt()));
                    date.setVisibility(View.VISIBLE);
                } else {
                    date.setVisibility(View.GONE);
                }
                if (mChannel.isDistinct()) {
                    profileImage.setVisibility(View.GONE);
                    nameText.setVisibility(View.GONE);
                } else {
                    if (isSameUserMessage) {
                        profileImage.setVisibility(View.INVISIBLE);
                        nameText.setVisibility(View.GONE);
                    } else {
                        profileImage.setVisibility(View.VISIBLE);
                        nameText.setVisibility(View.VISIBLE);
                        nameText.setText(message.getSender().getNickname());
                        ImageUtils.displayRoundImageFromUrl(parent.getContext(),
                                message.getSender().getProfileUrl(), profileImage);
                    }

                }
                messageText.setText(message.getMessage());
                timeText.setText(DateUtils.formatTime(message.getCreatedAt()));
            }

        }
    }

    private class MyImageFileMessageHolder extends RecyclerView.ViewHolder {
        TextView timeText, readReceiptText, dateText;
        ImageView fileThumbnailImage;

        CircleProgressBar circleProgressBar;

        public MyImageFileMessageHolder(View itemView) {
            super(itemView);
            timeText = (TextView) itemView.findViewById(R.id.text_group_chat_time);
            fileThumbnailImage = (ImageView) itemView.findViewById(R.id.image_group_chat_file_thumbnail);
            circleProgressBar = itemView.findViewById(R.id.circleprogress);

        }

        void bind(final FileMessage message, final AdapterView.OnItemClickListener listener) {
            if (message != null && message.getUrl() != null) {
                if (isTempMessage(message)) {
                    circleProgressBar.setVisibility(View.VISIBLE);
                    mFileMessageMap.put(message, circleProgressBar);
                    ImageUtils.displayImagefromUri(parent.getContext(), mTempFileMessageUriTable.get(message.getRequestId()), fileThumbnailImage);
                } else {
                    mFileMessageMap.remove(message);
                    circleProgressBar.setVisibility(View.GONE);
                    if (!CommonUtils.getInstance().checkFileExitOrNot(message.getName())) {
                        CommonUtils.getInstance().downloadFile(parent.getContext(), message.getUrl(), message.getName());
                    } else {
                        ImageUtils.displayImagefromUri(parent.getContext(), Uri.fromFile(CommonUtils.getInstance().getExternalFilesPath(message.getName())), fileThumbnailImage);
                    }
                }
                timeText.setText(DateUtils.formatTime(message.getCreatedAt()));

            }

            fileThumbnailImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImage(message.getName(), fileThumbnailImage);
                }
            });
        }
    }

    private class OtherImageFileMessageHolder extends RecyclerView.ViewHolder {
        TextView timeText, readReceiptText, dateText, username;
        ImageView fileThumbnailImage, downloadfile;
        FrameLayout downloadimageFrame;
        CircleProgressBar circleProgressBar;
        Bundle bundle = new Bundle();
        View gridView;
        CardView cardView;

        public OtherImageFileMessageHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.text_group_chat_nickname);
            timeText = (TextView) itemView.findViewById(R.id.text_group_chat_time);
            fileThumbnailImage = (ImageView) itemView.findViewById(R.id.image_group_chat_file_thumbnail);
            downloadimageFrame = itemView.findViewById(R.id.image_download_image);
            circleProgressBar = itemView.findViewById(R.id.download_progress_bar);
            downloadfile = itemView.findViewById(R.id.iv_download_file);
            gridView = itemView.findViewById(R.id.gridview);
            cardView = itemView.findViewById(R.id.card_group_chat_message);
        }

        void bind(int position, final FileMessage message, final AdapterView.OnItemClickListener listener) {
            {
                if (message != null && message.getUrl() != null) {
                    if (!isGridView) {
                        gridView.setVisibility(View.GONE);
                        cardView.setVisibility(View.VISIBLE);
                        if (CommonUtils.getInstance().checkFileExitOrNot(message.getName())) {
                            downloadimageFrame.setVisibility(View.GONE);
                            ImageUtils.displayImagefromUri(parent.getContext(), Uri.fromFile(CommonUtils.getInstance().getExternalFilesPath(message.getName())), fileThumbnailImage);
                        } else {
                            if (!mFileDownloadingMap.containsKey(message.getUrl())) {
                                downloadfile.setVisibility(View.VISIBLE);
                                downloadimageFrame.setVisibility(View.VISIBLE);
                                circleProgressBar.setVisibility(View.GONE);
                            } else {
                                downloadfile.setVisibility(View.GONE);
                                downloadimageFrame.setVisibility(View.GONE);
                                circleProgressBar.setVisibility(View.VISIBLE);
                            }
                            ImageUtils.displayThumbnailimages(parent.getContext(), message.getUrl(), fileThumbnailImage);
                        }
                        downloadfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadfile.setVisibility(View.GONE);
                                circleProgressBar.setVisibility(View.VISIBLE);
                                bundle.putString(StringConstants.FILENAME, message.getName());
                                bundle.putString(StringConstants.DOWNLOAD_URL, message.getUrl());
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DownloadHelpher.getInstance().startDownload(bundle);
                                    }
                                }).start();

                                mFileDownloadingMap.put(message.getUrl(), circleProgressBar);
                            }
                        });
                        username.setText(message.getSender().getNickname());
                        timeText.setText(DateUtils.formatTime(message.getCreatedAt()));

                    } else {
                        gridView.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.GONE);
                    }
                }
                fileThumbnailImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImage(message.getName(), fileThumbnailImage);
                    }
                });


            }
        }
    }

    private class OtherImageFileGridMessageHolder extends RecyclerView.ViewHolder {
        GridView gridView;
        GridAdapter gridAdapter;
        ImageView topleft, topRight, bottomleft, bottomright;
        TextView settotal_images_left;

        public OtherImageFileGridMessageHolder(View itemView) {
            super(itemView);
            topleft = itemView.findViewById(R.id.topLeft);
            topRight = itemView.findViewById(R.id.topRight);
            bottomleft = itemView.findViewById(R.id.bottomLeft);
            bottomright = itemView.findViewById(R.id.bottomRight);
            settotal_images_left = itemView.findViewById(R.id.tv_total_images);
        }

        void bind(final List<FileMessage> fileMessageList) {
            if (fileMessageList.size() > 4) {
                settotal_images_left.setText("+ " + String.valueOf((fileMessageList.size() - 4)));
                settotal_images_left.setVisibility(View.VISIBLE);
                bottomright.setAlpha(0.4f);
            } else {
                settotal_images_left.setVisibility(View.GONE);
                bottomright.setAlpha(1f);
            }
            final ArrayList<String> listUrls = new ArrayList<>();
            if (fileMessageList != null && fileMessageList.size() >= 4)
                for (int i = 0; i < fileMessageList.size(); i++) {
                    listUrls.add(fileMessageList.get(i).getUrl());
                    if (i == 0)
                        ImageUtils.displayImageFromUrl(parent.getContext(), fileMessageList.get(i).getUrl(), topleft);
                    if (i == 1)
                        ImageUtils.displayImageFromUrl(parent.getContext(), fileMessageList.get(i).getUrl(), topRight);
                    if (i == 2)
                        ImageUtils.displayImageFromUrl(parent.getContext(), fileMessageList.get(i).getUrl(), bottomleft);
                    if (i == 3 && fileMessageList.size() < 4)
                        ImageUtils.displayImageFromUrl(parent.getContext(), fileMessageList.get(i).getUrl(), bottomright);
                }

            topleft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.getContext().startActivity(new Intent(parent.getContext(), MultipleImageViewerActivity.class).putStringArrayListExtra("list", listUrls).putExtra("focus", 0));
                }
            });
            topRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.getContext().startActivity(new Intent(parent.getContext(), MultipleImageViewerActivity.class).putStringArrayListExtra("list", listUrls).putExtra("focus", 1));
                }
            });
            bottomleft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.getContext().startActivity(new Intent(parent.getContext(), MultipleImageViewerActivity.class).putStringArrayListExtra("list", listUrls).putExtra("focus", 2));
                }
            });
            if (!(fileMessageList.size() > 4)) {
                bottomright.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parent.getContext().startActivity(new Intent(parent.getContext(), MultipleImageViewerActivity.class).putStringArrayListExtra("list", listUrls).putExtra("focus", 3));
                    }
                });
            }
            settotal_images_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.getContext().startActivity(new Intent(parent.getContext(), MultipleImageViewerActivity.class).putStringArrayListExtra("list", listUrls).putExtra("focus", 0));
                }
            });

        }
    }

    private class MeImageFileGridMessageHolder extends RecyclerView.ViewHolder {
        GridView gridView;
        GridAdapter gridAdapter;
        ImageView topleft, topRight, bottomleft, bottomright;
        TextView settotal_images_left;

        public MeImageFileGridMessageHolder(View itemView) {
            super(itemView);
            topleft = itemView.findViewById(R.id.topLeft);
            topRight = itemView.findViewById(R.id.topRight);
            bottomleft = itemView.findViewById(R.id.bottomLeft);
            bottomright = itemView.findViewById(R.id.bottomRight);
            settotal_images_left = itemView.findViewById(R.id.tv_total_images);
        }

        void bind(final List<FileMessage> fileMessageList) {
            if (fileMessageList.size() > 4) {
                settotal_images_left.setText("+ " + String.valueOf((fileMessageList.size() - 4)));
                settotal_images_left.setVisibility(View.VISIBLE);
                bottomright.setAlpha(0.4f);
            } else {
                settotal_images_left.setVisibility(View.GONE);
                bottomright.setAlpha(1f);
            }
            final ArrayList<String> listUrls = new ArrayList<>();
            if (fileMessageList != null && fileMessageList.size() >= 4)
                for (int i = 0; i < fileMessageList.size(); i++) {
                    listUrls.add(fileMessageList.get(i).getUrl());
                    if (i == 0)
                        ImageUtils.displayImageFromUrl(parent.getContext(), fileMessageList.get(i).getUrl(), topleft);
                    if (i == 1)
                        ImageUtils.displayImageFromUrl(parent.getContext(), fileMessageList.get(i).getUrl(), topRight);
                    if (i == 2)
                        ImageUtils.displayImageFromUrl(parent.getContext(), fileMessageList.get(i).getUrl(), bottomleft);
                    if (i == 3)
                        ImageUtils.displayImageFromUrl(parent.getContext(), fileMessageList.get(i).getUrl(), bottomright);
                }

            topleft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.getContext().startActivity(new Intent(parent.getContext(), MultipleImageViewerActivity.class).putStringArrayListExtra("list", listUrls).putExtra("focus", 0));
                }
            });
            topRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.getContext().startActivity(new Intent(parent.getContext(), MultipleImageViewerActivity.class).putStringArrayListExtra("list", listUrls).putExtra("focus", 1));
                }
            });
            bottomleft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.getContext().startActivity(new Intent(parent.getContext(), MultipleImageViewerActivity.class).putStringArrayListExtra("list", listUrls).putExtra("focus", 2));
                }
            });
            if (!(fileMessageList.size() > 4)) {
                bottomright.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parent.getContext().startActivity(new Intent(parent.getContext(), MultipleImageViewerActivity.class).putStringArrayListExtra("list", listUrls).putExtra("focus", 3));
                    }
                });
            }
            settotal_images_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.getContext().startActivity(new Intent(parent.getContext(), MultipleImageViewerActivity.class).putStringArrayListExtra("list", listUrls).putExtra("focus", 0));
                }
            });

        }
    }

    public void loadPreviousMessages(List<BaseMessage> MessageList) {
        if (mMessageList != null && mMessageList.size() > 0) {
            if (MessageList.size() - mMessageList.size() > 0) {
                MessageList = MessageList.subList(0, MessageList.size() - mMessageList.size());
                Collections.reverse(MessageList);
                for (int i = 0; i < MessageList.size(); i++) {
                    if (MessageList.get(i) instanceof UserMessage)
                        appendMessage((UserMessage) MessageList.get(i));
                    else if (MessageList.get(i) instanceof FileMessage)
                        markMessageSent(MessageList.get(i));
                }
            }
            return;
        }
        createNewList(MessageList);
        /* mMessageList.addAll(MessageList);*/
        notifyDataSetChanged();
    }

    public void appendMessage(UserMessage message) {
        mMessageList.add(0, message);
        customBaseMessage = new CustomBaseMessage();
        customBaseMessage.baseMessage = message;
        customBaseMessagesList.set(0, customBaseMessage);
        notifyDataSetChanged();
    }

    public void sendMessage(final String message) {
        mChannel.sendUserMessage(message, new BaseChannel.SendUserMessageHandler() {
            @Override
            public void onSent(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    Toast.makeText(parent.getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }
                DatabaseHelpher.getInstance().insertUserMessage(userMessage);
                mMessageList.add(0, userMessage);
                customBaseMessage = new CustomBaseMessage();
                customBaseMessage.setBaseMessage(userMessage);
                customBaseMessagesList.add(0, customBaseMessage);
                notifyDataSetChanged();
            }
        });
    }

    public void markMessageSent(BaseMessage message) {
        mTempFileMessageUriTable.remove(message);
//        mMessageList.remove(0);

        //       mMessageList.add(0, message);
        customBaseMessagesList.remove(0);
        customBaseMessage = new CustomBaseMessage();
        customBaseMessage.setBaseMessage(message);
        customBaseMessagesList.add(0, customBaseMessage);

        notifyDataSetChanged();
    }

    public Uri getTempFileMessageUri(BaseMessage message) {
        if (!isTempMessage(message)) {
            return null;
        }

        if (!(message instanceof FileMessage)) {
            return null;
        }

        return mTempFileMessageUriTable.get(((FileMessage) message).getRequestId());
    }

    public boolean isTempMessage(BaseMessage message) {
        return message.getMessageId() == 0;
    }

    public void addTempFileMessageInfo(FileMessage message, Uri uri) {
        mTempFileMessageUriTable.put(message.getRequestId(), uri);
    }

    public void addFirst(BaseMessage message) {
        //mMessageList.add(0, message);
        customBaseMessage = new CustomBaseMessage();
        customBaseMessage.setBaseMessage(message);
        customBaseMessagesList.add(0, customBaseMessage);
        notifyDataSetChanged();
    }

    public void setFileProgressPercent(FileMessage message, int percent) {
        BaseMessage msg;
        for (int i = mMessageList.size() - 1; i >= 0; i--) {
            msg = mMessageList.get(i);
            if (msg instanceof FileMessage) {
                if (message.getRequestId().equals(((FileMessage) msg).getRequestId())) {
                    CircleProgressBar circleProgressBar = mFileMessageMap.get(message);
                    if (circleProgressBar != null) {
                        circleProgressBar.setProgress(percent);
                    }
                    break;
                }
            }
        }
    }

    public void setFileDownloadProgress(String url, int percent) {
        if (url != null) {
            CircleProgressBar circleProgressBar = mFileDownloadingMap.get(url);
            if (circleProgressBar != null) {
                circleProgressBar.setProgress(percent);
            }
        }
    }

    public void removeFileDownloaded(String url) {
        if (url != null) {
            mFileDownloadingMap.remove(url);
            notifyDataSetChanged();
        }
    }


    //For Grid View

    List<CustomBaseMessage> customBaseMessagesList;
    boolean flag = true;
    CustomBaseMessage customBaseMessage;
    List<FileMessage> fileMessageList;
    int count = 0;
    List<Integer> positionsList = new ArrayList<>();
    List<BaseMessage> messagelist = new ArrayList<>();
    String userId;

    public void createNewList(final List<BaseMessage> newmessagelist) {
        if (customBaseMessagesList != null)
            customBaseMessagesList.clear();
        newmessagelist.size();
        new Thread(new Runnable() {
            @Override
            public void run() {
                messagelist.addAll(newmessagelist);
                customBaseMessagesList = new ArrayList<>();
                for (int i = 0; i < messagelist.size(); i++) {
                    customBaseMessage = new CustomBaseMessage();
                    if (messagelist.get(i) instanceof FileMessage) {
                        userId = ((FileMessage) messagelist.get(i)).getSender().getUserId();
                        flag = true;
                        fileMessageList = new ArrayList<>();
                        while (flag) {
                            if (messagelist.get(i + count) instanceof FileMessage && userId.equalsIgnoreCase(((FileMessage) messagelist.get(i + count)).getSender().getUserId())) {
                                FileMessage nextfileMessage = (FileMessage) messagelist.get(i + count);
                                positionsList.add(i + count);
                                fileMessageList.add(nextfileMessage);
                                flag = true;
                                count++;
                            } else {
                                flag = false;
                                if (count > 3) {
                                    customBaseMessage = new CustomBaseMessage();
                                    customBaseMessage.setFileMessageList(fileMessageList);
                                    customBaseMessagesList.add(customBaseMessage);
                                    i = i + (count - 1);
                                } else {
                                    customBaseMessage.setBaseMessage(messagelist.get(i));
                                    customBaseMessagesList.add(customBaseMessage);
                                }
                                positionsList.clear();
                                customBaseMessage = null;
                                fileMessageList = null;
                                count = 0;
                                userId = "";
                            }
                        }

                    } else {
                        customBaseMessage.setBaseMessage(messagelist.get(i));
                        customBaseMessagesList.add(customBaseMessage);
                    }
                }
                messagelist.clear();
                customBaseMessagesList.size();
            }
        }).start();

    }


    class CustomBaseMessage {

        BaseMessage baseMessage;
        List<FileMessage> fileMessageList;

        public BaseMessage getBaseMessage() {
            return baseMessage;
        }

        public void setBaseMessage(BaseMessage baseMessage) {
            this.baseMessage = baseMessage;
        }

        public List<FileMessage> getFileMessageList() {
            return fileMessageList;
        }

        public void setFileMessageList(List<FileMessage> fileMessageList) {
            this.fileMessageList = fileMessageList;
        }
    }

    FragmentManager fragmentManager;

    public void showImage(String urlpath, ImageView imageView) {
        customImageViewer.setData(parent.getContext(), urlpath,true);
        if (fragmentManager == null)
            fragmentManager = ((ChatScreenActivity) parent.getContext()).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addSharedElement(imageView, ViewCompat.getTransitionName(imageView))
                .addToBackStack(null)
                .commit();

        customImageViewer.show(fragmentManager, "");

    }


}
