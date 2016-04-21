package com.zaze.commons.download;

/**
 * Description : 下载管理类
 * date : 2016-03-31 - 20:26
 *
 * @author : zaze
 * @version : 1.0
 */
public class DownloadManager {
//    private DownloadModel downloadModel;
    private static DownloadManager manager;
    
    public static DownloadManager getInstance() {
        if(manager == null) {
            manager = new DownloadManager();
        }
        return manager;
    }
    private DownloadManager() {
//        downloadModel = PubModelFactory.newDownloadModel();
    }
    
    // ----------------------------------------------------------
    public int download(DownloadFace downloadFace, TDownload tDownload, boolean isTempFile) {
        int code = downloadFace.checkFile(tDownload, isTempFile);
        switch (code) {
            case DownloadFace.Code.FILE_OK :
                break;
            case DownloadFace.Code.FILE_NEED_INSTALL :
                downloadFace.toInstallDownFile(tDownload);
                break;
            case DownloadFace.Code.PARAMS_ERROR:
                break;
            default:
                downloadFace.download(tDownload);
                break;
            
        }
        return code;
    }
}
