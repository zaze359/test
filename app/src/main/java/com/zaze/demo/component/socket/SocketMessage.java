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

    @Override
    public String toString() {
        return "SocketMessage{" +
                "address='" + address + '\'' +
                ", port=" + port +
                ", message='" + message + '\'' +
                '}';
    }
}
