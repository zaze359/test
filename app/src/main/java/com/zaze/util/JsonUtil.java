package com.zaze.util;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.List;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class JsonUtil {

    public static <T> T parseJson(String json, Class<T> classOfT) {
        if (json == null || classOfT == null) {
            return null;
        }
        Gson gson = new Gson();
        json = decode(json);
        return gson.fromJson(json, classOfT);
    }

    public static <T> String objToJson(T t) {
        if (t == null) {
            return null;
        }
        Gson gson = new Gson();
        try {
            return gson.toJson(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析json列表字符串
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> parseJsonToList(String json, Type type) {
        if (json == null || type == null) {
            return null;
        }
        List<T> retObj = null;
        Gson gson = new Gson();
        json = decode(json);
        retObj = gson.fromJson(json, type);
        return retObj;
    }

    public static String toJsonArray(Collection<JSONObject> jsonObjects) {
        JSONArray jsonArray = new JSONArray();
        if (jsonObjects != null) {
            for (JSONObject obj : jsonObjects) {
                jsonArray.put(obj);
            }
        }
        return jsonArray.toString();
    }

    public static String toJsonArray(JSONObject[] jsonObjects) {
        JSONArray jsonArray = new JSONArray();
        if (jsonObjects != null) {
            for (JSONObject obj : jsonObjects) {
                jsonArray.put(obj);
            }
        }
        return jsonArray.toString();
    }

    private static String decode(String json) {
        try {
            json = URLDecoder.decode(json, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
