package com.chatapp.Utils;

public interface IntegerConstant {

    int REGISTRATION_API_CALL = 1;
    int LOGIN_API_CALL = 2;
    int DIRECT_MESSAGE_CHANNEL_API_CALL = 3;
    int GROUP_MESSAGE_CHANNEL_API_CALL = 4;
    int USER_LIST_API_CALL = 5;

    //fragments
    int CHAT_FRAGMENT = 0;
    int GROUP_FRAGMENT = 1;
    int MORE_FRAGMENT = 2;
    int CREATE_GROUP = 1;
    int START_CHAT = 0;
    int GROUP_INFO = 3;
    int ADD_USERS = 4;
    //splash timeout
    int SPLASH_TIME_OUT = 2000;

    int VIEW_TYPE_USER_MESSAGE_ME = 10;
    int VIEW_TYPE_USER_MESSAGE_OTHER = 11;
    int VIEW_TYPE_FILE_MESSAGE_IMAGE_ME = 22;
    int VIEW_TYPE_FILE_MESSAGE_IMAGE_OTHER = 23;
    int CHANNEL_LIST_LIMIT = 15;
    int VIEW_TYPE_FILE_MESSAGE_IMAGE_GRID_OTHER = 33;
    int VIEW_TYPE_FILE_MESSAGE_IMAGE_GRID_ME = 34;

    int PERMISSION_WRITE_EXTERNAL_STORAGE = 13;
    int INTENT_REQUEST_CHOOSE_MEDIA = 301;

    int EDIT_STATUS = 101;
    int EDIT_USERNAME = 102;
    int USER = 201;
    int GROUP = 202;
    int CURRENT_USER = 203;

}
