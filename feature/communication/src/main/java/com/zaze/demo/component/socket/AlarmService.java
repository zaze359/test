package com.zaze.demo.component.socket;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.zaze.demo.component.socket.client.ui.ClientFragment;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-03-13 - 15:05
 */
public class AlarmService extends Service {
    private AlarmManager alarmManager;
    public static boolean runAlarm = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (runAlarm) {
            ClientFragment.send();
            setAlarm();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void setAlarm() {
        if (alarmManager == null) {
            alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        }
        if (alarmManager != null) {
            PendingIntent pi = PendingIntent.getService(
                    this,
                    0,
                    new Intent(this, AlarmService.class),
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000L, pi);
        }
    }
}
