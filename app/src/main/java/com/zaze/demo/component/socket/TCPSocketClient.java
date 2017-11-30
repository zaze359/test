package com.zaze.demo.component.socket;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-27 - 15:07
 */
public class TCPSocketClient extends BaseSocketClient {
    public TCPSocketClient(String host, int port, BaseSocketFace socketFace) {
        super(host, port, socketFace);
    }

    @Override
    public boolean receive() {
        return false;
    }

    @Override
    public void send(String host, int port, SocketMessage message) {

    }

    @Override
    public void close() {

    }
}
