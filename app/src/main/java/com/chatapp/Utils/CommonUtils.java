package com.chatapp.Utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.chatapp.Callbacks.ResponseCallback;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.model.CustomGroupChannel;
import com.chatapp.model.ObjectWrapperForBinder;
import com.chatapp.service.DownloadService;
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.User;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommonUtils {
    private static CommonUtils commonUtils;
    public List<User> usersList;
    Bundle bundle;
    public static File extStore = BaseApplication.getInstance.getExternalFilesDir("/File");

    private CommonUtils() {

    }

    public static CommonUtils getInstance() {
        if (commonUtils == null)
            commonUtils = new CommonUtils();
        return commonUtils;
    }

    public boolean checkContextExistOrNot(Context context) {
        if (context != null && context instanceof Activity) {
            if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                Log.i("contextChecked", "context is exist and instance of activity");
                return true;
            } else {
                Log.i("contextChecked", "context is not exist and instance of activity");
                return false;
            }
        } else {
            if (context != null) {
                Log.i("contextChecked", "context is exist and not a instance of activity");
                return true;
            } else {
                Log.i("contextChecked", "context is null");
                return false;
            }
        }
    }

    public void updateNetworkConnection(final Context context, final ResponseCallback<Boolean> responseCallback) {

        ReactiveNetwork
                .observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Log.d("TAG", "" + aBoolean);
                        BaseApplication.isConnectedtoNetwork = aBoolean;
                        if (PreferenceUtils.getInstance().getRememberme() && aBoolean) {
                            ClientFactory.getConnectionManagerClientInstance().connect(PreferenceUtils.getInstance().getUserId(), null);
                            ClientFactory.getUserListClientInstance().getGroupListApiCall(BaseApplication.getInstance, new ResponseCallback<List<GroupChannel>>() {
                                @Override
                                public void responseCallback(List<GroupChannel> data) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static boolean isEmailIdValid(String email) {
        Pattern pat = Pattern.compile(StringConstants.emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
        Drawable drawable = ContextCompat.getDrawable(context, icon).mutate();
        drawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return drawable;
    }

    public int getUSerId(GroupChannel groupChannel) {
        if (groupChannel != null) {
            for (int i = 0; i < groupChannel.getMembers().size(); i++) {
                if (!PreferenceUtils.getInstance().getUserId().equals(groupChannel.getMembers().get(i).getUserId())) {
                    return i;
                }
            }
        }
        return 0;
    }

    public int getCurrentUSerId(GroupChannel groupChannel) {
        if (groupChannel != null) {
            for (int i = 0; i < groupChannel.getMembers().size(); i++) {
                if (PreferenceUtils.getInstance().getUserId().equals(groupChannel.getMembers().get(i).getUserId())) {
                    return i;
                }
            }
        }
        return 0;
    }

    public Bundle setBundle(String filename, String downloadurl) {
        if (bundle == null)
            bundle = new Bundle();
        bundle.putString(StringConstants.DOWNLOAD_URL, downloadurl);
        bundle.putString(StringConstants.FILENAME, filename);
        return bundle;
    }

    public void downloadFile(Context context, final String downloadurl, final String filename) {
        if (!checkFileExitOrNot(filename)) {
            Bundle bundle = setBundle(filename, downloadurl);
            context.startService(new Intent(context, DownloadService.class).putExtra(StringConstants.BUNDLE_ID, bundle));
        }

    }

    public boolean checkFileExitOrNot(String filename) {
        File myFile = new File(extStore + "/" + filename);
        if (myFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public File getExternalFilesPath(String filename) {
        return new File(extStore + "/" + filename);
    }


}
