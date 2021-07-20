package com.zaze.utils;

/**
 * Description : 用于转换生成一些描述的工具类
 *
 * @author : ZAZE
 * @version : 2017-12-28 - 10:32
 */
public class DescriptionUtil {

    /**
     * 转换成字节单位
     *
     * @param byteLength 字节长度
     * @return 格式化后输出
     */
    public static String toByteUnit(long byteLength) {
        long kb = byteLength >> 10;
        long mb = byteLength >> 20;
        long gb = byteLength >> 30;
        if (gb > 0) {
            return ZStringUtil.format("%.3fGB", mb * 1.0f / 1024);
        } else if (mb > 0) {
            return ZStringUtil.format("%.3fMB", kb * 1.0f / 1024);
        } else if (kb > 0) {
            return ZStringUtil.format("%dKB", kb);
        }
        return ZStringUtil.format("%dB", byteLength);
    }
}
