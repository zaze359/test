package com.zaze.demo.component.socket;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-28 - 11:24
 */
public class SocketMessage {
    private String address;
    private int port;
    private String message;
    private int messageType;
    private long createTime = System.currentTimeMillis();
    private long receiverTime;

    public SocketMessage() {
    }

    public SocketMessage(String message, int messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    public SocketMessage(String address, int port, String message, int messageType) {
        this.address = address;
        this.port = port;
        this.message = message;
        this.messageType = messageType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getReceiverTime() {
        return receiverTime;
    }

    public void setReceiverTime(long receiverTime) {
        this.receiverTime = receiverTime;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "SocketMessage{" +
                "address='" + address + '\'' +
                ", port=" + port +
                ", message='" + message + '\'' +
                ", createTime=" + createTime +
                ", receiverTime=" + receiverTime +
                '}';
    }
}
