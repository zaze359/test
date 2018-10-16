package com.zaze.demo.debug;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;

import com.zaze.demo.model.entity.DeviceStatus;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.sevenzip4j.SevenZipArchiveOutputStream;
import org.sevenzip4j.archive.SevenZipEntry;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
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

    private static DeviceStatus deviceStatus = new DeviceStatus();
    private static WeakReference<List<DeviceStatus>> reference1 = new WeakReference<>(Collections.singletonList(deviceStatus));
    private static WeakReference<DeviceStatus> reference2 = new WeakReference<>(deviceStatus);
    private static List<WeakReference<DeviceStatus>> reference3 = null;

    public static void test(Context context) {
        // --------------------------------------------------
//        Toast.makeText(context, "sss", Toast.LENGTH_SHORT).show();
//        FileUtil.writeToFile("/sdcard/a.txt", "adsfasdfasdfasdfd");
//        FileUtil.rename("/sdcard/a.txt", "b.txt");
//        FileUtil.move("/sdcard/b.txt", "/sdcard/b/b.txt");
//        FileUtil.writeToFile("/sdcard/b/ba.txt", "ccccc#######!@#lkajsd;flkj;sdjhfl12ou1o2kjlhklsdhjfksd");
//        build7Zip("/sdcard/b");
        // --------------------------------------------------
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
        // --------------------------------------------------
//        ZLog.i(ZTag.TAG_DEBUG, "" + encrypt("abc"));
        // --------------------------------------------------
        // --------------------------------------------------
//        if (reference3 == null) {
//            reference3 = new ArrayList<>();
//            reference3.add(new WeakReference<>(deviceStatus));
//            reference3.add(new WeakReference<>(new DeviceStatus()));
//            reference3.add(new WeakReference<>(new DeviceStatus()));
//            reference3.add(new WeakReference<>(new DeviceStatus()));
//            reference3.add(new WeakReference<>(new DeviceStatus()));
//            reference3.add(new WeakReference<>(new DeviceStatus()));
//        }
//        ZLog.i(ZTag.TAG_DEBUG, "reference1 : " + reference1.get());
//        ZLog.i(ZTag.TAG_DEBUG, "reference2 : " + reference2.get());
//        for (WeakReference weakReference : reference3) {
//            ZLog.i(ZTag.TAG_DEBUG, "reference3 : " + weakReference.get());
//        }
    }


    /**
     * Compress test
     */
    static void build7Zip(final String dir) {
        ThreadManager.getInstance().runInMultiThread(new Runnable() {
            @Override
            public void run() {
                SevenZipArchiveOutputStream os = null;
                try {
                    os = new SevenZipArchiveOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/testout.7z"));
                    File dirFile = new File(dir);
                    for (File file : dirFile.listFiles()) {
                        SevenZipEntry sevenEntry = new SevenZipEntry();
                        setSevenZipEntryAttributes(file, sevenEntry);
                        os.putNextEntry(sevenEntry);
                        copy(os, new FileInputStream(file));
                    }
                    os.finish();
                    os.close();
                } catch (Exception e) {
                    try {
                        if (os != null) {
                            os.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        });
    }


    private static void copy(OutputStream out, InputStream in) throws IOException {
        byte[] buf = new byte[1024 * 1024];
        int i = 0;
        while ((i = in.read(buf)) != -1) {
            out.write(buf, 0, i);
        }
    }

    /**
     * set zip options
     */
    private static void setSevenZipEntryAttributes(File file, SevenZipEntry sevenEntry) {
        sevenEntry.setName(file.getName());
        sevenEntry.setSize(file.length());
        sevenEntry.setLastWriteTime(file.lastModified());
        sevenEntry.setReadonly(!file.canWrite());
        sevenEntry.setHidden(file.isHidden());
        sevenEntry.setDirectory(file.isDirectory());
        sevenEntry.setArchive(true);
        sevenEntry.setSystem(false);
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
