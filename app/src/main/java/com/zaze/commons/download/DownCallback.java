package com.zaze.commons.download;

/**
 * Description :
 * date : 2016-04-14 - 13:46
 *
 * @author : zaze
 * @version : 1.0
 */
public interface DownCallback {
    void onStart(int taskId, int var2);

    void onLoading(int taskId, int var2);

    void onDownloadSuccess(int taskId, String var2, String var3);

    void onDownloadFailure(int taskId, String var2, int var3);
    
    void onInstallFinish(int id, int code, Object obj);
}
