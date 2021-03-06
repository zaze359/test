package com.zaze.utils.cache;

import java.util.Arrays;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
class Cache {

    private byte[] bytes;
    /**
     * 保存时长
     */
    private long keepTime;
    // --------------------------------------------------
    /**
     * 使用次数
     */
    private int usedNum;
    /**
     * 最近使用时间
     */
    private long lastTimeMillis;
    /**
     * 创建时间
     */
    private long createDate;
    /**
     * 类型 数据？ 图片？
     */
    private int type;

    /**
     * key
     */
    private String key;
    // --------------------------------------------------

    public Cache(String key, byte[] bytes, long keepTime, int usedNum, long createDate) {
        this.key = key;
        if (bytes != null) {
            this.bytes = new byte[bytes.length];
            System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
        }
        this.keepTime = keepTime;
        this.usedNum = usedNum;
        this.lastTimeMillis = createDate;
        this.createDate = createDate;
    }

    public int getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(int usedNum) {
        this.usedNum = usedNum;
    }

    public long getLastTimeMillis() {
        return lastTimeMillis;
    }

    public void setLastTimeMillis(long lastTimeMillis) {
        this.lastTimeMillis = lastTimeMillis;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public void increaseUsedNum() {
        this.usedNum++;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public long getKeepTime() {
        return keepTime;
    }

    public void setKeepTime(long keepTime) {
        this.keepTime = keepTime;
    }
// ----------------------------------------------------

    public long updateCache(byte[] newBytes, long keepTime) {
        long offset;
        if (this.bytes != null) {
            offset = this.bytes.length;
        } else {
            offset = 0;
        }
        if (newBytes != null && newBytes.length > 0) {
            offset = newBytes.length - offset;
            this.bytes = new byte[newBytes.length];
            System.arraycopy(newBytes, 0, this.bytes, 0, newBytes.length);
        } else {
            bytes = null;
            offset = -offset;
        }
        this.keepTime = keepTime;
        this.increaseUsedNum();
        this.setLastTimeMillis(System.currentTimeMillis());
        return offset;
    }

    @Override
    public String toString() {
        return "Cache{" +
                "bytes=" + Arrays.toString(bytes) +
                ", keepTime=" + keepTime +
                ", usedNum=" + usedNum +
                ", lastTimeMillis=" + lastTimeMillis +
                ", createDate=" + createDate +
                ", type=" + type +
                ", key='" + key + '\'' +
                '}';
    }
}
