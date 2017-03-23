package com.zaze.aarrepo.utils;

import java.net.URLEncoder;
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
        if (map.isEmpty()) {
            return url;
        }
        StringBuilder paramBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            if (paramBuilder.length() != 0) {
                paramBuilder.append("&");
            }
            paramBuilder.append(URLEncoder.encode(key));
            paramBuilder.append("=");
            paramBuilder.append(URLEncoder.encode(map.get(key)));
        }
        return StringUtil.format("%s?%s", url, paramBuilder.toString());
    }

    /**
     * 处理get请求
     *
     * @param url url
     * @return Map<String, String>
     */
    public static Map<String, String> processGetRequest(String url) {
        Map<String, String> map = new HashMap<>();
        if (!StringUtil.isEmpty(url)) {
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
