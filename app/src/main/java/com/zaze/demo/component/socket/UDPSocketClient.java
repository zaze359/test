package com.zaze.demo.component.socket;

import android.net.wifi.WifiInfo;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.zaze.demo.app.MyApplication;
import com.zaze.utils.JsonUtil;
import com.zaze.utils.NetUtil;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.ZStringUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
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
public class UDPSocketClient extends BaseSocketClient {
    private static int id = 0;
    private ThreadPoolExecutor serverExecutor;
    private DatagramSocket serverSocket;
    private boolean isRunning = false;
    private final Object objLock = new Object();

    public UDPSocketClient(int port, BaseSocketFace socketFace) {
        this(null, port, socketFace);
    }

    public UDPSocketClient(String host, int port, BaseSocketFace socketFace) {
        super(host, port, socketFace);

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
    }

    @Override
    public boolean receive() {
        ZLog.d(ZTag.TAG_DEBUG, ZStringUtil.format("socket 监听 %s:%s", getHost(), getPort()));
        if (!isRunning) {
            isRunning = true;
            serverExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    ZLog.d(ZTag.TAG_DEBUG, "socket 开始接收数据");
                    try {
                        if (!TextUtils.isEmpty(getHost()) && InetAddress.getByName(getHost()).isMulticastAddress()) {
                            serverSocket = new MulticastSocket(getPort());
                            ((MulticastSocket) serverSocket).joinGroup(InetAddress.getByName(getHost()));
                        } else {
                            serverSocket = new DatagramSocket(getPort());
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

    private void dealMessage(DatagramSocket socket) {
        byte[] buffer = new byte[getMaxSize()];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (isRunning) {
            try {
                socket.receive(packet);
                SocketMessage socketMessage = JsonUtil.parseJson(new String(packet.getData(), 0, packet.getLength(), Charset.defaultCharset()), SocketMessage.class);
                socketMessage.setAddress(packet.getAddress().getHostAddress());
                socketMessage.setPort(packet.getPort());
                socketMessage.setReceiverTime(System.currentTimeMillis());
                onReceiver(socketMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void send(final String host, final int port, final SocketMessage message) {
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
                        String json = JsonUtil.objToJson(message);
                        byte[] data = json.getBytes();
                        WifiInfo wifiInfo = NetUtil.getConnectionInfo(MyApplication.getInstance());
                        if (wifiInfo != null) {
                            ZLog.d(ZTag.TAG_DEBUG, ZStringUtil.format("发送(%s:%s) : %s ", host, port, json));
                            DatagramPacket dataPacket = new DatagramPacket(data, data.length, new InetSocketAddress(host, port));
                            serverSocket.send(dataPacket);
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

    @Override
    public void close() {
        isRunning = false;
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    // --------------------------------------------------
}
