package com.zaze.aarrepo.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.zaze.aarrepo.commons.log.LogKit;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

/**
 * Description : 设备工具类
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class DeviceUtil {

    /**
     * @return 检查设备是否Root了
     */
    public static boolean checksRoot() {
        Process process = null;
        DataOutputStream outputStream = null;
        try {
            process = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                LogKit.i("设备已Root");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        LogKit.i("设备未Root");
        return false;
    }

    /**
     * @param context
     * @return 机器码
     */
    public static String getMachineCode(Context context) {
        UUID uuid;
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            if (!"9774d56d682e549c".equals(androidId)) {
                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
            } else {
                final String deviceId = ((TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE))
                        .getDeviceId();
                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId
                        .getBytes("utf8")) : UUID.randomUUID();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return uuid.toString();
    }

    /**
     * @return 机器型号
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * @return 系统版本
     */
    public static String getSysVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * @return 本地mac 地址
     */
    public static String getMacAddress() {
 /*获取mac地址有一点需要注意的就是android 6.0版本后，以下注释方法不再适用，不管任何手机都会返回"02:00:00:00:00:00"这个默认的mac地址，这是googel官方为了加强权限管理而禁用了getSYstemService(Context.WIFI_SERVICE)方法来获得mac地址。*/
        //        String macAddress= "";
//        WifiManager wifiManager = (WifiManager) MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        macAddress = wifiInfo.getMacAddress();
//        return macAddress;

        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }

    /**
     * @return sdcard空间大小
     */
    public static long getSDTotalSpace() {
        File sd = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(sd.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }

    /**
     * @return sdcard 剩余空间大小
     */
    public static long getSDFreeSpace() {
        File sd = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(sd.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * ROM
     *
     * @return 获取机身总大小
     */
    public static long getRomTotalSpace() {
        File rom = Environment.getDataDirectory();
        StatFs stat = new StatFs(rom.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }

    /**
     * ROM
     *
     * @return 获取机身剩余
     */
    public static long getRomFreeSpace() {
        File rom = Environment.getDataDirectory();
        StatFs stat = new StatFs(rom.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getAvailableBlocks();
        return blockSize * totalBlocks;
    }


    /**
     * RAM
     * java 虚拟机 最大内存
     *
     * @return
     */
    public static long getVMMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * RAM
     * java 虚拟机 当前 从机器内存中取过来的 内存的 中的空闲内存
     *
     * @return
     */
    public static long getVMFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * RAM
     * java 虚拟机 当前 从机器内存中取过来 的 总内存(包括使用了的和 freeMemory)
     *
     * @return
     */
    public static long getVMTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @param context
     * 测试打印
     */
//    public static void printPhoneMsg(Context context) {
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        StringBuilder sb = new StringBuilder();
//        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
//        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
//        sb.append("\nLine1Number = " + tm.getLine1Number());
//        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
//        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
//        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
//        sb.append("\nNetworkType = " + tm.getNetworkType());
//        sb.append("\nPhoneType = " + tm.getPhoneType());
//        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
//        sb.append("\nSimOperator = " + tm.getSimOperator());
//        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
//        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
//        sb.append("\nSimState = " + tm.getSimState());
//        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
//        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
//        sb.append("\nProduct Model: " + android.os.Build.MODEL + ","
//                + android.os.Build.VERSION.SDK + ","
//                + android.os.Build.VERSION.RELEASE);
//        LogDevelopmentKit.e("zaze 0720 : " + sb.toString());
//    }
}
