package com.zaze.aarrepo.commons.download.impl;


import android.support.annotation.NonNull;

import com.zaze.aarrepo.commons.download.DownCallback;
import com.zaze.aarrepo.commons.download.DownloadFace;
import com.zaze.aarrepo.commons.download.DownloadHelper;
import com.zaze.aarrepo.commons.download.DownloadUtil;
import com.zaze.aarrepo.commons.download.TDownload;
import com.zaze.aarrepo.commons.log.LogKit;
import com.zaze.aarrepo.utils.FileUtil;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.aarrepo.utils.helper.ConfigHelper;

import java.io.File;
import java.util.Properties;

/**
 * Description :
 * date : 2016-04-01 - 14:22
 *
 * @author : zaze
 * @version : 1.0
 */
public abstract class BaseDownload implements DownloadFace {
//    private DownloadModel downloadModel;
    private static final String INSTALL_PATH_KEY = "install_path_";
    private ConfigHelper configHelper;
    protected TDownload download;
    private DownCallback callback;

    public BaseDownload(DownCallback callback) {
        configHelper = ConfigHelper.newInstance(null);
//        downloadModel = PubModelFactory.newDownloadModel();
        this.callback = callback;
    }

    @Override
    public int download(TDownload download) {
        this.download = download;
        download.setDown_state(DownState.DOWNLOADING);
        download.setDown_time(String.valueOf(System.currentTimeMillis()));
        saveDownInfo(download);
        DownloadUtil.downloadByCurlHttpDownload(download.getDown_url(), download.getSave_path(), this);
        return 0;
    }

    @Override
    public int checkFile(TDownload download, boolean isTempFile) {
        if (download == null) {
            LogKit.w("Download checkFile : 参数错误");
            return Code.PARAMS_ERROR;
        }
        download.setInstall_path(getInstallPath(download.getDown_id()));
        int downCode = checkDownFile(download);
        switch (downCode) {
            case Code.PARAMS_ERROR:
                LogKit.w("Download checkFile : 参数错误");
                break;
            case Code.FILE_VERSION_ERROR:
                downCode = Code.FILE_NEED_DOWN;
                break;
            case Code.FILE_OK:
                if (isTempFile) {
                    downCode = Code.FILE_NEED_INSTALL;
                }
                break;
            case Code.FILE_NOT_FOUNT:
                if (isTempFile) {  // 下载文件是临时文件, 检查安装文件
                    downCode = checkInstallFile(download);
                    break;
                } else {
                    downCode = Code.FILE_NEED_DOWN;
                }
                break;
        }
        return downCode;
    }

    @Override
    public int checkDownFile(TDownload download) {
        if (download == null || StringUtil.isEmpty(download.getSave_path())
                || download.getDown_type() != getType()
                || StringUtil.isEmpty(download.getDown_url())) {
            LogKit.w("Download checkDownFile : 参数错误");
            return Code.PARAMS_ERROR;
        }
        // 检查历史版本记录， 不存在或者不匹配, 直接重新下载
        String historyVersion = getLocalVersion(download.getDown_id());
        if (historyVersion == null) {
            LogKit.w("Download checkDownFile : 不存在历史版本记录");
            download.setDown_state(DownState.NEED_DOWNLOAD);
            saveDownInfo(download);
            return Code.FILE_VERSION_ERROR;
        }
        if (!historyVersion.equalsIgnoreCase(download.getVersion())) {
            LogKit.w("Download checkDownFile : 版本不匹配 historyVersion=" + historyVersion
                    + "; curVersion=" + download.getVersion());
            download.setDown_state(DownState.NEED_UPDATE);
            saveDownInfo(download);
            return Code.FILE_VERSION_ERROR;
        }
        // -------------  检查下载文件是否可用
        if (FileUtil.isFileExist(download.getSave_path())) {
            return checkFileEnable(download);
        } else {
            return Code.FILE_NOT_FOUNT;
        }
    }

    @Override
    public int checkInstallFile(@NonNull TDownload download) {
        if (FileUtil.isFileExist(download.getInstall_path())) {
            return Code.FILE_OK;
        } else {
            LogKit.w("Download checkFile : 安装或解压文件不存在 需要下载!" + download.getInstall_path());
            download.setDown_state(DownState.NEED_DOWNLOAD);
            saveDownInfo(download);
            return Code.FILE_NOT_FOUNT;
        }
    }

    private int checkFileEnable(@NonNull TDownload download) {
//        saveLocalVersion(download.getDown_id(), download.getVersion());
        download.setDown_state(DownState.FINISH_DOWNLOAD);
        saveDownInfo(download);
        return Code.FILE_OK;
    }

    //
//    @Override
//    public void onStart(int i, int i1) {
//        if (callback != null) {
//            callback.onStart(i, i1);
//        }
//    }
//
//    @Override
//    public void onLoading(int i, int i1) {
//        if (callback != null) {
//            callback.onLoading(i, i1);
//        }
//    }
//
//    @Override
//    public void onSuccess(int i, String s, String s1) {
//        if (callback != null) {
//            callback.onDownloadSuccess(i, s, s1);
//        }
//        toInstallDownFile(download);
//        saveLocalVersion(download.getDown_id(), download.getVersion());
//        download.setDown_state(DownState.FINISH_DOWNLOAD);
//        saveDownInfo(download);
//
//    }
//
//    @Override
//    public void onFailure(int i, String s, int i1) {
//        if (callback != null) {
//            callback.onDownloadFailure(i, s, i1);
//        }
//    }

    public void onInstallFinish(int id, int code, String installPath, String tempFile) {
        if (!StringUtil.isEmpty(tempFile)) {
            File file = new File(tempFile);
            boolean bool = file.delete();
            LogKit.v("bool : " + bool);
        }
        if (callback != null) {
            callback.onInstallFinish(id, code, installPath);
        }
    }

    //
    @Override
    public String getLocalVersion(long id) {
        return DownloadHelper.getInstance().getLocalVersion(getType(), id);
    }

    @Override
    public void saveLocalVersion(long id, String version) {
        DownloadHelper.getInstance().saveLocalVersion(getType(), id, version);
    }

    @Override
    public String getInstallPath(long id) {
        Properties properties = configHelper.load();
        Object object = properties.get(INSTALL_PATH_KEY + getType() + id);
        if (object instanceof String) {
            return StringUtil.parseString((String) object, null);
        } else {
            return null;
        }
    }

    @Override
    public void saveInstallPath(long id, String path) {
        configHelper.store(INSTALL_PATH_KEY + getType() + id, StringUtil.parseString(path, ""));
    }

    // ---------------------------------
    public TDownload getHistoryInfo(long id) {
//        return downloadModel.getDownloadInfo(getType(), id);
        return null;
    }

    public void saveDownInfo(TDownload download) {
//        downloadModel.saveDownloadInfo(download);
    }
}
