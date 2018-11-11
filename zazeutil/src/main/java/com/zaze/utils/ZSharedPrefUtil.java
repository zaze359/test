package com.zaze.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-08 - 16:02
 */
public class ZSharedPrefUtil {

    private SharedPreferences sharedPreferences;

    public static ZSharedPrefUtil newInstance(Context context) {
        return newInstance(context, null);
    }

    public static ZSharedPrefUtil newInstance(final Context context, final String spName) {
        return new ZSharedPrefUtil(context, spName);
    }

    private ZSharedPrefUtil(Context context, String spName) {
        if (TextUtils.isEmpty(spName)) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        } else {
            sharedPreferences = context.getApplicationContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        }
    }

    /**
     * 异步
     *
     * @param key   key
     * @param value value
     * @param <T>   t
     */
    public <T> void apply(String key, T value) {
        if (sharedPreferences != null) {
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            put(key, value, editor).apply();
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
    public <T> boolean commit(String key, T value) {
        if (sharedPreferences != null) {
            return put(key, value, sharedPreferences.edit()).commit();
        }
        return false;
    }

    public boolean contains(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.contains(key);
        }
        return false;
    }

    private <T> SharedPreferences.Editor put(final String key, final T value, final SharedPreferences.Editor editor) {
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

    public <T> T get(String key, T defaultValue) {
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
            } else {
                ZLog.w(ZTag.TAG_ERROR, "not support type");
            }
        }
        return defaultValue;
    }

}
