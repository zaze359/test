package com.zaze.commons.heart;


import com.zaze.commons.log.LogKit;

/**
 * Description : 心跳检测
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class HeartDetection {
    /**
     * 心跳间隔
     */
    private static final long heartInterval = 1000 * 10;
    /**
     * 超时时间
     */
    private static final long timeout = heartInterval * 3;

    /**
     * 最近一次时间
     */
    private long lastRunTime = 0;

    /**
     * 检验心跳
     * @return
     */
    public int checkHeart() {
        // TODO 需要增加心跳检测
        if(lastRunTime == 0) {
            // 第一次
            return 1;
        }
        if (System.currentTimeMillis() - lastRunTime > timeout) {
            LogKit.e("HeartDetection time out");
            //heart time out
        }
        if (System.currentTimeMillis() - lastRunTime > heartInterval) {//send heartmsg
            return 1;
        }
        return 0;
    }

    /**
     * 运行
     * @param timeMillis
     */
    public void run(long timeMillis) {
        lastRunTime = timeMillis;
    }
}
