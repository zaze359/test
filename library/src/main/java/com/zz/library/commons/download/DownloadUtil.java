package com.zz.library.commons.download;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;


/**
 * Description :
 * date : 2016-03-17 - 20:04
 *
 * @author : zaze
 * @version : 1.0
 */
public class DownloadUtil {
    //----------------constant argc
    public static final int FLAG_START = 0;
    public static final int FLAG_DOWNLOADING = 1;
    public static final int FLAG_SUCCESS = 2;
    public static final int FLAG_FAILURE = 3;
    // ---------------------------------------------
    // ---------------------------------------------
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_START:
                    break;
                case FLAG_DOWNLOADING:
                    break;
                case FLAG_SUCCESS:
                    break;
                case FLAG_FAILURE:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public DownloadUtil() {
    }

    /**
     * 下载题库
     *
     * @param downUrl     下载路径
     * @param destPathTmp 目标路径
     * @return 构建下载成功或失败
     */
    public static boolean downloadByCurlHttpDownload(String downUrl, String destPathTmp, Object downloadCallback) {
//        ZTYCurlHttpDownload ztyCurlHttpDownload = createZhiTongYunDownload(downloadCallback);
//        if (ztyCurlHttpDownload != null) {
//            System.out.println("curlHttp -- 开始下载");
//            ztyCurlHttpDownload.downloadFile2SD((int) (System.currentTimeMillis() / 1000), downUrl, destPathTmp);
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }

    /**
     * 创建ZTYCurlHttpDownload
     *
     * @param callback
     * @return
     */
//    private static ZTYCurlHttpDownload createZhiTongYunDownload(DownloadCallback callback) {
//        ZTYCurlHttpDownload ztyCurlHttpDownload;
//        try {
//            ztyCurlHttpDownload = new ZTYCurlHttpDownload(callback);
//        } catch (Error e) {
//            e.printStackTrace();
//            Log.e("so error", "make sure the libztysiface.so is exists!");
//            ztyCurlHttpDownload = null;
//        }
//
//        return ztyCurlHttpDownload;
//    }
}
