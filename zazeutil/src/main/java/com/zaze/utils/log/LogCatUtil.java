package com.zaze.utils.log;

import android.text.TextUtils;

import com.zaze.utils.ZCommand;
import com.zaze.utils.ZFileUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.zaze.utils.ZCommand.COMMAND_LINE_END;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-10-22 - 21:57
 */
public class LogCatUtil {
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
     * @param filePath filePath
     * @return boolean
     */
    public static boolean startCatchLog(String command, String filePath, long maxSize) {
        ZLog.i(ZTag.TAG_CDM, "command : " + command);
        if (isRunning) {
            stopCatchLog();
        }
        isRunning = true;
        int result = -1;
        if (TextUtils.isEmpty(command) && !ZCommand.isRoot()) {
            return false;
        }
        Process process = null;
        BufferedReader successReader = null;
        try {
            process = Runtime.getRuntime().exec("su");
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.write(command.getBytes());
            outputStream.writeBytes(COMMAND_LINE_END);
            outputStream.flush();
            String line;
            while (isRunning) {
                if ((line = successReader.readLine()) != null) {
                    ZFileUtil.INSTANCE.writeToFile(filePath, line + "\n", maxSize);
                }
            }
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
