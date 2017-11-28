package com.zaze.demo.component.socket;

import org.json.JSONObject;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-28 - 16:06
 */
public abstract class SocketClient {
    private SocketFace socketFace;
    private String host;
    private int port;
    private int maxSize = 1024 * 100;

    public SocketClient(int port, SocketFace socketFace) {
        this(null, port, socketFace);
    }

    public SocketClient(String host, int port, SocketFace socketFace) {
        this.host = host;
        this.port = port;
        this.socketFace = socketFace;
    }

    // --------------------------------------------------

    public abstract boolean receive();

    public abstract void send(String host, int port, JSONObject json);

    public abstract void close();

    protected void onReceiver(SocketMessage socketMessage) {
        if (socketFace != null) {
            socketFace.onReceiver(socketMessage);
        }
    }

    // --------------------------------------------------
    public SocketFace getSocketFace() {
        return socketFace;
    }

    public void setSocketFace(SocketFace socketFace) {
        this.socketFace = socketFace;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
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
