package com.zaze.utils;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 15:05
 */
public class AnalyzeUtil {

    private static boolean needLog = false;

    public static void setNeedLog(boolean needLog) {
        AnalyzeUtil.needLog = needLog;
    }

    /**
     * 分析第一行数据表示标签的数据
     * [filePath] 文件路径
     * [lineSplit] 行分隔符
     * [valueSplit] 每个值之间的分隔符
     */
    public static JSONArray analyzeFileFirstLineIsTag(String filePath, String lineSplit, String valueSplit) {
        return analyzeLineValueFirstLineIsTag(analyzeFileByLine(filePath, lineSplit), valueSplit);
    }

    /**
     * 分析每行数据中的第一个值表示tag的数据
     * [filePath] 文件路径
     * [lineSplit] 行分隔符
     * [valueSplit] 每个值之间的分隔符
     */
    public static JSONObject analyzeFileFirstValueIsTag(String filePath, String lineSplit, String valueSplit) {
        return analyzeLineValueFirstValueIsTag(analyzeFileByLine(filePath, lineSplit), valueSplit);
    }


    /**
     * 按行分析文件
     *
     * @param filePath  filePath
     * @param lineSplit 行分隔符
     * @return String[]
     */
    protected static String[] analyzeFileByLine(String filePath, String lineSplit) {
        if (FileUtil.INSTANCE.isCanRead(filePath)) {
            return FileUtil.INSTANCE.readFromFile(filePath).toString().split(lineSplit);
        } else {
            return null;
        }
    }

    /**
     * 分析第一行数据表示标签的数据
     * 格式如下:
     * aa bb cc
     * 1 2 3
     * 22 33 44
     *
     * @param lineArray  lineArray
     * @param valueSplit 值分隔符
     * @return JSONArray
     */
    protected static JSONArray analyzeLineValueFirstLineIsTag(String[] lineArray, String valueSplit) {
        if (lineArray != null && lineArray.length > 0) {
            String[] tagArray = lineArray[0].split(valueSplit);
            JSONArray jsonArray = new JSONArray();
            for (int i = 1; i < lineArray.length; i++) {

                String[] valueArray = lineArray[i].split(valueSplit);
                JSONObject jsonObject = new JSONObject();
                for (int j = 0; j < valueArray.length; j++) {
                    if (j < tagArray.length) {
                        try {
                            jsonObject.put(tagArray[j], valueArray[j]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                jsonArray.put(jsonObject);
            }
            if (needLog) {
                ZLog.i(ZTag.TAG_ANALYZE, jsonArray.toString());
            }
            return jsonArray;
        } else {
            return null;
        }
    }


    /**
     * 分析每行数据中的第一个值表示tag的数据
     * aa: 111
     * ccc: 222
     * d: 333
     *
     * @param lineArray  lineArray
     * @param valueSplit 值分割符
     * @return JSONArray
     */
    protected static JSONObject analyzeLineValueFirstValueIsTag(String[] lineArray, String valueSplit) {
        if (lineArray != null && lineArray.length > 0) {
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < lineArray.length; i++) {
                String value = lineArray[i];
                Pattern pattern = Pattern.compile(valueSplit);
                Matcher matcher = pattern.matcher(value);
                int start = 0;
                int end = 0;
                if (matcher.find()) {
                    start = matcher.start();
                    end = matcher.end();
                }
                try {
                    jsonObject.put(value.substring(0, start), value.substring(end));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
////                String[] valueArray = value.split(valueSplit);
//                int valueLength = valueArray.length;
//                if (valueLength > 1) {
//                    String tag = valueArray[0];
//                    try {
//                        jsonObject.put(tag, value.substring(tag.length()));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
            if (needLog) {
                ZLog.i(ZTag.TAG_ANALYZE, jsonObject.toString());
            }
            return jsonObject;
        } else {
            return null;
        }
    }
}
