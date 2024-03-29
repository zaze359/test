package com.zaze.utils;


import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : RootCmd
 * date : 2015-12-18 - 16:12
 *
 * @author : zaze
 * @version : 1.0
 */
public class ZCommand {
    private static final String TAG = ZTag.TAG + "cmd";
    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    private static Boolean isRoot = null;
    private static final Object object = new Object();

    private static boolean showLog = false;

    public static void setShowLog(boolean showLog) {
        ZCommand.showLog = showLog;
    }

    private static final String[] suPathname = {
            "/data/local/su",
            "/data/local/bin/su",
            "/data/local/xbin/su",
            "/system/xbin/su",
            "/system/bin/su",
            "/system/bin/.ext/su",
            "/system/bin/failsafe/su",
            "/system/sd/xbin/su",
            "/system/usr/we-need-root/su",
            "/sbin/su",
            "/su/bin/su"};
    // --------------------------------------------------

    /**
     * 重启
     */
    public static void reboot() {
        String reboot = "reboot";
        if (isRoot()) {
            execRootCmd(reboot);
        } else {
            execCmd(reboot);
        }
    }

    public static boolean isCommandExists(String cmdName) {
        return execCmdForRes("command -v " + cmdName).isSuccess();
    }

    /**
     * 检查设备是否Root了
     * 记录状态(只需要判断一次)
     *
     * @return true则Root
     */
    public static boolean isRoot() {
        synchronized (object) {
            if (isRoot == null) {
                if (isCommandExists(COMMAND_SU)) {
                    isRoot = true;
                    return isRoot;
                }
                isRoot = false;
                try {
                    for (String path : suPathname) {
                        File file = new File(path);
                        if (file.exists()) {
                            isRoot = true;
                            break;
                        }
                    }
                } catch (Exception ignored) {
                }
            }
            return isRoot;
        }
    }


    // --------------------------------------------------

    /**
     * @param cmd cmd
     * @return 执行命令并且输出结果
     */
    public static CommandResult execRootCmdForRes(String cmd) {
        return execRootCmdForRes(new String[]{cmd});
    }

    /**
     * @param cmdArray cmdArray
     * @return 执行命令并且输出结果
     */
    public static CommandResult execRootCmdForRes(String[] cmdArray) {
        return execCommand(cmdArray, true, true);
    }

    // --------------------------------------------------

    /**
     * 执行命令但不关注结果输出
     *
     * @param cmd cmd
     * @return 成功 : 0
     */
    public static int execRootCmd(String cmd) {
        return execRootCmd(new String[]{cmd});
    }

    /**
     * 执行命令但不关注结果输出
     *
     * @param cmdArray cmdArray
     * @return 成功 : 0
     */
    public static int execRootCmd(String[] cmdArray) {
        return execCommand(cmdArray, true, false).code;
    }
    // --------------------------------------------------

    /**
     * 执行命令但不关注结果输出
     *
     * @param cmd cmd
     * @return 成功 : 0
     */
    public static int execCmd(String cmd) {
        return execCmd(new String[]{cmd});
    }

    /**
     * 执行命令但不关注结果输出
     *
     * @param cmdArray cmdArray
     * @return 成功 : 0
     */
    public static int execCmd(String[] cmdArray) {
        return execCommand(cmdArray, false, false).code;
    }

    // --------------------------------------------------


    /**
     * 执行命令但不关注结果输出
     *
     * @param cmd cmd
     * @return 成功 : 0
     */
    public static CommandResult execCmdForRes(String cmd) {
        return execCmdForRes(new String[]{cmd});
    }

    /**
     * 执行命令但不关注结果输出
     *
     * @param cmdArray cmdArray
     * @return 成功 : 0
     */
    public static CommandResult execCmdForRes(String[] cmdArray) {
        return execCommand(cmdArray, false, true);
    }

    // --------------------------------------------------

    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }
        Process process;
        BufferedReader successReader = null;
        BufferedReader errorReader = null;
        StringBuilder errorBuilder = null;
        DataOutputStream outputStream = null;
        List<String> resultList = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            outputStream = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }
                if (showLog) {
                    ZLog.i(TAG, "command: " + command + "\n");
                }
                // donnot use os.writeBytes(commmand), avoid chinese charset error
                outputStream.write(command.getBytes());
                outputStream.writeBytes(COMMAND_LINE_END);
            }
            outputStream.writeBytes(COMMAND_EXIT);
            outputStream.flush();
            result = process.waitFor();
            if (isNeedResultMsg) {
                resultList = new ArrayList<>();
                errorBuilder = new StringBuilder();
                successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String str;
                while ((str = successReader.readLine()) != null) {
                    resultList.add(str);
                }
                while ((str = errorReader.readLine()) != null) {
                    errorBuilder.append(str);
                }
            }
            process.destroy();
        } catch (Exception e) {
            if (showLog) {
                ZLog.e(TAG, "execCommand error", e);
            }
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (successReader != null) {
                    successReader.close();
                }
                if (errorReader != null) {
                    errorReader.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }
        if (showLog) {
            ZLog.v(TAG, "execCommand result: " + result);
            if(errorBuilder != null) {
                ZLog.e(TAG, "execCommand error: " + errorBuilder);
            }
        }
        return new CommandResult(result,
                errorBuilder == null ? null : errorBuilder.toString(),
                resultList
        );
    }

    // --------------------------------------------------
    public static boolean isSuccess(int result) {
        return result == CommandResult.SUCCESS;
    }

    @Deprecated
    public static boolean isSuccess(CommandResult commandResult) {
        return commandResult.isSuccess();
    }

    // --------------------------------------------------
    public static class CommandResult {
        private static int SUCCESS = 0;


        public int code;
        public String successMsg = "";
        public List<String> successList;
        public String errorMsg;

        public CommandResult(int code) {
            this.code = code;
        }

        public CommandResult(int code, String errorMsg, List<String> list) {
            this.code = code;
            successList = new ArrayList<>();
            StringBuilder builder = new StringBuilder();
            if (list != null && !list.isEmpty()) {
                successList.addAll(list);
                for (String str : list) {
                    builder.append(str);
                }
            }
            this.successMsg = builder.toString();
            this.errorMsg = errorMsg;
        }

        public boolean isSuccess() {
            return code == SUCCESS;
        }

        @Override
        public String toString() {
            return "CommandResult{" +
                    "code=" + code +
                    ", successMsg='" + successMsg + '\'' +
                    ", successList=" + successList +
                    ", errorMsg='" + errorMsg + '\'' +
                    '}';
        }
    }
}
