package com.chatapp.service;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.chatapp.Scope.BaseApplication;
import com.chatapp.UI.adapter.MessageAdapter;
import com.chatapp.Utils.CommonUtils;
import com.chatapp.Utils.StringConstants;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;

import java.io.File;
import java.util.ArrayList;

public class DownloadHelpher {

    private static DownloadHelpher downloadHelpher;
    private String filepath;
    private String downloadurl;
    private String filename;
    ArrayList<String> strings = new ArrayList<>();

    private DownloadHelpher() {
    }

    public static DownloadHelpher getInstance() {
        if (downloadHelpher == null)
            downloadHelpher = new DownloadHelpher();
        return downloadHelpher;
    }


    public void startDownload(Bundle bundle) {
        if (bundle != null) {
            filename = bundle.getString(StringConstants.FILENAME);
            downloadurl = bundle.getString(StringConstants.DOWNLOAD_URL);
            strings.add(downloadurl);
            filepath = new File(CommonUtils.extStore + "/").getAbsolutePath();

        }
        if (downloadurl != null && filepath != null && filename != null) {
            PRDownloader.download(downloadurl, filepath, filename)
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {

                        }
                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {

                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {
                            Log.i("URL", downloadurl);
                            BaseApplication.chatScreenActivity.setDownloadingUrlProgress(strings.get(0), (int) (((float) progress.currentBytes / (float) progress.totalBytes) * 100));
                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            Log.i("complete", downloadurl);
                            strings.remove(0);
                            BaseApplication.chatScreenActivity.removeUrl(downloadurl);
                        }

                        @Override
                        public void onError(Error error) {

                        }
                    });
        }
    }

}
