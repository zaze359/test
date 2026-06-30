package com.zaze.core.network.http;

import com.zaze.utils.FileUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HTTP下载工具类，提供从URL下载文件和文本内容的功能
 */
public class HttpDownloader {

    /**
     * 下载回调接口
     */
    public interface DownloadCallback {
        /**
         * 下载前回调，返回文件总大小
         */
        void preDownload(long totalCount);

        /**
         * 下载进度回调
         */
        void onProgress(long totalCount, long currentCount);

        /**
         * 下载成功回调
         */
        void onSuccess();

        /**
         * 下载失败回调
         */
        void onError();
    }

    /**
     * 根据URL获取输入流
     *
     * @param urlStr URL字符串
     * @return 输入流，调用方负责关闭
     * @throws IOException IO异常
     */
    public static InputStream getInputStreamFromURL(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        try {
            urlConn.connect();
            return urlConn.getInputStream();
        } catch (IOException e) {
            // 异常时主动断开连接，避免资源泄漏
            urlConn.disconnect();
            throw e;
        }
    }


    /**
     * 从URL下载文件到指定路径
     *
     * @param urlStr   下载URL
     * @param filePath 目标文件路径
     */
    public static void downFile(String urlStr, String filePath) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            try {
                urlConn.connect();
                // 使用 try-with-resources 自动关闭输入流
                try (InputStream inputStream = urlConn.getInputStream()) {
                    FileUtil.writeToFile(new File(filePath), inputStream, true);
                }
            } finally {
                // 确保连接被正确断开
                urlConn.disconnect();
            }
        } catch (Exception e) {
            ZLog.e(ZTag.TAG_DEBUG, "downFile error", e);
        }
    }

    /**
     * -1：代表文件出错
     * 0：表示下载成功
     * 1：表示已经存在
     *
     * @param urlStr
     * @param filePath
     * @return File
     */
    public static void downFile(String urlStr, String filePath, final DownloadCallback callback) {
        try {
            ZLog.i(ZTag.TAG_DEBUG, "url : " + urlStr);
            String tempFilePath = filePath + ".zz";
            URL url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            try {
                urlConn.connect();
                final int contentLength = urlConn.getContentLength();
                if (callback != null) {
                    callback.preDownload(contentLength);
                }
                try (InputStream inputStream = urlConn.getInputStream()) {
                    FileUtil.writeToFile(new File(tempFilePath), inputStream, true);
                }
                if (callback != null) {
                    File file = new File(tempFilePath);
                    if (file.exists()) {
                        file.renameTo(new File(filePath));
                    }
                    callback.onSuccess();
                }
            } finally {
                urlConn.disconnect();
            }
        } catch (Exception e) {
            ZLog.e(ZTag.TAG_DEBUG, "downFile error", e);
            if (callback != null) {
                callback.onError();
            }
        }
    }

    /**
     * 从URL下载文本内容（不创建文件，仅读取到内存中）
     * 注意：此方法假设目标URL返回的是文本内容，编码为GB2312
     *
     * @param urlStr 下载URL
     * @return 文本内容
     */
    public static String download(String urlStr) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            try {
                urlConn.connect();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "gb2312"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                }
            } finally {
                urlConn.disconnect();
            }
        } catch (Exception e) {
            ZLog.e(ZTag.TAG_DEBUG, "download error", e);
        }
        return sb.toString();
    }

}
