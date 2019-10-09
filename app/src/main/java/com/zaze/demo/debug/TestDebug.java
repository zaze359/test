package com.zaze.demo.debug;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

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

    private static byte[] bytes;

    private static final Pattern sTrimPattern =
            Pattern.compile("^[\\s|\\p{javaSpaceChar}]*(.*)[\\s|\\p{javaSpaceChar}]*$");

    public static void test(Context context) {
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            windowMgr.getDefaultDisplay().getRealMetrics(displayMetrics);
//        }
//        ZLog.i(ZTag.TAG_DEBUG, "displayMetrics : " + displayMetrics);

//        StorageLoader.query();

//        AppUtil.isAppRunning(context, "com.xh.arespunc");

//        final File systemDir = new File(Environment.getDataDirectory(), "system");
//        ZLog.i(ZTag.TAG_DEBUG, "systemDir : " + systemDir.getAbsolutePath());

//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//            try {
//                Method method = AppOpsManager.class.getDeclaredMethod("setMode", String.class,int.class,String.class,int.class);
//                method.invoke(appOpsManager,"OP_POST_NOTIFICATION", 0,"", AppOpsManager.MODE_ALLOWED);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

//               mPolicyFile = new AtomicFile(new File(systemDir, "notification_policy.xml"));

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
//        JSONArray allArray = new JSONArray();
//        HashSet<String> apkSet = new HashSet<>();
//        File fileDir = new File("/sdcard/zaze/all/");
//        if (fileDir.exists() && fileDir.isDirectory()) {
//            for (File file : fileDir.listFiles()) {
//                StringBuffer buffer = FileUtil.readFromFile(file.getAbsolutePath());
//                try {
//                    JSONArray jsonArray = new JSONArray(buffer.toString());
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        String packageName = jsonObject.optString("packageName", "");
//                        if (!apkSet.contains(packageName) && !TextUtils.isEmpty(packageName)) {
//                            apkSet.add(packageName);
//                            allArray.put(jsonObject);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        FileUtil.writeToFile("/sdcard/zaze/all/all.json", allArray.toString(), false);

        new Thread() {
            public void run() {
                download("http://xhfs5.oss-cn-hangzhou.aliyuncs.com/CA102001/92d2b6a05b9a4df097749c2e68e68181.png", "/sdcard/aa.png");
            }
        }.start();
//        aaaaaa(context);
    }


    private static final OkHttpClient client = new OkHttpClient();

    //下载
    private static void download(final String url, final String savePath) {
        final long startTime = System.currentTimeMillis();
        Log.i("DownloadUtil", "startTime=" + startTime);
        Log.d("DownloadUtil", "download: url = " + url + " savePath = " + savePath);
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
                Log.i("DownloadUtil", "download failed e = " + e);
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
                    Log.i("DownloadUtil", "download success");
                    Log.i("DownloadUtil", "totalTime=" + (System.currentTimeMillis() - startTime));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("DownloadUtil", "download failed e = " + e);
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
                    for (PackageInfo info : list) {
                        if (count >= 1000) {
                            break;
                        }
                        try {
                            PackageInfo pi = context.getPackageManager()
                                    .getPackageInfo(info.packageName,
                                            PackageManager.GET_ACTIVITIES);
                            Log.e("test", "threadid:" + Thread.currentThread().getId()
                                    + ",i:" + count++);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
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
