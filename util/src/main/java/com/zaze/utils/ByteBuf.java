package com.zaze.utils;


import android.os.SystemClock;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.SocketException;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-08-01 - 15:59
 */
public class ByteBuf {

    private static final String TAG = ZTag.TAG + "ByteBuf";

    private final int resetLength;
    /**
     * 默认分配长度
     */
    private final int defaultLength;

    protected byte[] byteBuffer;
    /**
     * 当前byte数据长度
     */
    private int dataLength = 0;

    public ByteBuf(int defaultLength) {
        if (defaultLength <= 0) {
            defaultLength = 1024;
        }
        byteBuffer = new byte[defaultLength];
        this.defaultLength = defaultLength;
        resetLength = defaultLength * 10;
    }

    /**
     * 拓展空间大小
     *
     * @param needLength 需要读取多大
     */
    private void expand(int needLength) {
        if (dataLength + needLength > byteBuffer.length) {
            // 暂存原始数据
            byte[] tempBuf = byteBuffer;
            byteBuffer = new byte[dataLength + needLength + 1];
            // 拷贝原始数据
            System.arraycopy(tempBuf, 0, byteBuffer, 0, tempBuf.length);
        }
    }

    /**
     * @param inputStream inputStream
     * @param needLength  需要读多长
     * @return true byteBuffer已读取足够的数据, false数据不够
     * @throws Exception Exception
     */
    public boolean read(@NotNull InputStream inputStream, int needLength) throws Exception {
        if (needLength <= 0) {
            return true;
        }
        int readLength = readToBuffer(inputStream, needLength);
        // 测试代码
//        int readLength = readToBuffer(inputStream, Math.min(needLength, 10));
        if (readLength > 0) {
            dataLength += readLength;
        } else {
            // 表示当前管道数据已读完
        }
        return readLength == needLength;
    }

    public int readToBuffer(@NotNull InputStream inputStream, int needLength) throws Exception {
        // 读取指定长度
        expand(needLength);
//        return readAndRetry(inputStream, byteBuffer, dataLength, needLength);
        return inputStream.read(byteBuffer, dataLength, needLength);
    }


    /**
     * 超时增加重试逻辑
     *
     * @param inputStream
     * @param buffer
     * @param off
     * @param len
     * @return
     * @throws Exception Exception
     */
    private long readAndRetry(@NotNull InputStream inputStream, byte[] buffer, int off, int len) throws Exception {
        for (int i = 0; i < 3; ) {
            try {
                long length = actualRead(inputStream, buffer, off, len);
                i = Integer.MAX_VALUE;
                return length;
            } catch (SocketException e) {
                ZLog.e(TAG, "readAndRetry actualRead 出现异常" + e.getMessage());
                if (isConnectionTimeOut(e)) {
                    i++;
                    ZLog.w(TAG, "readAndRetry 超时异常 准备重试：" + i + "次");
                    SystemClock.sleep(1000L);
                } else {
                    throw e;
                }
            }
        }
        throw new SocketException("readAndRetry error");
    }

    private long actualRead(@NotNull InputStream inputStream, byte[] buffer, int off, int len) throws Exception {
        return inputStream.read(buffer, off, len);
    }

    private boolean isConnectionTimeOut(SocketException e) {
        String exceptionMessage = e.getMessage();
        ZLog.w(TAG, "isConnectionTimeOut: " + exceptionMessage);
        if (exceptionMessage == null || exceptionMessage.isEmpty()) {
            return false;
        } else {
            return exceptionMessage.contains("Connection timed out");
        }
    }

    // --------------------------------------------------
    public int getDataLength() {
        return dataLength;
    }

    // --------------------------------------------------
    public byte[] take() {
        byte[] byteArray = get();
        reset();
        return byteArray;
    }

    public byte[] get() {
        if (dataLength == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[dataLength];
        System.arraycopy(byteBuffer, 0, byteArray, 0, dataLength);
        return byteArray;
    }

    public void reset() {
        if (byteBuffer.length > resetLength) {
            byteBuffer = new byte[defaultLength];
        }
        dataLength = 0;
    }
}
