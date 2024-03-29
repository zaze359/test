package com.zaze.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.text.TextUtils;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-06-20 - 13:42
 */
@SuppressLint("MissingPermission")
public class NetUtil {

    //    其中wifiInfo有以下的方法：
//    wifiinfo.getLinkSpeed()；获取连接速度，可以让用户获知这一信息。
//    wifiinfo.getRssi()；获取RSSI，RSSI就是接受信号强度指示。在这可以直 接和华为提供的Wi-Fi信号阈值进行比较来提供给用户，让用户对网络或地理位置做出调整来获得最好的连接效果。
//    这里得到信号强度就靠wifiinfo.getRssi()；这个方法。得到的值是一个0到-100的区间值，是一个int型数据，其中0到-50表示信号最好，-50到-70表示信号偏差，小于-70表示最差，有可能连接不上或者掉线，一般Wifi已断则值为-200。

    public static final String WIFI = "wifi";
    public static final String MOBILE = "mobile";

    private static WifiManager mWifiManager;
    private static ConnectivityManager connectivityManager;

    private static final String MARK = "\"";


    public static NetworkInfo getNetworkInfo(Context context) {
        return getConnectivityManager(context).getActiveNetworkInfo();
    }

    public static WifiManager getWifiManager(Context context) {
        if (mWifiManager == null) {
            mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }
        return mWifiManager;
    }

    public static ConnectivityManager getConnectivityManager(Context context) {
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        return connectivityManager;
    }
    // --------------------------------------------------


    public static boolean isWifiEnabled(Context context) {
        return getWifiManager(context).isWifiEnabled();
    }

    /**
     * 判断网络是否可用
     *
     * @param context context
     * @return true : available
     */
    public static boolean isAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        //如果仅仅是用来判断网络连接
        NetworkInfo networkInfo = getConnectivityManager(context).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    // --------------------------------------------------

    public static boolean isSSIDEquals(String ssId1, String ssId2) {
        if (TextUtils.isEmpty(ssId1) || TextUtils.isEmpty(ssId2)) {
            return false;
        }
        if (TextUtils.equals(ssId1, ssId2)) {
            return true;
        } else {
            if (ssId1.startsWith(MARK) && ssId1.endsWith(MARK)) {
                return TextUtils.equals(ssId1, MARK + ssId2 + MARK);
            } else if (ssId2.startsWith(MARK) && ssId2.endsWith(MARK)) {
                return TextUtils.equals(MARK + ssId1 + MARK, ssId2);
            } else {
                return false;
            }
        }
    }

    public static NetworkInfo getWifiInfo(Context context) {
        return getConnectivityManager(context).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    }

    public static String getNetwork(Context context) {
        NetworkInfo activeInfo = getConnectivityManager(context).getActiveNetworkInfo();
        if (activeInfo == null) {
            ZLog.e(ZTag.TAG_DEBUG, "无网络连接");
            return "";
        } else {
            String wifi = null;
            switch (activeInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    ZLog.i(ZTag.TAG_DEBUG, "当前连接wifi");
                    wifi = WIFI;
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    ZLog.i(ZTag.TAG_DEBUG, "当前使用数据流量");
                    wifi = MOBILE;
                    break;
            }
            if (activeInfo.isAvailable()) {
                return wifi;
            } else {
                return "";
            }
        }
    }

    // --------------------------------------------------
    public static String getWLANMacAddress() {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            return getMacAddress(networkInterface);
        } catch (Exception var8) {
            var8.printStackTrace();
            return "02:00:00:00:00:02";
        }
    }

    public static String getMacAddress(NetworkInterface networkInterface) {
        StringBuilder buf = new StringBuilder();
        try {
            if (networkInterface == null) {
                return null;
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            return buf.toString();
        } catch (Exception var8) {
            var8.printStackTrace();
        }
        return null;
    }

    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getLocalMacAddressFromIp() {
        String strMacAddr = null;
        try {
            //获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
        }
        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                        break;
                    } else {
                        ip = null;
                    }
                }
                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }


    // --------------------------------------------------

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
    // --------------------------------------------------

    public static void connect(Context context, String ssId, String password, String capabilities) {
        if (!isWifiEnabled(context)) {
            ZLog.e(ZTag.TAG_DEBUG, "当前wifi不可用");
            return;
        }
        // 开启wifi功能需要一段时间(一般需要1-3秒左右)，所以要等到wifi
        // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
        while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            // 为了避免程序一直while循环，让它睡个100毫秒在检测……
            SystemClock.sleep(100);
        }

        WifiConfiguration wifiConfiguration = createWifiInfo(ssId, password, capabilities);

    }

    private static WifiConfiguration createWifiInfo(String ssId, String password, String capabilities) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssId + "\"";
        if (capabilities.toUpperCase().contains("WPA")) {
            // config.preSharedKey = "\"" + Password + "\"";
            // config.hiddenSSID = true;
            // config.allowedAuthAlgorithms
            // .set(WifiConfiguration.AuthAlgorithm.OPEN);
            // config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            // config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            // config.allowedPairwiseCiphers
            // .set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            // config.status = WifiConfiguration.Status.ENABLED;

            // 修改之后配置
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
        } else if (capabilities.toUpperCase().contains("WEP")) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        return config;
    }

    // --------------------------------------------------
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
            appendDesc(descBuilder, "WPA");
        }
        if (isWPA2) {
            appendDesc(descBuilder, "WPA2");
        }
        if (isESS) {
            appendDesc(descBuilder, "ESS");
        }
        return descBuilder.toString();
    }

    private static void appendDesc(StringBuilder sb, String desc) {
        if (sb.length() > 0) {
            sb.append("/").append(desc);
        } else {
            sb.append(desc);
        }
    }
    // --------------------------------------------------
}
