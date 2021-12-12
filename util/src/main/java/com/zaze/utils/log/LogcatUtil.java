package com.zaze.utils.log;

import static com.zaze.utils.ZCommand.COMMAND_LINE_END;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import com.zaze.utils.FileUtil;
import com.zaze.utils.ZCommand;
import com.zaze.utils.ZStringUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Description : Root情况下可以抓取其他应用的日志， 未Root时将只获得调用应用自身的Log
 *
 * @author : ZAZE
 * @version : 2017-10-22 - 21:57
 */
public class LogcatUtil {
    private static boolean isRunning = false;

    public static boolean isRunning() {
        return isRunning;
    }

    public static void stopCatchLog() {
        isRunning = false;
    }


    /**
     * logcat *:E
     */
    public static void startCatchLog(Context context) {
        try {
            startCatchLog(
                    ZStringUtil.format(
                            "%s/catch#%s#.log",
                            context.getExternalCacheDir().getAbsolutePath() + "/zaze/log",
                            context.getPackageName()
                    ), 10L << 20
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * logcat *:E
     *
     * @param savePath savePath
     * @param maxSize  maxSize
     */
    public static void startCatchLog(String savePath, long maxSize) {
//        return startCatchLog("logcat -v time process |grep " + android.os.Process.myPid(), savePath, maxSize);
        startCatchLog("logcat -v time", savePath, maxSize);
    }


    /**
     * logcat *:E
     *
     * @param savePath savePath
     * @return boolean
     */
    public static void startCatchLog(String command, String savePath, long maxSize) {
        ZLog.i(ZTag.TAG_DEBUG, "command : " + command);
        if (isRunning) {
            return;
        }
        isRunning = true;
        int result = -1;
        if (TextUtils.isEmpty(command)) {
            return;
        }
        Process process = null;
        BufferedReader successReader = null;
        try {
            process = Runtime.getRuntime().exec(ZCommand.isRoot() ? "su" : "sh");
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.write(command.getBytes());
            outputStream.writeBytes(COMMAND_LINE_END);
            outputStream.flush();
            String line;
            File saveFile = new File(savePath);
            while (isRunning) {
                if ((line = successReader.readLine()) != null) {
                    isRunning = FileUtil.writeToFile(saveFile, line + "\n", maxSize);
                    SystemClock.sleep(300L);
                }
            }
            isRunning = false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (process != null) {
                    process.destroy();
                }
                if (successReader != null) {
                    successReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
