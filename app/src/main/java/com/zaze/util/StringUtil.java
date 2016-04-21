package com.zaze.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class StringUtil {

    /**
     * String to int
     * @param value
     * @param defaultValue
     * @return
     */
    public static int parseInt(String value, int defaultValue) {
        int id;
        if (!stringIsNull(value)) {
            try {
                id = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                id = 0;
            }
        } else {
            id = defaultValue;
        }
        return id;
    }

    /**
     * String 的处理， null 返回 defaultValue
     * @param value
     * @param defaultValue
     * @return
     */
    public static String parseString(String value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    /**
     * byte[] 转String
     * @param bytes
     * @param defaultValue
     * @return
     */
    public static String bytesToString(byte[] bytes, String defaultValue) {
        return bytesToString(bytes, defaultValue, "UTF-8");
    }

    /**
     * byte[] 转String
     * @param bytes
     * @param defaultValue
     * @return
     */
    public static String bytesToString(byte[] bytes, String defaultValue, String charsetName) {
        if (bytes == null) {
            return defaultValue;
        }
        try {
            return new String(bytes, charsetName).trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new String(bytes).trim();
    }
    
    
    /**
     * @param src
     * @param maxLength
     * @return
     */
    public static byte[] string2Bytes(String src, int maxLength) {
        byte[] des = new byte[maxLength];
        if(src != null) {
            byte[] temp = src.getBytes();
            int length = temp.length > maxLength ? maxLength : temp.length;
            System.arraycopy(temp, 0, des, 0, length);
        }
        return des;
    }

    public static byte[] string2Bytes(String src) {
        if(src != null) {
            return src.getBytes();
        }
        return null;
    }
    

    public static String arrayToString(int[] intArray, String split) {
        if(intArray == null) {
            return "";
        }
        if(split == null) {
            split = "";
        }
        StringBuilder ids = new StringBuilder();
        int size = intArray.length;
        for(int i = 0; i < size; i++) {
            if(i == 0) {
                ids.append(intArray[i]);
            } else {
                ids.append(split).append(intArray[i]);
            }
        }
        return ids.toString();
    }

    public static String arrayToString(String[] strArray, String split) {
        if(strArray == null) {
            return null;
        }
        if(split == null) {
            split = "";
        }
        StringBuilder ids = new StringBuilder();
        int size = strArray.length;
        for(int i = 0; i < size; i++) {
            if(i == 0) {
                ids.append(strArray[i]);
            } else {
                ids.append(split).append(strArray[i]);
            }
        }
        return ids.toString();
    }

    public static String arrayToString(List<String> list, String split) {
        if(list == null) {
            return null;
        }
        if(split == null) {
            split = "";
        }
        StringBuilder ids = new StringBuilder();
        int size = list.size();
        for(int i = 0; i < size; i++) {
            if(i == 0) {
                ids.append(list.get(i));
            } else {
                ids.append(split).append(list.get(i));
            }
        }
        return ids.toString();
    }

    /** 
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean stringIsNull(String str) {
        return null == str || str.isEmpty();
    }
}
