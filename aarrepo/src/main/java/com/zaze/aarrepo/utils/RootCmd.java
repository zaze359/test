package com.zaze.aarrepo.utils;


import com.zaze.aarrepo.commons.log.LogKit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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


    public interface Callback {
        void back(int code, String result);
    }

    public static final int ERROR = -1;

    /**
     * @param cmd
     * @return 执行命令并且输出结果
     */
    public static List<String> execRootCmdForRes(String cmd) {
        List<String> resultList = new ArrayList<>();
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            // 经过Root处理的android系统即有su命令
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line;
            while ((line = dis.readLine()) != null) {
                LogKit.d("result=" + line);
                resultList.add(line);
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultList;
    }


    /**
     * @param cmd
     * @return 执行命令并且输出结果
     */
    public static String execRootCmdForStrRes(String cmd) {
        StringBuilder result = new StringBuilder();
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            // 经过Root处理的android系统即有su命令
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line;
            while ((line = dis.readLine()) != null) {
                LogKit.d("result=" + line);
                result.append(line);
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    /**
     * 执行命令但不关注结果输出
     *
     * @param cmd
     * @return 失败 : = -1; 成功 : != -1
     */
    public static int execRootCmdSilent(String cmd) {
        int result = ERROR;
        DataOutputStream dos = null;
        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 执行命令但不关注结果输出
     *
     * @param cmd
     * @return 失败 : = -1; 成功 : != -1
     */
    public static int execRootCmd(String cmd) {
        int result = ERROR;
        DataOutputStream dos = null;
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static void reboot() {
        RootCmd.execRootCmdSilent("reboot");
    }

//    public static void rmSystemFile(String filePath) {
//        ZLog.i(TAG, "execSystemCmd start ");
//        String mount = "/system/mount-xuehai.sh ";
//        String chmod = "chmod 777 " + filePath;
//        String execute = "rm -r " + filePath;
//        int res = FAILED;
//        ZLog.i(TAG, "start mount...");
//        if (RootCmd.execRootCmdSilent(mount) != FAILED) {
//            ZLog.i(TAG, "start chmod...");
//            if (RootCmd.execRootCmdSilent(chmod) != FAILED) {
//                ZLog.i(TAG, "start uninstall...");
//                res = RootCmd.execRootCmdSilent(execute);
//                if (FAILED != res) {
//                    ZLog.i(TAG, "uninstall success");
//                } else {
//                    ZLog.w(TAG, "uninstall fail");
//                }
//            } else {
//                ZLog.w(TAG, "chmod fail");
//            }
//        } else {
//            ZLog.w(TAG, "mount fail");
//        }
//        ZLog.i(TAG, "execSystemCmd end ");
//    }


}
