package com.zaze.demo.debug.wifi;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-27 - 21:21
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class WifiJob extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        StringBuilder sb = new StringBuilder();
        sb.append("onStartJob Wifi has changed:\n");
        Network network = getNetWork(params);
        sb.append("network: ").append(network);
        ZLog.i(ZTag.TAG_DEBUG, sb.toString());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        StringBuilder sb = new StringBuilder();
        sb.append("onStopJob Wifi has changed:\n");
        Network network = getNetWork(params);
        sb.append("network: ").append(network);
        ZLog.i(ZTag.TAG_DEBUG, sb.toString());
        return false;
    }

    private Network getNetWork(JobParameters params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return params.getNetwork();
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                return connectivityManager.getActiveNetwork();
            }
        }
        return null;
    }
}
