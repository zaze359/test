package com.zaze.aarrepo.commons.cache;

import com.zaze.aarrepo.utils.StringUtil;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class Cache {

    private byte[] bytes;
    /**
     * 保存时长
     */
    private long keepTime;
    // -------------------
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
    
    //
    private String key;
    // -----------------
    
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
    public void increaseUsedNum(){
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

//    public void setCacheData(byte[] bytes, long keepTime) {
//        if (bytes != null) {
//            this.bytes = new byte[bytes.length];
//            System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
//        }
//        this.keepTime = keepTime;
//    }

    public void updateCache(byte[] bytes, long keepTime) {
        if (bytes != null) {
            this.bytes = new byte[bytes.length];
            System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
        }
        this.keepTime = keepTime;
        this.increaseUsedNum();
        this.setLastTimeMillis(System.currentTimeMillis());
    }
    
    @Override
    public String toString() {
        return "Cache{" +
                "bytes=" + StringUtil.bytesToString(bytes) +
                ", keepTime=" + keepTime +
                ", usedNum=" + usedNum +
                ", lastTimeMillis=" + lastTimeMillis +
                ", createDate=" + createDate +
                ", type=" + type +
                ", key='" + key + '\'' +
                '}';
    }
}
