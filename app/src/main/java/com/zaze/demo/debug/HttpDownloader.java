package com.zaze.demo.debug;

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

public class HttpDownloader {

    public interface DownloadCallback {
        void preDownload(long totalCount);

        void onProgress(long totalCount, long currentCount);

        void onSuccess();

        void onError();
    }

    /**
     * 根据URL得到输入流
     *
     * @param urlStr
     * @return InputStream
     * @throws IOException
     */
    public static InputStream getInputStreamFromURL(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.connect();
        return urlConn.getInputStream();
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
    public static void downFile(String urlStr, String filePath) {
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
            inputStream = urlConn.getInputStream();
            // TODO: 2017/8/26
            FileUtil.writeToFile(new File(filePath), inputStream, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        InputStream inputStream = null;
        try {
            ZLog.i(ZTag.TAG_DEBUG, "url : " + urlStr);
            String tempFilePath = filePath + ".zz";
            URL url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
            final int contentLength = urlConn.getContentLength();
            if (callback != null) {
                callback.preDownload(contentLength);
            }
            inputStream = urlConn.getInputStream();
//            FileUtil.writeToFile(inputStream, tempFilePath, new ZCallback<Long>() {
//                @Override
//                public void onNext(Long dataSize) {
//                    if (callback != null) {
//                        callback.onProgress(contentLength, dataSize);
//                    }
//                }
//
//                @Override
//                public void onCompleted() {
//                }
//            });
            if (callback != null) {
                File file = new File(tempFilePath);
                if (file.exists()) {
                    file.renameTo(new File(filePath));
                }
                callback.onSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onError();
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 并没有创建文件,只是单纯的读取了文本的内容存在了StringBuffer中
     * 根据URL下载文件,前提是这个文件是文本,函数返回值就是这个文件当中的内容
     *
     * @param urlStr
     * @return 文本String
     */

    public static String download(String urlStr) {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        String line = null;
        try {
            //创建一个URL对象
            URL url = new URL(urlStr);
            //创建一个http连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            //使用IO流读取数据
            br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "gb2312"));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
