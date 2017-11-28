package com.zaze.demo.component.socket;

import org.json.JSONObject;

import java.net.SocketAddress;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-27 - 15:07
 */
public class TCPSocketClient extends SocketClient {

    public TCPSocketClient(String host, int port, SocketFace socketFace) {
        super(host, port, socketFace);
    }

    @Override
    public boolean receive() {
        return false;
    }

    @Override
    public void send(SocketAddress address, JSONObject json) {

    }

    @Override
    public void close() {

    }
}
