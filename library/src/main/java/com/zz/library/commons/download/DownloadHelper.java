package com.zz.library.commons.download;

import com.zz.library.util.ConfigHelper;
import com.zz.library.util.StringUtil;

import java.util.Properties;

/**
 * Description :
 * date : 2016-04-14 - 16:38
 *
 * @author : zaze
 * @version : 1.0
 */
public class DownloadHelper {
    private static final String VERSION_KEY = "version_";
    private ConfigHelper configHelper;
    private static DownloadHelper downloadHelper;

    public static DownloadHelper getInstance() {
        if(downloadHelper == null) {
            downloadHelper = new DownloadHelper();
        }
        return downloadHelper;
    }
    private DownloadHelper() {
        configHelper = ConfigHelper.newInstance(null);
    }
    // ----------------------------------------------------------
    public int download(DownloadFace downloadFace, TDownload tDownload, boolean isTempFile) {
        return DownloadManager.getInstance().download(downloadFace, tDownload, isTempFile);
    }
    public boolean checkFileEnable(DownloadFace downloadFace, TDownload tDownload, boolean isTempFile) {
        int code = downloadFace.checkFile(tDownload, isTempFile);
        return code == DownloadFace.Code.FILE_OK;
    }
    // ----------------------------------------------------------
    /**
     * @param type
     * @param id
     * @return 本地历史版本
     */
    public String getLocalVersion(int type, long id) {
        Properties properties = configHelper.load();
        Object object = properties.get(VERSION_KEY + type + id);
        if (object instanceof String) {
            return StringUtil.parseString((String) object, null);
        } else {
            return null;
        }
    }
    public void saveLocalVersion(int type, long id, String version) {
        configHelper.store(VERSION_KEY + type + id, StringUtil.parseString(version, ""));
    }
}