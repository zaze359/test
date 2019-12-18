package com.zaze.demo.debug;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.zaze.utils.FileUtil;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.ZCommand;
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
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-09-29 - 15:31
 */
public class TestDebug {

    private static final String TAG = "TestDebug";
    private static byte[] bytes;

    private static final Pattern sTrimPattern =
            Pattern.compile("^[\\s|\\p{javaSpaceChar}]*(.*)[\\s|\\p{javaSpaceChar}]*$");

    public static void test(final Context context) {
//        ThreadPlugins.runInIoThread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 1000; i++) {
//                    List<ApplicationInfo> list = AppUtil.getInstalledApplications(context, 0);
//                    for (ApplicationInfo app : list) {
//                        Log.i(TAG, "getInstalledApplication : " + app.packageName);
//                    }
//                }
//            }
//        });
//        new A<B>().a("{\"a\" : \"abc\"}");
//        aaaaaa(context);
        ZCommand.CommandResult result = ZCommand.execCmdForRes("dumpsys activity activities");
        Log.i(TAG, "CommandResult = " + result.successMsg);
    }

    private static final OkHttpClient client = new OkHttpClient();

    //下载
    private static void download(final String url, final String savePath) {
        final long startTime = System.currentTimeMillis();
        Log.i(TAG, "startTime=" + startTime);
        Log.d(TAG, "download: url = " + url + " savePath = " + savePath);
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
                Log.i(TAG, "download failed e = " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Sink sink = null;
                BufferedSink bufferedSink = null;
                try {
//                    String mSDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();//SD卡路径
                    //String appPath= getApplicationContext().getFilesDir().getAbsolutePath();//此APP的files路径
                    File dest = new File(savePath);
                    sink = Okio.sink(dest);
                    bufferedSink = Okio.buffer(sink);
                    bufferedSink.writeAll(response.body().source());

                    bufferedSink.close();
                    Log.i(TAG, "download success");
                    Log.i(TAG, "totalTime=" + (System.currentTimeMillis() - startTime));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "download failed e = " + e);
                } finally {
                    if (bufferedSink != null) {
                        bufferedSink.close();
                    }
                }
            }
        });
    }

    /**
     * 默认浏览器下载
     *
     * @param context
     * @param url
     */
    public static void downFromBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * @param apkPath
     * @return 得到对应插件的Resource对象
     */
    public static void getPluginResources(Context context, String apkPath, String assetFileName) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            //反射调用方法addAssetPath(String path)
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            //将未安装的Apk文件的添加进AssetManager中,第二个参数是apk的路径
            addAssetPath.invoke(assetManager, apkPath);
            FileUtil.writeToFile("/sdcard/aa.clear", assetManager.open(assetFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    /**
//     * @param apkPath
//     * @return 得到对应插件的Resource对象
//     */
//    private Resources getPluginResources(Context context, String apkPath) {
//        try {
//            AssetManager assetManager = AssetManager.class.newInstance();
//            //反射调用方法addAssetPath(String path)
//            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
//            //将未安装的Apk文件的添加进AssetManager中,第二个参数是apk的路径
//            addAssetPath.invoke(assetManager, apkPath);
//            Resources superRes = context.getResources();
//            Resources mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
//            return mResources;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    static void aaaaaa(final Context context) {
        for (int i = 0; i < 4; i++) {
            new Thread() {
                @Override
                public void run() {
                    int count = 0;
                    List<PackageInfo> list = context.getPackageManager()
                            .getInstalledPackages(0);
//                    for (PackageInfo info : list) {
//                        if (count >= 1000) {
//                            break;
//                        }
//                        try {
//                            PackageInfo pi = context.getPackageManager()
//                                    .getPackageInfo(info.packageName,
//                                            PackageManager.GET_ACTIVITIES);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
            }.start();
        }
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
