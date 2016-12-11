package com.zaze.aarrepo.utils;


import android.os.Environment;
import android.util.Log;

import com.zaze.aarrepo.commons.log.LogKit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Description : 
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class FileUtil {

    private static final boolean showLog = true;
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    private static final int NOT_FILE = -5;         // 不是文件
    private static final int FILE_NOT_EXISTS = -4;  // 文件不存在
    private static final int DIR_EXISTS = -3;       // 目录存在
    private static final int FILE_EXISTS = -2;      // 文件存在
    private static final int CREATE_ERROR = -1;     // 构建失败
    private static final int UNKNOW = 0;            // 
    private static final int CREATE_FILE_SUCCESS = 1;   // 创建文件成功
    private static final int CREATE_DIR_SUCCESS = 2;    // 创建目录成功

    /**
     * @return SD卡根目录
     */
    public static String getSDCardRoot() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }


    /**
     * @param filePath
     * @return
     * @throws IOException
     */
    public static File createFile(String filePath) throws IOException {
        File file = new File(filePath);
        boolean result = false;
        if (!isFileExist(filePath)) {
            if (createParentDir(filePath)) {
                result = file.createNewFile();
            }
        } else {
            result = true;
        }
        if (showLog) {
            Log.v("FileUtil", "createFileInSDCard filePath : " + filePath);
            Log.v("FileUtil", "createFileInSDCard result : " + result);
        }
        return file;
    }
    
    /**
     * @param dirPath  目录
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public static File createFileInSDCard(String dirPath, String fileName) throws IOException {
        String filePath = dirPath + File.separator + fileName;
        File file = new File(filePath);
        boolean result = false;
        if (!isFileExist(filePath)) {
            if (createDir(dirPath)) {
                result = file.createNewFile();
            }
        } else {
            result = true;
        }
        if (showLog) {
            Log.v("FileUtil", "createFileInSDCard filePath : " + filePath);
            Log.v("FileUtil", "createFileInSDCard result : " + result);
        }
        return file;
    }

    /**
     * @param dirStr 目录
     * @return file
     */
    public static boolean createDir(String dirStr) {
        File dir = new File(dirStr + File.separator);
        boolean result = dir.mkdir();
        if (showLog) {
            Log.v("FileUtil", "createDir dir : " + dirStr + File.separator);
            Log.v("FileUtil", "createDir result : " + result);
        }
        return result;
    }

    /**
     * 创建上级目录
     * @param savePath 文件绝对路径
     * @return
     */
    public static boolean createParentDir(String savePath) {
        File file = new File(savePath);
        int status = UNKNOW;
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            if (parentFile.mkdirs())
                status = CREATE_DIR_SUCCESS;
            else
                status = CREATE_ERROR;
        } else {
            status = DIR_EXISTS;
        }
        return checkResult(status, parentFile.getAbsolutePath());
    }


    /**
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        boolean result = file.exists();
        if (!result)
            checkResult(FILE_NOT_EXISTS, filePath);
        return result;
    }

    /**
     * 将数据写入sd卡
     *
     * @param dirPath
     * @param fileName
     * @param dataStr
     * @return
     */
    public static File write2SDCardFile(String dirPath, String fileName, String dataStr) {
        writeLock(lock);
        File file = null;
        OutputStream output = null;
        InputStream input = new ByteArrayInputStream(dataStr.getBytes());
        try {
            createDir(dirPath);
            file = createFileInSDCard(fileName, dirPath);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int temp = 0;
            while ((temp = input.read(buffer)) != -1) {
                output.write(buffer, 0, temp);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeUnlock(lock);
        return file;
    }

    /**
     * 将数据写入sd卡
     *
     * @param dirPath
     * @param fileName
     * @param input
     * @return
     */
    public static File write2SDCardFile(String dirPath, String fileName, InputStream input) {
        writeLock(lock);
        File file = null;
        OutputStream output = null;
        try {
            createDir(dirPath);
            file = createFileInSDCard(fileName, dirPath);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int temp = 0;
            while ((temp = input.read(buffer)) != -1) {
                output.write(buffer, 0, temp);
            }
            String split = "\n-------------------------\n";
            output.write(split.getBytes(), 0, split.length());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeUnlock(lock);
        return file;
    }


    /**
     * @param filePath
     */
    public static StringBuffer readFromFile(String filePath) {
        File file = new File(filePath);
        StringBuffer results = new StringBuffer();
        if (isFileExist(filePath)) {
            try {
                results = readLine(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public static StringBuffer readLine(Reader reader) {
        readLock(lock);
        BufferedReader bfReader = null;
        StringBuffer results = new StringBuffer();
        try {
            String line;
            bfReader = new BufferedReader(reader);
            while ((line = bfReader.readLine()) != null) {
                results.append(line);
            }
            bfReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bfReader != null) {
                try {
                    bfReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        readUnlock(lock);
        return results;
    }

    public static void delSysDir(String path) {
        LogKit.v("delete dir : " + path);
        if (isFileExist(path)) {
            String delStr = " rm -r " + path;
            int res = RootCmd.execRootCmd(delStr);
        }
    }

    public static String getFileMD5(File file) {
        return MD5Util.getMD5(file);
    }
    
    //------------
    private static boolean needLock = true;

    private static void writeLock(ReadWriteLock lock) {
        if (needLock) lock.writeLock().lock();
    }

    private static void writeUnlock(ReadWriteLock lock) {
        if (needLock) lock.writeLock().unlock();
    }

    private static void readLock(ReadWriteLock lock) {
        if (needLock) lock.readLock().lock();
    }

    private static void readUnlock(ReadWriteLock lock) {
        if (needLock) lock.readLock().unlock();
    }

    //
    private static boolean checkResult(int status, String str) {
        String log = "";
        boolean result = false;
        switch (status) {
            case CREATE_DIR_SUCCESS:
                log = "CREATE_DIR_SUCCESS";
                result = true;
                break;
            case CREATE_FILE_SUCCESS:
                log = "CREATE_FILE_SUCCESS";
                result = true;
                break;
            case FILE_NOT_EXISTS:
                log = "FILE_NOT_EXISTS";
                result = false;
                break;
            case FILE_EXISTS:
                log = "File_EXISTS";
                result = true;
                break;
            case DIR_EXISTS:
                log = "DIR_EXISTS";
                result = true;
                break;
            case CREATE_ERROR:
                log = "CREATE_ERROR";
                result = false;
                break;
            default:
                log = "UNKNOW";
                result = false;
                break;
        }
        if (showLog) {
            LogKit.w("FileUtil " + log + " : " + str);
        }
        return result;
    }

}
