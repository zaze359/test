package com.zaze.demo.component.socket;

import java.net.SocketAddress;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-28 - 11:24
 */
public class SocketMessage {
    private String address;
    private SocketAddress socketAdress;
    private int port;
    private String message;

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

    public SocketAddress getSocketAdress() {
        return socketAdress;
    }

    public void setSocketAdress(SocketAddress socketAdress) {
        this.socketAdress = socketAdress;
    }

    @Override
    public String toString() {
        return "SocketMessage{" +
                "address='" + address + '\'' +
                ", socketAdress=" + socketAdress +
                ", port=" + port +
                ", message='" + message + '\'' +
                '}';
    }
}
