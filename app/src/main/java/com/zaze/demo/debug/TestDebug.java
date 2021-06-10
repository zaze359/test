package com.zaze.demo.debug;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.zaze.utils.FileUtil;
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
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
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

    private static FileLock fileLock;

    public static final String TABLE_NAME = "favorites";
    public static final String AUTHORITY = "com.android.launcher3.settings".intern();

    /**
     * The content:// style URL for this table
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" +
            AUTHORITY + "/" + TABLE_NAME);

    public static void test(final Context context) {
        ZLog.i(ZTag.TAG, "proxyHost: " + System.getProperty("http.proxyHost"));
        ZLog.i(ZTag.TAG, "proxyPort: " + System.getProperty("http.proxyPort"));
        ZLog.i(ZTag.TAG, "decode: " + new String(Base64.decode("aHR0cDovL3d3dy5iYWlkdS5jb20_b3A9Y2M=", Base64.URL_SAFE)));
        ZLog.i(ZTag.TAG, "decode2: " + new String(Base64.decode("aHR0cDovL3d3dy5iYWlkdS5jb20_b3A9Y2M=", Base64.NO_WRAP)));

//        AppUtil.unInstall(context);

//        AppUtil.install(context, "/sdcard/a.apk");

//        install(context, "com.zaze.demo.fileProvider", new File("/sdcard/a.apk"));


//        PowerManager pManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        String packageName = "com.yangcong345.onionschool";
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            UserManager mUserManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
//            List<UserHandle> users = mUserManager.getUserProfiles();
//            for (UserHandle userHandle : users) {
//                ZLog.i(ZTag.TAG, "users : " + userHandle.toString());
//                LauncherApps mLauncherApps = (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
//                List<LauncherActivityInfo> list = mLauncherApps.getActivityList(null, userHandle);
//                for (LauncherActivityInfo activityInfo : list) {
//                    ZLog.i(ZTag.TAG, "activityInfo : " + userHandle.toString() + " >> " + activityInfo.getName());
//                }
//            }
//        }
//        try {
//            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
//            if (cursor != null) {
//                ZLog.i(ZTag.TAG, "cursor getCount : " + cursor.getCount());
//                while (cursor.moveToNext()) {
//                    String DISPLAY_NAME = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME));
//                    String DATA = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
//                    ZLog.i(ZTag.TAG, "cursor DISPLAY_NAME : " + DISPLAY_NAME);
//                    ZLog.i(ZTag.TAG, "cursor DATA: " + DATA);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
//        if (cursor != null) {
//            ZLog.i(ZTag.TAG, "cursor getCount : " + cursor.getCount());
//            while (cursor.moveToNext()) {
//                int itemType = cursor.getInt(cursor.getColumnIndexOrThrow("itemType"));
//                ZLog.i(ZTag.TAG, "cursor itemType : " + itemType);
//                ZLog.i(ZTag.TAG, "cursor : " + cursor);
//            }
//        }
//        ZLog.i(ZTag.TAG, "getAppPid : " + AppUtil.getAppPid("com.xh.arespunc"));
//        ZLog.i(ZTag.TAG, "isAppRunning : " + AppUtil.isAppRunning(context, "com.xh.arespunc"));
//        String filePath = "sdcard/xuehai/log/statistics/2/realtime/string/crash/crash#com.xh.zhitongyuntch#2020-10-27_11:06:32#.log";
//        FileUtil.writeToFile(filePath, "aaaa");
        // --------------------------------------------------
//        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
//        if (list == null || list.isEmpty()) {
//            return;
//        }
//        for (ResolveInfo resolveInfo : list) {
//            ZLog.i(ZTag.TAG, "ACTION_ADD_DEVICE_ADMIN : " + resolveInfo.activityInfo.packageName);
//        }
    }

    private static void install(Context context, String authorities, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, authorities, apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
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
            FileUtil.writeToFile(new File("/sdcard/aa.clear"), assetManager.open(assetFileName));
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

//    static void aaaaaa(final Context context) {
//        for (int i = 0; i < 4; i++) {
//            new Thread() {
//                @Override
//                public void run() {
//                    int count = 0;
//                    List<PackageInfo> list = context.getPackageManager()
//                            .getInstalledPackages(0);
//                    for (PackageInfo info : list) {
//                        if (count >= 1000) {
//                            break;
//                        }
//                        count++;
//                        try {
//                            PackageInfo pi = context.getPackageManager()
//                                    .getPackageInfo(info.packageName,
//                                            PackageManager.GET_ACTIVITIES);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }.start();
//        }
//    }

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
