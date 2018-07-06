package com.zaze.utils.log;

import android.content.Context;
import android.text.TextUtils;

import com.zaze.utils.FileUtil;
import com.zaze.utils.ZCommand;
import com.zaze.utils.ZStringUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.zaze.utils.ZCommand.COMMAND_LINE_END;

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
     *
     * @return boolean
     */
    public static boolean startCatchLog(Context context) {
        return startCatchLog(ZStringUtil.format("%s/catch#%s#.log", FileUtil.getSDCardRoot() + "/xuehai/log", context.getPackageName()));
    }

    /**
     * logcat *:E
     *
     * @param savePath savePath
     * @return boolean
     */
    public static boolean startCatchLog(String savePath) {
        return startCatchLog(savePath, 512L << 10);
    }

    /**
     * logcat *:E
     *
     * @param savePath savePath
     * @param maxSize  maxSize
     * @return maxSize
     */
    public static boolean startCatchLog(String savePath, long maxSize) {
        return startCatchLog("logcat -v time process |grep " + android.os.Process.myPid(), savePath, maxSize);
    }


    /**
     * logcat *:E
     *
     * @param savePath savePath
     * @return boolean
     */
    public static boolean startCatchLog(String command, String savePath, long maxSize) {
        ZLog.i(ZTag.TAG_CMD, "command : " + command);
        if (isRunning) {
            return true;
        }
        isRunning = true;
        int result = -1;
        if (TextUtils.isEmpty(command)) {
            return false;
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
            while (isRunning) {
                if ((line = successReader.readLine()) != null) {
                    FileUtil.writeToFile(savePath, line + "\n", maxSize);
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
        return ZCommand.isSuccess(result);
    }
}
