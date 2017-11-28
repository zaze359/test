package com.zaze.demo.component.socket;

import android.net.wifi.WifiInfo;
import android.support.annotation.NonNull;

import com.zaze.demo.app.MyApplication;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.ZNetUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-27 - 15:07
 */
public class SocketHelper {
    private static int id = 0;
    private ThreadPoolExecutor serverExecutor;
    private SocketFace socketFace;
    private DatagramSocket serverSocket;
    private boolean isRunning = false;

    public SocketHelper() {
        this(null);
    }

    public SocketHelper(SocketFace socketFace) {
        serverExecutor = new ThreadPoolExecutor(1, 1, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r, "socket server thread " + id++);
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                return thread;
            }
        });
        setSocketFace(socketFace);
    }

    public void setSocketFace(SocketFace socketFace) {
        this.socketFace = socketFace;
    }

    public boolean joinGroup(int port) {
        return joinGroup(ZNetUtil.getIpAddress(MyApplication.getInstance()), port);
    }

    public boolean joinGroup(final String host, final int port) {
        ZLog.d(ZTag.TAG_DEBUG, "socket 监听 %s:%s", host, port);
        if (!isRunning) {
            isRunning = true;
            serverExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    ZLog.d(ZTag.TAG_DEBUG, "socket 开始接收数据");
                    try {
                        InetAddress receiveAddress = InetAddress.getByName(host);
                        if (serverSocket != null) {
                            serverSocket.close();
                        }
                        if (receiveAddress.isMulticastAddress()) {
                            serverSocket = new MulticastSocket(port);
                            ((MulticastSocket) serverSocket).joinGroup(receiveAddress);
                        } else {
                            serverSocket = new DatagramSocket(port);
                        }
                        dealMessage(serverSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isRunning = false;
                    ZLog.d(ZTag.TAG_DEBUG, "socket stop");
                }
            });
            return true;
        } else {
            ZLog.d(ZTag.TAG_DEBUG, "socket 正在等待接收数据");
            return false;
        }
    }

    public void stop() {
        isRunning = false;
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    private void dealMessage(DatagramSocket socket) {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (isRunning) {
            try {
                socket.receive(packet);
                SocketMessage socketMessage = new SocketMessage();
                socketMessage.setAddress(packet.getAddress().getHostAddress());
                socketMessage.setSocketAdress(packet.getSocketAddress());
                socketMessage.setPort(packet.getPort());
                socketMessage.setMessage(new String(packet.getData(), 0, packet.getLength(), Charset.defaultCharset()));
                ZLog.d(ZTag.TAG_DEBUG, socketMessage.toString());
                if (socketFace != null) {
                    socketFace.onReceiver(socketMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void send(final String host, final int port, final JSONObject jsonObject) {
        // 发送的数据包，局网内的所有地址都可以收到该数据包
        ThreadManager.getInstance().runInMultiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (serverSocket != null) {
                        if (serverSocket instanceof MulticastSocket) {
                            ((MulticastSocket) serverSocket).setTimeToLive(4);
                        }
                        // 将本机的IP（这里可以写动态获取的IP）地址放到数据包里，其实server端接收到数据包后也能获取到发包方的IP的
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("content", "测试数据");
                        jsonObject.put("time", System.currentTimeMillis());
                        byte[] data = jsonObject.toString().getBytes();
                        WifiInfo wifiInfo = ZNetUtil.getConnectionInfo(MyApplication.getInstance());
                        if (wifiInfo != null) {
                            ZLog.d(ZTag.TAG_DEBUG, "发送(%s:%s) : %s ", host, port, jsonObject.toString(4));
                            InetAddress server = InetAddress.getByName(host);
                            DatagramPacket dataPacket = new DatagramPacket(data, data.length, server, port);
                            serverSocket.send(dataPacket);
                            // socket?.close()
                        } else {
                            ZLog.e(ZTag.TAG_DEBUG, "当前未连接网络！");

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void send(final SocketAddress socketAddress, final JSONObject jsonObject) {
        // 发送的数据包，局网内的所有地址都可以收到该数据包
        ThreadManager.getInstance().runInMultiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (serverSocket != null) {
                        if (serverSocket instanceof MulticastSocket) {
                            ((MulticastSocket) serverSocket).setTimeToLive(4);
                        }
                        // 将本机的IP（这里可以写动态获取的IP）地址放到数据包里，其实server端接收到数据包后也能获取到发包方的IP的
                        byte[] data = jsonObject.toString().getBytes();
                        WifiInfo wifiInfo = ZNetUtil.getConnectionInfo(MyApplication.getInstance());
                        if (wifiInfo != null) {
                            ZLog.d(ZTag.TAG_DEBUG, "发送(%s) : %s ", socketAddress, jsonObject.toString(4));
                            DatagramPacket dataPacket = new DatagramPacket(data, data.length, socketAddress);
                            serverSocket.send(dataPacket);
                            // socket?.close()
                        } else {
                            ZLog.e(ZTag.TAG_DEBUG, "当前未连接网络！");

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // --------------------------------------------------

    /**
     * Socket回调
     */
    public interface SocketFace {

        /**
         * 接收到数据 异步线程
         *
         * @param socketMessage socketMessage
         */
        void onReceiver(SocketMessage socketMessage);
    }
}
