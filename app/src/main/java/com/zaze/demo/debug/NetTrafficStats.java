package com.zaze.demo.debug;

/**
 * Description : 应用流量统计
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 16:10
 */
public class NetTrafficStats {

    /**
     * 应用对应uid
     */
    private int uid;
    /**
     * 接收数据大小
     */
    private long rxBytes;
    /**
     * 发送数据大小
     */
    private long txBytes;
    /**
     * 上一次合并时的大小, 开机时间发生变化则清空
     */
    private long mergedRxBytes;
    /**
     * 上一次合并时的大小, 开机时间发生变化则清空
     */
    private long mergedTxBytes;

    /**
     * 开机时间
     */
    private long bootTime;

    /**
     * 应用信息
     */
    private AppShortcut appShortcut;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getRxBytes() {
        return rxBytes;
    }

    public void setRxBytes(long rxBytes) {
        this.rxBytes = rxBytes;
    }

    public long getTxBytes() {
        return txBytes;
    }

    public void setTxBytes(long txBytes) {
        this.txBytes = txBytes;
    }

    public long getMergedRxBytes() {
        return mergedRxBytes;
    }

    public void setMergedRxBytes(long mergedRxBytes) {
        this.mergedRxBytes = mergedRxBytes;
    }

    public long getMergedTxBytes() {
        return mergedTxBytes;
    }

    public void setMergedTxBytes(long mergedTxBytes) {
        this.mergedTxBytes = mergedTxBytes;
    }

    public long getBootTime() {
        return bootTime;
    }

    public void setBootTime(long bootTime) {
        if (this.bootTime != bootTime) {
            this.bootTime = bootTime;
            setMergedRxBytes(0);
            setMergedTxBytes(0);
        }
    }

    public AppShortcut getAppShortcut() {
        return appShortcut;
    }

    public void setAppShortcut(AppShortcut appShortcut) {
        this.appShortcut = appShortcut;
    }


    /**
     * 封装一些操作
     * 从这个方法获取流量的变化
     *
     * @return long
     */
    public long getDiffRxBytes() {
        long diffRxBytes = rxBytes - mergedRxBytes;
        this.mergedRxBytes = rxBytes;
        return diffRxBytes > 0 ? diffRxBytes : 0;
    }

    /**
     * 封装一些操作
     * 从这个方法获取流量的变化
     *
     * @return long
     */
    public long getDiffTxBytes() {
        long diffTxBytes = txBytes - mergedTxBytes;
        this.mergedTxBytes = txBytes;
        return diffTxBytes > 0 ? diffTxBytes : 0;
    }

    @Override
    public String toString() {
        return "NetTrafficStats{" +
                "uid=" + uid +
                ", rxBytes=" + rxBytes +
                ", txBytes=" + txBytes +
                ", mergedRxBytes=" + mergedRxBytes +
                ", mergedTxBytes=" + mergedTxBytes +
                ", bootTime=" + bootTime +
                ", appShortcut=" + appShortcut +
                '}';
    }
}
