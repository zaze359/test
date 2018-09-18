package com.zaze.demo.debug;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.provider.Settings;
import android.util.Base64;
import android.widget.Toast;

import com.zaze.utils.FileUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-09-29 - 15:31
 */
public class TestDebug {

    private static byte[] bytes;

    private static final Pattern sTrimPattern =
            Pattern.compile("^[\\s|\\p{javaSpaceChar}]*(.*)[\\s|\\p{javaSpaceChar}]*$");

    public static void test(Context context) {
//        Toast.makeText(context, "sss", Toast.LENGTH_SHORT).show();
        FileUtil.reCreateFile("/sdcard/a.txt");
        FileUtil.rename("/sdcard/a.txt", "b.txt");
        FileUtil.move("/sdcard/b.txt", "/sdcard/b/b.txt");

//        try {
//            Intent intent = new Intent("com.xh.launcher.wake.up");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//            context.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Network[] networkArray = new Network[0];
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            networkArray = ZNetUtil.getConnectivityManager(context).getAllNetworks();
//        }
//        for (Network network : networkArray) {
//            ZLog.i(ZTag.TAG_DEBUG, "" + network.toString());
//        }

//        ZLog.i(ZTag.TAG_DEBUG, "" + encrypt("abc"));
    }

    public static String encrypt(String password) {
        long seedValue = System.currentTimeMillis();
        final Random random = new Random(seedValue);

        byte[] inData = password.getBytes(Charset.forName("utf-8"));

        byte[] mask = {0x0};
        for (int i = 0; i < inData.length; i++) {
            random.nextBytes(mask);
            inData[i] ^= mask[0];
        }

        final byte[] seeds = longToBytes(seedValue);
        ByteBuffer buffer = ByteBuffer.allocate(inData.length + seeds.length);
        buffer.put(inData);
        buffer.put(seeds);
        return Base64.encodeToString(buffer.array(), Base64.NO_WRAP | Base64.URL_SAFE);
    }

    private static byte[] longToBytes(long n) {
        byte[] b = new byte[8];
        b[7] = (byte) (n & 0xff);
        b[6] = (byte) (n >> 8 & 0xff);
        b[5] = (byte) (n >> 16 & 0xff);
        b[4] = (byte) (n >> 24 & 0xff);
        b[3] = (byte) (n >> 32 & 0xff);
        b[2] = (byte) (n >> 40 & 0xff);
        b[1] = (byte) (n >> 48 & 0xff);
        b[0] = (byte) (n >> 56 & 0xff);
        return b;
    }


    public static boolean hasPermissionToReadNetworkStats(Context context) {
        // 打开“有权查看使用情况的应用”页面
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        context.startActivity(intent);
        return false;
    }


    public static void beginDocument(XmlResourceParser parser) throws XmlPullParserException, IOException {
        ZLog.i(ZTag.TAG_DEBUG, "" + parser.getEventType());
        ZLog.i(ZTag.TAG_DEBUG, "" + parser.getName());
        int type = parser.next();
        ZLog.i(ZTag.TAG_DEBUG, "" + type);
        ZLog.i(ZTag.TAG_DEBUG, "" + parser.getEventType());
        ZLog.i(ZTag.TAG_DEBUG, "" + parser.getName());
        while (type != XmlPullParser.END_DOCUMENT) {
            if (type == XmlPullParser.START_TAG) {
//                ZLog.d(ZTag.TAG_DEBUG, "" + getAttributeValue(parser, "resolve"));
//                ZLog.d(ZTag.TAG_DEBUG, "" + getAttributeValue(parser, "favorites"));
//                ZLog.d(ZTag.TAG_DEBUG, "" + getAttributeValue(parser, "screen"));
            }
            type = parser.next();
            ZLog.i(ZTag.TAG_DEBUG, "" + type);
            ZLog.i(ZTag.TAG_DEBUG, "" + parser.getName());
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
