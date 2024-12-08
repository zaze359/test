package com.zaze.demo.debug.torrent;

import android.util.Log;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.libtorrent4j.AlertListener;
import org.libtorrent4j.alerts.*;

public abstract class MyAlertListener implements AlertListener {

    @Override
    public int[] types() {
        return null;
    }

    @Override
    public void alert(Alert<?> alert) {
        AlertType type = alert.type();

        switch (type) {
            case ADD_TORRENT:
                ZLog.i(ZTag.TAG, "torrent added");
                ((AddTorrentAlert) alert).handle().resume();
                break;
            case BLOCK_FINISHED:
                BlockFinishedAlert blockFinishedAlert = (BlockFinishedAlert) alert;
                int p = (int) (blockFinishedAlert.handle().status().progress() * 100);

                ZLog.i(ZTag.TAG, "Progress: " + p + " for torrent name: " + blockFinishedAlert.torrentName()
                        + ",peer:" + blockFinishedAlert.peerId()
                        + ",endpoint:" + blockFinishedAlert.endpoint());
//                        ZLog.i(ZTag.TAG, "dhtNodes:" + session.stats().dhtNodes()
//                                + ",totalDownload:" + session.stats().totalDownload()
//                                + ",totalUpload:" + session.stats().totalUpload()
//                        );
                onProgress(p);
                break;
            case TORRENT_FINISHED:
                ZLog.i(ZTag.TAG, "Torrent finished");
//                        signal.countDown();
                onComplete();
                break;
            case PEER_CONNECT:
                PeerConnectAlert peerConnectAlert = (PeerConnectAlert) alert;

                ZLog.i(ZTag.TAG, "Connected to peer: " + peerConnectAlert.peerId()
                        + ",endpoint:" + peerConnectAlert.endpoint()
                        + ",direction:" + peerConnectAlert.direction()
                );
                break;
            case PEER_DISCONNECTED:
                PeerDisconnectedAlert peerDisconnectAlert = (PeerDisconnectedAlert) alert;
                ZLog.i(ZTag.TAG, "Disconnected from peer: " + peerDisconnectAlert.peerId()
                        + ",endpoint:" + peerDisconnectAlert.endpoint()
                );
                break;
            case INCOMING_CONNECTION:
                IncomingConnectionAlert incomingAlert = (IncomingConnectionAlert) alert;
                ZLog.i(ZTag.TAG, "Incoming connection from: " + incomingAlert.endpoint()
//                                + incomingAlert.
                );
                break;
            case DHT_GET_PEERS:
                DhtGetPeersAlert dhtGetPeersAlert = (DhtGetPeersAlert) alert;
                ZLog.i(ZTag.TAG, "DHT get peers for info hash: " + dhtGetPeersAlert.infoHash()
//                                +dhtGetPeersAlert.
                );
                break;
            case DHT_ANNOUNCE:
                DhtAnnounceAlert dhtAnnounceAlert = (DhtAnnounceAlert) alert;
                ZLog.i(ZTag.TAG, "DHT announce from: " + dhtAnnounceAlert.ip() + ":"
                        + dhtAnnounceAlert.port()
                );
                break;
            case LSD_PEER:
                LsdPeerAlert lsdPeerAlert = (LsdPeerAlert) alert;
                ZLog.i(ZTag.TAG, "LSD peer discovered: " + lsdPeerAlert.peerId()
                        + ",type: " + lsdPeerAlert.type()
                        + ",endpoint: " + lsdPeerAlert.endpoint()
                        + ",toString: " + lsdPeerAlert
                );
                break;
//                    case FILE_COMPLETED:
//                        FileCompletedAlert fileCompletedAlert = (FileCompletedAlert) alert;
//                        ZLog.i(ZTag.TAG, "File completed: " + fileCompletedAlert.torrentName());
//                        break;
//                    case PIECE_FINISHED:
//                        PieceFinishedAlert pieceFinishedAlert = (PieceFinishedAlert) alert;
//                        ZLog.i(ZTag.TAG, "Piece finished for torrent: " + pieceFinishedAlert.torrentName());
//                        break;
            case SESSION_STATS:
                SessionStatsAlert sessionStatsAlert = (SessionStatsAlert) alert;
                ZLog.i(ZTag.TAG, "Session stats updated");
                // 这里可以添加更多关于 sessionStatsAlert 的处理逻辑
                break;
            case TORRENT_ERROR:
            case FILE_ERROR:
            case PEER_ERROR:
            case DHT_ERROR:
                ZLog.i(ZTag.TAG + "ERROR", "TORRENT_ERROR or FILE_ERROR or PEER_ERROR or DHT_ERROR");
                onError();
                break;
            default:
                break;
        }
    }


    abstract void onProgress(int progress);

    abstract void onComplete();

    abstract void onError();


}