package com.zaze.demo.component.checknet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.zaze.aarrepo.utils.StringUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-06-20 - 13:42
 */
public class NetworkUtil {

//    其中wifiInfo有以下的方法：
//    wifiinfo.getBSSID()；
//    wifiinfo.getSSID()；
//    wifiinfo.getIpAddress()；获取IP地址。
//    wifiinfo.getMacAddress()；获取MAC地址。
//    wifiinfo.getNetworkId()；获取网络ID。
//    wifiinfo.getLinkSpeed()；获取连接速度，可以让用户获知这一信息。
//    wifiinfo.getRssi()；获取RSSI，RSSI就是接受信号强度指示。在这可以直 接和华为提供的Wi-Fi信号阈值进行比较来提供给用户，让用户对网络或地理位置做出调整来获得最好的连接效果。
//    这里得到信号强度就靠wifiinfo.getRssi()；这个方法。得到的值是一个0到-100的区间值，是一个int型数据，其中0到-50表示信号最好，-50到-70表示信号偏差，小于-70表示最差，有可能连接不上或者掉线，一般Wifi已断则值为-200。


    private static NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    private static WifiManager getWifiManager(Context context) {
        return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public static String getWifiMacAddress(Context context) {
        try {
            String mac = getWifiManager(context).getConnectionInfo().getMacAddress();
            if (TextUtils.isEmpty(mac)) {
                return null;
            }
            return mac;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // --------------------------------------------------
//    public static String getIPAddress(Context context) {
//        NetworkInfo info = getNetworkInfo(context);
//        if (info != null && info.isConnected()) {
//            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
//                try {
//                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
//                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
//                        NetworkInterface intf = en.nextElement();
//                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
//                            InetAddress inetAddress = enumIpAddr.nextElement();
//                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
//                                return inetAddress.getHostAddress();
//                            }
//                        }
//                    }
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                }
//            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
//                WifiManager wifiManager = getWifiManager(context);
//                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//                return intToInetAddress(wifiInfo.getIpAddress()).getHostAddress();//得到IPV4地址
//            }
//        } else {
//            //当前无网络连接,请在设置中打开网络
//        }
//        return null;
//    }


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
    public static String getDNS(Context context) {
        DhcpInfo dhcpInfo = getDhcpInfo(context);
        String dns1 = intToInetAddress(dhcpInfo.dns1).getHostAddress();
        String dns2 = intToInetAddress(dhcpInfo.dns2).getHostAddress();
        return StringUtil.format("%s,%s", dns1, dns2);
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

}
