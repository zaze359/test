package com.zaze.demo.component.socket;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-28 - 16:06
 */
public abstract class BaseSocketClient {
    private BaseSocketFace socketFace;
    private String host;
    private int port;
    private int maxSize = 1024 * 100;

    public BaseSocketClient(String host, int port, BaseSocketFace socketFace) {
        this.host = host;
        this.port = port;
        this.socketFace = socketFace;
    }

    // --------------------------------------------------

    public abstract boolean receive();

    public abstract void send(String host, int port, SocketMessage message);

    public abstract void close();

    protected void onReceiver(SocketMessage socketMessage) {
        if (socketFace != null) {
            socketFace.onReceiver(socketMessage);
        }
    }

    // --------------------------------------------------
    public BaseSocketFace getSocketFace() {
        return socketFace;
    }

    public void setSocketFace(BaseSocketFace socketFace) {
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
    public abstract static class BaseSocketFace {

        /**
         * 接收到数据 异步线程
         *
         * @param socketMessage socketMessage
         */
        protected void onReceiver(SocketMessage socketMessage) {
            if (socketMessage != null) {
                ZLog.d(ZTag.TAG_DEBUG, "收到消息 ： " + socketMessage);
                switch (socketMessage.getMessageType()) {
                    case MessageType.PRESENCE:
                        onPresence(socketMessage);
                        break;
                    case MessageType.CONFIG:
                        onConfig(socketMessage);
                        break;
                    case MessageType.CHAT:
                        onChat(socketMessage);
                        break;
                    default:
                        break;
                }
            }
        }

        /**
         * 接收到邀请信息
         *
         * @param socketMessage socketMessage
         */
        protected void onPresence(SocketMessage socketMessage) {
            ZLog.d(ZTag.TAG_DEBUG, "是邀请信息");
        }

        /**
         * 接收到配置信息
         *
         * @param socketMessage socketMessage
         */
        protected void onConfig(SocketMessage socketMessage) {
            ZLog.d(ZTag.TAG_DEBUG, "是配置信息");
        }

        /**
         * 接收到聊天信息
         *
         * @param socketMessage socketMessage
         */
        protected void onChat(SocketMessage socketMessage) {
            ZLog.d(ZTag.TAG_DEBUG, "是聊天信息");
        }
    }
}
