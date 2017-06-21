package com.zaze.aarrepo.utils;


import com.zaze.aarrepo.commons.log.ZLog;

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
public class RootCmd {
    private static final String TAG = "RootCmd[执行命令]";

    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";


    public static int ERROR = -1;

    // --------------------------------------------------
    public static void reboot() {
        RootCmd.execRootCmd("reboot");
    }

    /**
     * @return 检查设备是否Root了
     */
    public static boolean checkRoot() {
        Process process = null;
        DataOutputStream outputStream = null;
        try {
            process = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                ZLog.i(ZTag.TAG_DEBUG, "设备已Root");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        ZLog.e(ZTag.TAG_ERROR, "设备未Root");
        return false;
    }


    // --------------------------------------------------

    /**
     * @param cmd
     * @return 执行命令并且输出结果
     */
    public static CommandResult execRootCmdForRes(String cmd) {
        return execRootCmdForRes(new String[]{cmd});
    }

    /**
     * @param cmdArray
     * @return 执行命令并且输出结果
     */
    public static CommandResult execRootCmdForRes(String[] cmdArray) {
        return execCommand(cmdArray, true, true);
    }

    // --------------------------------------------------

    /**
     * 执行命令但不关注结果输出
     *
     * @param cmd
     * @return 失败 : = -1; 成功 : != -1
     */
    public static int execRootCmd(String cmd) {
        return execRootCmd(new String[]{cmd});
    }

    /**
     * 执行命令但不关注结果输出
     *
     * @param cmdArray
     * @return 失败 : = -1; 成功 : != -1
     */
    public static int execRootCmd(String[] cmdArray) {
        return execCommand(cmdArray, true, false).result;
    }

    // --------------------------------------------------
    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder errorMsg = null;
        DataOutputStream os = null;
        List<String> resultList = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }
                // donnot use os.writeBytes(commmand), avoid chinese charset error
                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();
            result = process.waitFor();
            if (isNeedResultMsg) {
                resultList = new ArrayList<>();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String str;
                while ((str = successResult.readLine()) != null) {
                    resultList.add(str);
                }
                while ((str = errorResult.readLine()) != null) {
                    errorMsg.append(str);
                }
            }
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new CommandResult(result,
                errorMsg == null ? null : errorMsg.toString(),
                resultList
        );
    }

    public static boolean isSuccess(int result) {
        return result != ERROR;
    }

    // --------------------------------------------------
    public static class CommandResult {
        public int result;
        public String successMsg = "";
        public List<String> msgList;
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String errorMsg, List<String> list) {
            this.result = result;
            msgList = new ArrayList<>();
            StringBuilder builder = new StringBuilder();
            if (list != null && !list.isEmpty()) {
                msgList.addAll(list);
                for (String str : list) {
                    builder.append(str);
                }
            }
            this.successMsg = builder.toString();
            this.errorMsg = errorMsg;
        }
    }


}
