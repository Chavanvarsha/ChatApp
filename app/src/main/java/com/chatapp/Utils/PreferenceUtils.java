package com.chatapp.Utils;

public class PreferenceUtils {

    private static PreferenceUtils preferenceUtils;

    private PreferenceUtils() {

    }

    public static PreferenceUtils getInstance() {
        if (preferenceUtils == null)
            preferenceUtils = new PreferenceUtils();
        return preferenceUtils;
    }

    public void setUserId(String userId) {
        SharedPref.write(StringConstants.PREFERENCE_KEY_USER_ID, userId);
    }

    public String getUserId() {
        return SharedPref.read(StringConstants.PREFERENCE_KEY_USER_ID);
    }

    public void setUserNickName(String userNickName) {
        SharedPref.write(StringConstants.PREFERENCE_KEY_USER_NICKNAME, userNickName);
    }

    public String getUserNickName() {
        return SharedPref.read(StringConstants.PREFERENCE_KEY_USER_NICKNAME);
    }

    public void setRememberme(boolean isChecked) {
        SharedPref.write(StringConstants.PREFERENCE_KEY_REMEMBER_ME, isChecked);
    }

    public boolean getRememberme() {
        return SharedPref.read(StringConstants.PREFERENCE_KEY_REMEMBER_ME, false);
    }

    public void setUserEmailID(String emailid) {
        SharedPref.write(StringConstants.PREFERENCE_KEY_EMAIL_ID, emailid);
    }

    public String getEmailID() {
        return SharedPref.read(StringConstants.PREFERENCE_KEY_EMAIL_ID);
    }

    public void emptyPreferences(){
        setUserId("");
        setUserEmailID("");
        setRememberme(false);
        setUserNickName("");
    }
}
