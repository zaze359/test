package com.zaze.demo.debug;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Environment;
import android.provider.Settings;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-09-29 - 15:31
 */
public class TestDebug {
    public static void test(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            TestDebug.hasPermissionToReadNetworkStats(context);
//        }
//        ZLog.d(ZTag.TAG_DEBUG, "toByteUnit : %s", DescriptionUtil.toByteUnit(1024 * 1024 * 1024 + 1024 * 1024));
        // --------------------------------------------------
//        String ACTION_LAUNCHER_CUSTOMIZATION = "android.autoinstalls.config.action.PLAY_AUTO_INSTALL";
//        ZAppUtil.INSTANCE.findSystemApk(context, ACTION_LAUNCHER_CUSTOMIZATION);
//        SimpleLayoutParser simpleLayoutParser = new SimpleLayoutParser(context);
//        simpleLayoutParser.parser(R.xml.default_workspace_4x4, new ArrayList<Long>());
        // --------------------------------------------------
//        XmlResourceParser parser = context.getResources().getXml(R.xml.default_workspace_4x4);
//        try {
//            beginDocument(parser);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        ZConfigHelper configHelper = ZConfigHelper.newInstance(context.getFilesDir().getAbsolutePath() + "/config/zaze.ini");
//        configHelper.setProperty("a", "112233");
//        ZLog.d(ZTag.TAG_DEBUG, configHelper.getProperty("a"));
//        ZFileUtil.INSTANCE.reCreateDir();
        ZLog.d(ZTag.TAG_DEBUG, Environment.getExternalStorageDirectory().getAbsolutePath());
        ZLog.d(ZTag.TAG_DEBUG, Environment.getExternalStorageDirectory().getParentFile().getAbsolutePath());

//        File file = new File("/proc/net/xt_qtaguid/stats");
//        AnalyzeTrafficCompat.getNewestNetworkTraffic();
//        AnalyzeTrafficCompat.analyzeProcStat();
//        AnalyzeTrafficCompat.getInstance(context).archiveNetworkTraffic(bootTime);
//        String cmd = "cat /proc/net/xt_qtaguid/stats | grep 10135";
//        ZCommand.CommandResult commandResult;
//        if (ZCommand.isRoot()) {
//            commandResult = ZCommand.execRootCmdForRes(cmd);
//        } else {
//            commandResult = ZCommand.execCmdForRes(cmd);
//        }
//        ZLog.d(ZTag.TAG_DEBUG, "analyzeFileFirstLineIsTag : " + AnalyzeUtil.analyzeFileFirstLineIsTag("/proc/net/xt_qtaguid/stats", "\n", "\\s+"));
//        ZLog.d(ZTag.TAG_DEBUG, "getDayNetworkTraffic : " + AnalyzeTrafficCompat.getInstance(context).getDayNetworkTraffic());
//        ZLog.d(ZTag.TAG_DEBUG, "rx_bytes : " + ZFileUtil.INSTANCE.readFromFile("/sys/class/net/wlan0/statistics/rx_bytes"));
//        ZLog.d(ZTag.TAG_DEBUG, "rx_bytes : " + AnalyzeTrafficCompat.getInstance(context).getDayNetworkTraffic());

//        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//        context.startActivity(intent);
//        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
//        Calendar calendar = Calendar.getInstance();
//        long endTime = calendar.getTimeInMillis();
//        calendar.add(Calendar.YEAR, -1);
//        long startTime = calendar.getTimeInMillis();
//        List usageStatsList = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, System.currentTimeMillis());
//            ZLog.d(ZTag.TAG_DEBUG, "usageStatsList : " + usageStatsList);
//        }
    }

    public static boolean hasPermissionToReadNetworkStats(Context context) {
//        final AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
//                android.os.Process.myUid(), context.getPackageName());
//        if (mode == AppOpsManager.MODE_ALLOWED) {
//            return true;
//        }
        // 打开“有权查看使用情况的应用”页面
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        context.startActivity(intent);
        return false;
    }


    public static void beginDocument(XmlResourceParser parser) throws XmlPullParserException, IOException {
        int type = XmlPullParser.START_DOCUMENT;
        while (type != XmlPullParser.END_DOCUMENT) {
            if (type == XmlPullParser.START_TAG) {
                ZLog.d(ZTag.TAG_DEBUG, "" + getAttributeValue(parser, "resolve"));
                ZLog.d(ZTag.TAG_DEBUG, "" + getAttributeValue(parser, "favorites"));
                ZLog.d(ZTag.TAG_DEBUG, "" + getAttributeValue(parser, "screen"));
            }
            type = parser.next();
//            ZLog.i(ZTag.TAG_DEBUG, "" + parser.getName());
        }
    }

    /**
     * Return attribute value, attempting launcher-specific namespace first
     * before falling back to anonymous attribute.
     */
    protected static String getAttributeValue(XmlResourceParser parser, String attribute) {
        String value = parser.getAttributeValue(
                "http://schemas.android.com/apk/res-auto/com.android.launcher3", attribute);
        if (value == null) {
            value = parser.getAttributeValue(null, attribute);
        }
        return value;
    }

    /**
     * Return attribute resource value, attempting launcher-specific namespace
     * first before falling back to anonymous attribute.
     */
    protected static int getAttributeResourceValue(XmlResourceParser parser, String attribute,
                                                   int defaultValue) {
        int value = parser.getAttributeResourceValue(
                "http://schemas.android.com/apk/res-auto/com.android.launcher3", attribute,
                defaultValue);
        if (value == defaultValue) {
            value = parser.getAttributeResourceValue(null, attribute, defaultValue);
        }
        return value;
    }


}
