package com.zaze.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-06-20 - 13:42
 */
public class ZNetUtil {

//    其中wifiInfo有以下的方法：
//    wifiinfo.getBSSID()；
//    wifiinfo.getSSID()；
//    wifiinfo.getIpAddress()；获取IP地址。
//    wifiinfo.getMacAddress()；获取MAC地址。
//    wifiinfo.getNetworkId()；获取网络ID。
//    wifiinfo.getLinkSpeed()；获取连接速度，可以让用户获知这一信息。
//    wifiinfo.getRssi()；获取RSSI，RSSI就是接受信号强度指示。在这可以直 接和华为提供的Wi-Fi信号阈值进行比较来提供给用户，让用户对网络或地理位置做出调整来获得最好的连接效果。
//    这里得到信号强度就靠wifiinfo.getRssi()；这个方法。得到的值是一个0到-100的区间值，是一个int型数据，其中0到-50表示信号最好，-50到-70表示信号偏差，小于-70表示最差，有可能连接不上或者掉线，一般Wifi已断则值为-200。


    public static NetworkInfo getNetworkInfo(Context context) {
        return getConnectivityManager(context).getActiveNetworkInfo();
    }

    public static WifiManager getWifiManager(Context context) {
        return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
    // --------------------------------------------------

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isAvailable(Context context) {
        if (context == null) {
            return false;
        }
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = getConnectivityManager(context);
        if (connectivityManager == null) {
            return false;
        } else {
            //如果仅仅是用来判断网络连接
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable();
        }
    }

    // --------------------------------------------------
    public static String getNetwork(Context context) {
        ConnectivityManager connectivityManager = getConnectivityManager(context);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();
        if (activeInfo == null) {
            ZLog.e(ZTag.TAG_DEBUG, "无网络连接");
            return "";
        } else {
            if (wifiInfo != null && mobileInfo != null) {
                if (!wifiInfo.isConnected() && mobileInfo.isConnected()) {
                    ZLog.i(ZTag.TAG_DEBUG, "当前使用数据流量");
                    return "4g";
                } else {
                    ZLog.i(ZTag.TAG_DEBUG, "当前连接wifi");
                    return "wifi";
                }
            } else {
                return "unKnow";
            }
        }
    }

    // --------------------------------------------------
    public static String getWLANMacAddress() {
        /*获取mac地址有一点需要注意的就是android 6.0版本后，以下注释方法不再适用，不管任何手机都会返回"02:00:00:00:00:00"这个默认的mac地址，这是googel官方为了加强权限管理而禁用了getSYstemService(Context.WIFI_SERVICE)方法来获得mac地址。*/
        String macAddress;
        StringBuilder buf = new StringBuilder();
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
     * @param context context
     * @return WifiInfo
     */
    public static WifiInfo getConnectionInfo(Context context) {
        return getWifiManager(context).getConnectionInfo();
    }


    /**
     * @param context context
     * @return ipAddress
     */
    public static String getIpAddress(Context context) {
        return intToInetAddress(getDhcpInfo(context).ipAddress).getHostAddress();
    }

    /**
     * @param context context
     * @return dns
     */
    public static List<String> getDNS(Context context) {
        DhcpInfo dhcpInfo = getDhcpInfo(context);
        List<String> list = new ArrayList<>();
        list.add(intToInetAddress(dhcpInfo.dns1).getHostAddress());
        list.add(intToInetAddress(dhcpInfo.dns2).getHostAddress());
        return list;
    }

    /**
     * @param context context
     * @return Gateway
     */
    public static String getGateway(Context context) {
        DhcpInfo dhcpInfo = getDhcpInfo(context);
        return intToInetAddress(dhcpInfo.gateway).getHostAddress();
    }

    /**
     * ipAddress
     * gateway
     * netmask
     * dns1
     * dns2
     * serverAddress
     * leaseDuration
     *
     * @param context context
     * @return DhcpInfo
     */
    public static DhcpInfo getDhcpInfo(Context context) {
        return getWifiManager(context).getDhcpInfo();
    }

    // --------------------------------------------------
    public static InetAddress intToInetAddress(int hostAddress) {
        byte[] addressBytes = {(byte) (0xff & hostAddress),
                (byte) (0xff & (hostAddress >> 8)),
                (byte) (0xff & (hostAddress >> 16)),
                (byte) (0xff & (hostAddress >> 24))};

        try {
            return InetAddress.getByAddress(addressBytes);
        } catch (UnknownHostException e) {
            throw new AssertionError();
        }
    }

    // --------------------------------------------------
//    public static void getProviders(Context context) {
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String IMSI = telephonyManager.getSubscriberId();
//        ZLog.i(ZTag.TAG_DEBUG, IMSI);
//    }

    public static String formatWifiDesc(ScanResult scanResult) {
        String descOri = scanResult.capabilities.toUpperCase();
        StringBuilder descBuilder = new StringBuilder();
        boolean isWPA = descOri.contains("WPA-PSK");
        boolean isWPA2 = descOri.contains("WPA2-PSK");
        boolean isESS = descOri.contains("ESS");
        if (isWPA) {
//            appendDesc(descBuilder, "WPA");
        }
        if (isWPA2) {
//            appendDesc(descBuilder, "WPA2");
        }
        if (isESS) {
//            appendDesc(descBuilder, "ESS");
        }
        return descBuilder.toString();
    }


    // --------------------------------------------------

    public static void analyzeNetworkState() {
        StringBuffer buffer = ZFileUtil.INSTANCE.readFromFile("/proc/net/xt_qtaguid/stats");
//        ZFileUtil.INSTANCE.writeToFile("/sdcard/aaa.txt", "aa\nbb\n", true);
//        StringBuffer buffer = ZFileUtil.INSTANCE.readFromFile("/sdcard/aaa.txt");
        String lineSplit = "\n";
        String valueSplit = " ";
        String[] linesArray = buffer.toString().split(lineSplit);
        if (linesArray.length > 0) {
            String[] keyArray = linesArray[0].split(valueSplit);

            for (int i = 1; i < linesArray.length; i++) {
                String[] valueArray = linesArray[i].split(valueSplit);
                for (String value : valueArray) {
                    ZLog.i(ZTag.TAG_DEBUG, value);
                }
            }
        }

    }
}
