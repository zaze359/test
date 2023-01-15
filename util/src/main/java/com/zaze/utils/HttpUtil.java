package com.zaze.utils;

import android.text.TextUtils;

import com.zaze.utils.ext.HttpExtKt;

import java.util.HashMap;
import java.util.Map;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-09 - 14:15
 */
public class HttpUtil {
    public static final String URL = "url";

    /**
     * 构建get 请求
     *
     * @param url url
     * @param map map
     * @return String
     */
    public static String buildGetRequest(String url, Map<String, String> map) {
        return HttpExtKt.buildGetRequest(url, map);
    }

    /**
     * 处理get请求
     *
     * @param url url
     * @return Map<String, String>
     */
    public static Map<String, String> processGetRequest(String url) {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(url)) {
            String[] strArray = url.split("\\?");
            if (strArray.length == 2) {
                map.put(URL, strArray[0]);
                String[] paramArray = strArray[1].split("&");
                for (String param : paramArray) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2) {
                        map.put(keyValue[0], keyValue[1]);
                    }
                }
            }
        }
        return map;
    }

}
