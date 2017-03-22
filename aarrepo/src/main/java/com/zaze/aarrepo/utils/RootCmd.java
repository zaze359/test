package com.zaze.aarrepo.utils;


import com.zaze.aarrepo.commons.log.LogKit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * date : 2015-12-18 - 16:12
 *
 * @author : zaze
 * @version : 1.0
 */
public class RootCmd {
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


    /**
     * 执行命令但不关注结果输出
     *
     * @param cmd
     * @return 失败 : = -1; 成功 : != -1
     */
//    public static int execRootCmd(String cmd) {
//        int result = ERROR;
//        DataOutputStream dos = null;
//        try {
//            Process p = Runtime.getRuntime().exec("su");
//            dos = new DataOutputStream(p.getOutputStream());
//            dos.writeBytes(cmd + "\n");
//
//            dos.flush();
//            dos.writeBytes("exit\n");
//            dos.flush();
//            
//            final InputStream is1 = p.getInputStream();
//            //获取进城的错误流  
//            final InputStream is2 = p.getErrorStream();
//            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流  
//            new Thread() {
//                public void run() {
//                    BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
//                    try {
//                        String line1 = null;
//                        while ((line1 = br1.readLine()) != null) {
//                            if (line1 != null){}
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    finally{
//                        try {
//                            is1.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }.start();
//
//            new Thread() {
//                public void  run() {
//                    BufferedReader br2 = new  BufferedReader(new InputStreamReader(is2));
//                    try {
//                        String line2 = null ;
//                        while ((line2 = br2.readLine()) !=  null ) {
//                            if (line2 != null){}
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    finally{
//                        try {
//                            is2.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }.start();
//            p.waitFor();
//            p.destroy();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if(dos != null) {
//                    dos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }

}
