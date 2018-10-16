package com.zaze.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-08 - 16:02
 */
public class ZSharedPrefUtil {

    private static String spName = "sp_zaze";

    public static void setSpName(String spName) {
        ZSharedPrefUtil.spName = spName;
    }

    private static volatile SharedPreferences sharedPreferences;

    public static void initSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            synchronized (ZSharedPrefUtil.class) {
                sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
            }
        }
    }

    private static void initSharedPreferences(Context context, String sharePrefName) {
        spName = sharePrefName;
        initSharedPreferences(context);
    }


    /**
     * 异步
     *
     * @param key   key
     * @param value value
     * @param <T>   t
     */
    public static <T> void apply(String key, T value) {
        if (sharedPreferences != null) {
            put(key, value, sharedPreferences.edit()).apply();
        }
    }

    /**
     * 同步
     *
     * @param key   key
     * @param value value
     * @param <T>   t
     * @return boolean
     */
    public static <T> boolean commit(String key, T value) {
        if (sharedPreferences != null) {
            return put(key, value, sharedPreferences.edit()).commit();
        }
        return false;
    }

    public static boolean contains(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.contains(key);
        }
        return false;
    }

    private static <T> SharedPreferences.Editor put(String key, T value, SharedPreferences.Editor editor) {
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, ((Integer) value));
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) value));
        } else if (value instanceof Float) {
            editor.putFloat(key, ((Float) value));
        } else if (value instanceof Long) {
            editor.putLong(key, ((Long) value));
        }
        return editor;
    }


    // --------------------------------------------------

    public static <T> T get(String key, T defaultValue) {
        if (sharedPreferences != null) {
            if (defaultValue instanceof String) {
                return (T) sharedPreferences.getString(key, (String) defaultValue);
            } else if (defaultValue instanceof Integer) {
                return (T) Integer.valueOf(sharedPreferences.getInt(key, (Integer) defaultValue));
            } else if (defaultValue instanceof Boolean) {
                return (T) Boolean.valueOf(sharedPreferences.getBoolean(key, (Boolean) defaultValue));
            } else if (defaultValue instanceof Float) {
                return (T) Float.valueOf(sharedPreferences.getFloat(key, (Float) defaultValue));
            } else if (defaultValue instanceof Long) {
                return (T) Long.valueOf(sharedPreferences.getLong(key, (Long) defaultValue));
            }
        }
        return defaultValue;
    }

}
