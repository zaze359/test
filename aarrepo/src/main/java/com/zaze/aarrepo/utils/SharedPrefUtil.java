package com.zaze.aarrepo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-08 - 16:02
 */
public class SharedPrefUtil {
    private static String spName = "sp_zaze";

    public static void setSpName(String spName) {
        SharedPrefUtil.spName = spName;
    }

    public static <T> void put(Context context, String key, T value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
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
        editor.commit();
    }

    public static <T> T get(Context context, String key, T defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        if (defaultValue instanceof String) {
            return (T) sharedPreferences.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return (T) new Integer(sharedPreferences.getInt(key, (Integer) defaultValue));
        } else if (defaultValue instanceof Boolean) {
            return (T) new Boolean(sharedPreferences.getBoolean(key, (Boolean) defaultValue));
        } else if (defaultValue instanceof Float) {
            return (T) new Float(sharedPreferences.getFloat(key, (Float) defaultValue));
        } else if (defaultValue instanceof Long) {
            return (T) new Long(sharedPreferences.getLong(key, (Long) defaultValue));
        }
        return null;
    }
}
