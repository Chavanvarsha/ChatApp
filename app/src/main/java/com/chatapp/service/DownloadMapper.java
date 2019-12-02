package com.chatapp.service;

import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.Map;

public class DownloadMapper {

    private static DownloadMapper downloadMapper;
    private Map<String, String> downloadMap = new HashMap<>();

    public static DownloadMapper getInstance() {
        if (downloadMapper == null)
            downloadMapper = new DownloadMapper();
        return downloadMapper;
    }

    public void addDownloadingFile(String filename, String downloadurl) {
        if (downloadMap != null)
            downloadMap.put(filename, downloadurl);
    }

    public void removeFromMap(String filename) {
        if (downloadMap != null)
            downloadMap.remove(filename);
    }

    public boolean checkFileIsDownloadingOrNot(String filename) {
        if (downloadMap != null) {
            for (String data : downloadMap.values()) {
                if (data.equals(filename)) {
                    return true;
                }
            }
        }
        return false;
    }


}
