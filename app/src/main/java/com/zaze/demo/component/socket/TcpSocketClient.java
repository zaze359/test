package com.zaze.demo.component.socket;

import android.os.SystemClock;
import android.text.TextUtils;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
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
class TcpSocketClient {
    private SocketFace socketFace;
    private boolean isRunning;
    private ThreadPoolExecutor serverExecutor;
    private static int connectId;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private static TcpSocketClient tcpSocketClient;
    // --------------------------------------------------
    private static final int headLength = 16;
    private byte[] headBytes = new byte[headLength];
    // --------------------------------------------------
    private static final long SLEEP_TIME = 500L;

    /**
     * getInstance
     *
     * @return TcpSocketClient
     */
    public static TcpSocketClient getInstance() {
        if (tcpSocketClient == null) {
            synchronized (TcpSocketClient.class) {
                if (tcpSocketClient == null) {
                    tcpSocketClient = new TcpSocketClient();
                }
            }
        }
        return tcpSocketClient;
    }

    private TcpSocketClient() {
        serverExecutor = new ThreadPoolExecutor(1, 1, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "socket server thread ");
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                return thread;
            }
        });
        serverExecutor.execute(new Runnable() {
            @Override
            public void run() {
                isRunning = true;
                ZLog.d(ZTag.TAG_DEBUG, "socket 开始接收数据");
                onReceiver();
                isRunning = false;
                ZLog.d(ZTag.TAG_DEBUG, "socket 停止接收数据");
            }
        });
    }

    /**
     * 建立连接
     *
     * @param host       host
     * @param port       port
     * @param socketFace socketFace
     * @return connectId
     */
    public int connect(String host, int port, SocketFace socketFace) {
        this.socketFace = socketFace;
        ZLog.d(ZTag.TAG_DEBUG, "socket 监听 %s:%s", host, port);
        try {
            if (!TextUtils.isEmpty(host)) {
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), 15000);
            }
            if (socket != null) {
                connectId++;
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                isRunning = true;
                if (socketFace != null) {
                    socketFace.onEvent(connectId, SocketCode.CONNECT_OK.getCode(), SocketCode.CONNECT_OK.getMsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            connectId = 0;
        }
        return connectId;
    }

    /**
     * 断开连接
     */
    public void disConnect() {
        try {
            if (socket != null) {
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                    outputStream = null;
                }
                if (socket.isConnected()) {
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断socket是否连接
     *
     * @return 是/否
     */
    public boolean isConnected() {
        if (socket != null) {
            return socket.isConnected() && !socket.isClosed();
        }
        return false;
    }

    /**
     * 接收数据时
     */
    private void onReceiver() {
        int length;
        while (isRunning) {
            try {
                if (inputStream != null) {
                    // 读取头
                    length = inputStream.read(headBytes);
                    if (headLength == length) {
                        // 获取数据包长度
                        int contentLength = Math.min(readInt(headBytes, 4), 1 << 20);
                        // 读取内容
                        byte[] buffer = new byte[contentLength - headLength];
                        length = inputStream.read(buffer);
                        if (socketFace != null) {
                            if (length > 0) {
                                byte[] bytes = new byte[length];
                                System.arraycopy(buffer, 0, bytes, 0, length);
                                socketFace.onReceiver(connectId, bytes, length);
                            } else {
                                socketFace.onEvent(connectId, SocketCode.CONNECT_CLOSED.getCode(), SocketCode.CONNECT_CLOSED.getMsg());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            SystemClock.sleep(SLEEP_TIME);
        }
    }


    /**
     * 发送数据
     *
     * @param message message
     * @return true 成功
     */
    public boolean send(byte[] message) {
        try {
            if (outputStream != null) {
                outputStream.write(message);
                outputStream.flush();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (socketFace != null) {
                socketFace.onEvent(connectId, SocketCode.SEND_MSG_FAILED.getCode(), SocketCode.SEND_MSG_FAILED.getMsg());
            }
        }
        return false;
    }


//    public void close() {
//        isRunning = false;
//        if (serverExecutor != null && !serverExecutor.isShutdown()) {
//            serverExecutor.shutdown();
//        }
//        tcpSocketClient = null;
//    }


    // --------------------------------------------------

    private static int readInt(byte[] msgData, int index) {
        int iRet;
        iRet = (((msgData[index + 3] & 0xff) << 24) | ((msgData[index + 2] & 0xff) << 16) | ((msgData[index + 1] & 0xff) << 8) | ((msgData[index + 0] & 0xff) << 0));
        return iRet;
    }

    // --------------------------------------------------


    /**
     * Socket回调.
     */
    public interface SocketFace {

        /**
         * onEvent
         *
         * @param id          id
         * @param code        code
         * @param description description
         */
        void onEvent(int id, int code, String description);

        /**
         * 接收到数据 异步线程.
         *
         * @param id      id
         * @param data    data
         * @param dataLen dataLen
         */
        void onReceiver(int id, byte[] data, int dataLen);

    }

    public enum SocketCode {

        /**
         * 连接成功
         */
        CONNECT_OK(0, "连接成功"),
        /**
         * 连接失败
         */
        CONNECT_FAIL(100, "连接失败"),
        /**
         * 断开连接
         */
        CONNECT_CLOSED(200, "断开连接"),
        /**
         * 发送消息失败
         */
        SEND_MSG_FAILED(300, "发送消息失败");


        private int code;
        private String msg;

        SocketCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
