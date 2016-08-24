package com.zz.library.commons.download;

import com.zz.library.util.FileUtil;

/**
 * Description :
 * date : 2016-04-01 - 11:15
 * @author : zaze
 * @version : 1.0
 */
public interface DownloadFace {
    interface DownType {
        int BOOK = 11;
    }
    // ---------------------------------------------
    interface DownState {
        int NEED_DOWNLOAD = 101;
        int NEED_UPDATE = 102;
        int DOWNLOADING = 103;
        int FINISH_DOWNLOAD = 104;
    }
    // ---------------------------------------------
    interface SavePath {
        String BOOK_ZIP_PATH = FileUtil.getSDCardRoot() + "download/book/bk_";
    }
    // ---------------------------------------------
    interface Code {
        int FILE_OK = 201;                      // 
        int FILE_ERROR = 202;                   // 错误
        int FILE_NOT_FOUNT = 203;               // 找不到文件
        int FILE_VERSION_ERROR = 204;           // 文件存在但是版本不对
        int PARAMS_ERROR = 210;                 // 传入的参数错误
        //
        int FILE_NEED_INSTALL = 212;            // 需要安装
        int FILE_NEED_DOWN = 213;               // 需要下载
    }
    // ---------------------------------------------

    /**
     * 下载文件 
     * @param download
     * @return
     */
    int download(TDownload download);
    /**
     * 安装 或 接压缩文件
     * @param download
     * @return
     */
    boolean toInstallDownFile(TDownload download);

    int checkFile(TDownload download, boolean isTempFile);
    int checkDownFile(TDownload download);
    int checkInstallFile(TDownload download);

    /**
     * 获取历史版本
     * @param id
     * @return
     */
    String getLocalVersion(long id);

    /**
     * 保存历史版本
     * @param id
     * @param version
     */
    void saveLocalVersion(long id, String version);

    /**
     * 获取安装路径
     * @param id
     * @return
     */
    String getInstallPath(long id);
    /**
     * 保存安装路径
     * @param id
     * @param path
     */
    void saveInstallPath(long id, String path);
    //
    int getType();
}
