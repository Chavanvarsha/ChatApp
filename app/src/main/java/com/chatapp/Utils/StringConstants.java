package com.chatapp.Utils;

public interface StringConstants {

    //Send-Bird AppId and Token
    String APP_ID = "9B5C3312-A411-4633-955F-EA6D085F83D0";
    String APP_TOKEN = "80028ea41abeb4cbb3aa7a35c54ff409f3762c64";

    String BUNDLE_ID = "bundle_id_service";
    String FILENAME="filename";
    String DOWNLOAD_URL="downloadurl";

    String TYPE = "type";
    String ACTIVITY_TYPE="activity_type";
    String SUCCESS = "success";
    String ERROR = "error";

    //Firebase Utils
    String USERS = "users";

    //preference string
    String PREFERENCE_KEY_USER_ID = "userId";
    String PREFERENCE_KEY_USER_NICKNAME = "userNickName";
    String PREFERENCE_KEY_REMEMBER_ME = "remember_me";
    String PREFERENCE_KEY_EMAIL_ID = "email_id";
    String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_GROUP_CHAT";
    String CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_GROUP_CHANNEL_CHAT";
    String CHANNEL_USER_HANDLER_ID = "CHANNEL_HANDLER_USER";


    //Data Caching
    String channel_list = "channel_list";

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    //intent
    String GROUP_CHANNEL_OBJ = "groupchannel";
    String STATUS = "status";



}
