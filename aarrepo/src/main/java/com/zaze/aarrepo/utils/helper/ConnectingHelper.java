package com.zaze.aarrepo.utils.helper;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.List;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class ConnectingHelper {
    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            //如果仅仅是用来判断网络连接
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable();
        }
    }

    /**
     * 是否是wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static boolean is3rd(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    public static boolean isWifiEnabled(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager mgrConn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null
                && mgrConn.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED)
                || mgrTel.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    public static boolean isGpsEnabled(Context context) {
        if (context == null) {
            return false;
        }
        LocationManager lm = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = lm.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }
}
