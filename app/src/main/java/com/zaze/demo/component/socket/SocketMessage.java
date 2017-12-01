package com.zaze.demo.component.socket;

/**
 * Description :.
 *
 * @author : ZAZE
 * @version : 2017-11-28 - 11:24
 */
public class SocketMessage {
    private long fromId;
    private long toId;
    private String address;
    private int port;
    private String message;
    private int msgType;
    private long createTime = System.currentTimeMillis();
    private long receiverTime;

    public SocketMessage() {
    }

    public SocketMessage(long fromId, long toId, String message, int msgType) {
        this.fromId = fromId;
        this.toId = toId;
        this.message = message;
        this.msgType = msgType;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
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

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "SocketMessage{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", address='" + address + '\'' +
                ", port=" + port +
                ", message='" + message + '\'' +
                ", msgType=" + msgType +
                ", createTime=" + createTime +
                ", receiverTime=" + receiverTime +
                '}';
    }
}
