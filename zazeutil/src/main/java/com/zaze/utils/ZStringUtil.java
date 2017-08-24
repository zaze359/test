package com.zaze.utils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class ZStringUtil {

    /**
     * @param format
     * @param args
     * @return
     */
    public static String format(String format, Object... args) {
        try {
            return String.format(Locale.getDefault(), format, args);
        } catch (Exception e) {
            e.printStackTrace();
            return format;
        }
    }


    /**
     * String to int(0)
     *
     * @param value
     * @return defaultValue=0
     */
    public static int parseInt(String value) {
        return parseInt(value, 0);
    }

    /**
     * String to int
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static int parseInt(String value, int defaultValue) {
        int id;
        if (!isEmpty(value)) {
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
     * @param value
     * @return defaultValue=""
     */
    public static String parseString(String value) {
        return parseString(value, "");
    }

    /**
     * String 的处理， null 返回 defaultValue
     *
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
     *
     * @param bytes
     * @return
     */
    public static String bytesToString(byte[] bytes) {
        return bytesToString(bytes, "");
    }

    /**
     * byte[] 转String
     *
     * @param bytes
     * @param defaultValue
     * @return
     */
    public static String bytesToString(byte[] bytes, String defaultValue) {
        return bytesToString(bytes, defaultValue, "UTF-8");
    }

    /**
     * byte[] 转String
     *
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
        if (src != null) {
            byte[] temp = src.getBytes();
            int length = temp.length > maxLength ? maxLength : temp.length;
            System.arraycopy(temp, 0, des, 0, length);
        }
        return des;
    }

    public static byte[] string2Bytes(String src) {
        if (src != null) {
            return src.getBytes();
        }
        return null;
    }

    public static String arrayToString(int[] intArray, String split) {
        if (intArray == null) {
            return "";
        }
        if (split == null) {
            split = "";
        }
        StringBuilder ids = new StringBuilder();
        int size = intArray.length;
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                ids.append(intArray[i]);
            } else {
                ids.append(split).append(intArray[i]);
            }
        }
        return ids.toString();
    }

    public static String arrayToString(String[] strArray, String split) {
        if (strArray == null) {
            return null;
        }
        if (split == null) {
            split = "";
        }
        StringBuilder ids = new StringBuilder();
        int size = strArray.length;
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                ids.append(strArray[i]);
            } else {
                ids.append(split).append(strArray[i]);
            }
        }
        return ids.toString();
    }

    public static String arrayToString(List<String> list, String split) {
        if (list == null) {
            return null;
        }
        if (split == null) {
            split = "";
        }
        StringBuilder ids = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                ids.append(list.get(i));
            } else {
                ids.append(split).append(list.get(i));
            }
        }
        return ids.toString();
    }

    // --------------------------------------------------

    /**
     * 判断字符串是否为空
     *
     * @param str str
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return null == str || str.isEmpty();
    }

    // --------------------------------------------------

    /**
     * @param one
     * @param two
     * @return
     */
    public static boolean equals(CharSequence one, CharSequence two) {
        if (one == two) {
            return true;
        }
        int length;
        if (one != null && two != null && ((length = one.length()) == two.length())) {
            if (one instanceof String && two instanceof String) {
                return one.equals(two);
            } else {
                for (int i = 0; i < length; i++) {
                    if (one.charAt(i) != two.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

}
