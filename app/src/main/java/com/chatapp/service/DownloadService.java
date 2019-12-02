package com.chatapp.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chatapp.Scope.BaseApplication;
import com.chatapp.Utils.StringConstants;

import java.util.Objects;

public class DownloadService extends Service {
    Bundle bundle;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bundle = intent.getBundleExtra(StringConstants.BUNDLE_ID);
        if (bundle != null) {
            String filename = bundle.getString(StringConstants.FILENAME);
            String downloadurl = bundle.getString(StringConstants.DOWNLOAD_URL);
            if (filename != null && downloadurl != null) {
                downloadFile(downloadurl, filename);
            }

        }
        return START_NOT_STICKY;
    }

    public void downloadFile(final String downloadurl, final String filename) {
        if (!DownloadMapper.getInstance().checkFileIsDownloadingOrNot(filename)) {
            DownloadMapper.getInstance().addDownloadingFile(filename, downloadurl);
            DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(downloadurl));
            request1.setVisibleInDownloadsUi(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request1.allowScanningByMediaScanner();
                request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            }
            Log.e("start Download",filename);
            request1.setDestinationInExternalFilesDir(BaseApplication.getInstance, "/File", filename);
            DownloadManager manager1 = (DownloadManager) BaseApplication.getInstance.getSystemService(Context.DOWNLOAD_SERVICE);
            Objects.requireNonNull(manager1).enqueue(request1);
            if (DownloadManager.STATUS_SUCCESSFUL == 8) {
                DownloadMapper.getInstance().removeFromMap(filename);
                return;
            }
            return;
        }
    }

}
