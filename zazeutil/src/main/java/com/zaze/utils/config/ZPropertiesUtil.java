package com.zaze.utils.config;

import android.text.TextUtils;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Description : 配置信息
 *
 * @author : zaze
 * @version : 2015-11-02 - 16:07
 */
public class ZPropertiesUtil {

    /**
     * 加载
     *
     * @param file
     * @return
     */
    public static Properties load(String file) {
        Properties properties = new Properties();
        if (!TextUtils.isEmpty(file)) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                properties.load(inputStream);
            } catch (Exception e) {
                ZLog.e(ZTag.TAG_ERROR, e.getMessage());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        ZLog.e(ZTag.TAG_ERROR, e.getMessage());
                    }
                }
            }
        }
        return properties;
    }

    public static String getProperty(String file, String key) {
        if (!TextUtils.isEmpty(file) && !TextUtils.isEmpty(key)) {
            Properties properties = load(file);
            return properties.getProperty(key);
        } else {
            return null;
        }
    }


    /**
     * 保存
     *
     * @param file
     * @param properties
     */
    public static void store(String file, Properties properties) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file, false);
            properties.store(outputStream, "");
        } catch (Exception e) {
            ZLog.e(ZTag.TAG_ERROR, e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    ZLog.e(ZTag.TAG_ERROR, e.getMessage());
                }
            }
        }
    }
}
