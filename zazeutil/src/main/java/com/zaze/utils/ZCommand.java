package com.zaze.utils;


import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    private static int SUCCESS = 0;
    private static Boolean isRoot = null;
    private static final Object object = new Object();

    private static boolean showLog = false;

    public static void setShowLog(boolean showLog) {
        ZCommand.showLog = showLog;
    }
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
        return ZCommand.isSuccess(execCmdForRes("command -v " + cmdName));
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
                Process process = null;
                DataOutputStream outputStream = null;
                try {
                    process = Runtime.getRuntime().exec("su");
                    outputStream = new DataOutputStream(process.getOutputStream());
                    outputStream.writeBytes("exit\n");
                    outputStream.flush();
                    int exitValue = process.waitFor();
                    if (exitValue == SUCCESS) {
                        if (showLog) {
                            ZLog.i(ZTag.TAG_CMD, "设备已Root");
                        }
                        isRoot = true;
                    }
                } catch (Exception e) {
                    ZLog.e(ZTag.TAG_CMD, "设备未Root");
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (process != null) {
                        process.destroy();
                    }
                }
                if (isRoot == null) {
                    isRoot = false;
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
                    ZLog.i(ZTag.TAG_CMD, "command ： " + command + "\n");
                }
                // donnot use os.writeBytes(commmand), avoid chinese charset error
                outputStream.write(command.getBytes());
                outputStream.writeBytes(COMMAND_LINE_END);
                outputStream.flush();
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
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
        return new CommandResult(result,
                errorBuilder == null ? null : errorBuilder.toString(),
                resultList
        );
    }
    // --------------------------------------------------

    public static boolean isSuccess(int result) {
        return result == SUCCESS;
    }

    public static boolean isSuccess(CommandResult commandResult) {
        return isSuccess(commandResult.code);
    }

    // --------------------------------------------------
    public static class CommandResult {
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
