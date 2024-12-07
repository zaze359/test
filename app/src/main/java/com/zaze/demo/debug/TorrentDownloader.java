//package com.zaze.demo.debug;
//
//import android.util.Log;
//
//
//import com.zaze.utils.log.ZLog;
//
//import org.libtorrent4j.AlertListener;
//import org.libtorrent4j.Priority;
//import org.libtorrent4j.SessionManager;
//import org.libtorrent4j.SettingsPack;
//import org.libtorrent4j.TorrentFlags;
//import org.libtorrent4j.TorrentHandle;
//import org.libtorrent4j.TorrentInfo;
//import org.libtorrent4j.alerts.Alert;
//import org.libtorrent4j.alerts.AlertType;
//import org.libtorrent4j.alerts.TorrentAlert;
//import org.libtorrent4j.swig.settings_pack;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.HashSet;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//public class TorrentDownloader {
//    private static final String TAG = "TorrentDownloader";
//    private final SessionManager session;
//    private final CountDownLatch signal = new CountDownLatch(1);
//    private TorrentHandle torrentHandle;
//
//    private OnProgressCallback onProgressCallback;
//    private OnCompleteCallback onCompleteCallback;
//    private OnErrorCallback onErrorCallback;
//
//    public final HashSet<String> magnets = new HashSet<>();
//    public final List<String> downloadPathList = new ArrayList<>();
//
//    private final int[] INNER_LISTENER_TYPES = new int[]{
//            AlertType.STATE_CHANGED.swig(),
//            AlertType.TORRENT_FINISHED.swig(),
//            AlertType.TORRENT_REMOVED.swig(),
//            AlertType.TORRENT_PAUSED.swig(),
//            AlertType.TORRENT_RESUMED.swig(),
//            AlertType.SAVE_RESUME_DATA.swig(),
//            AlertType.STORAGE_MOVED.swig(),
//            AlertType.STORAGE_MOVED_FAILED.swig(),
//            AlertType.METADATA_RECEIVED.swig(),
//            AlertType.PIECE_FINISHED.swig(),
//            AlertType.READ_PIECE.swig(),
//            AlertType.TORRENT_ERROR.swig(),
//            AlertType.METADATA_FAILED.swig(),
//            AlertType.FILE_ERROR.swig(),
//            AlertType.FASTRESUME_REJECTED.swig(),
//            AlertType.TORRENT_CHECKED.swig()
//    };
//
//    private static class SingletonHolder {
//        private static final TorrentDownloader INSTANCE = new TorrentDownloader();
//    }
//
//    private TorrentDownloader() {
//        session = new SessionManager();
//        SettingsPack settingsPack = SettingsPack.defaultSettings();
//        settingsPack.setString(settings_pack.string_types.listen_interfaces.swigValue(), "0.0.0.0:6881");
//        settingsPack.setBoolean(settings_pack.bool_types.enable_lsd.swigValue(), true);
//        settingsPack.setBoolean(settings_pack.bool_types.enable_dht.swigValue(), true);
//        settingsPack.setBoolean(settings_pack.bool_types.enable_upnp.swigValue(), true);
//        settingsPack.setBoolean(settings_pack.bool_types.enable_natpmp.swigValue(), true);
////        settingsPack.setBoolean(settings_pack.bool_types.enable_natpmp.swigValue(), true);
//        session.applySettings(settingsPack);
//    }
//
//    public static TorrentDownloader getInstance() {
//        return SingletonHolder.INSTANCE;
//    }
//
//
//    public void startDownload(File torrentFile, String bencode, String downloadPath, String[] nodes) {
//        if (downloadPathList.contains(downloadPath)){
//            return;
//        }
//        TorrentInfo torrentInfo = null;
//        if (torrentFile != null) {
//            torrentInfo = new TorrentInfo(torrentFile);
//        } else if (!bencode.isEmpty()) {
//            byte[] data = Base64.getDecoder().decode(bencode);
//            ZLog.i(TAG,data.toString());
//            torrentInfo = TorrentInfo.bdecode(data);
//        }
//        if (null == torrentInfo) {
//            return;
//        }
//        downloadPathList.add(downloadPath);
//        ZLog.i(TAG, "torrentInfo.name() = " + torrentInfo.name());
//        ZLog.i(TAG, "torrentInfo.numFiles() = " + torrentInfo.numFiles());
//        ZLog.i(TAG, "torrentInfo.totalSize() = " + torrentInfo.totalSize());
//        ZLog.i(TAG, "torrentInfo.similarTorrents() = " + torrentInfo.similarTorrents());
//        long alertListenTimeout = 5000; // ms
//        for (String node : nodes) {
//            ZLog.i(TAG, "addNode: " + node);
//            String[] ip = node.split(":");
//            if (ip.length == 2) {
//                torrentInfo.addNode(ip[0], Integer.parseInt(ip[1]));
//            }
//        }
//
//        session.addListener(new AlertListener() {
//            @Override
//            public int[] types() {
//                return INNER_LISTENER_TYPES;
//            }
//
//            @Override
//            public void alert(Alert<?> alert) {
//                ZLog.i(TAG, "alert");
//                if (alert == null) return;
//                AlertType type = alert.type();
//                if (type == null) return;
//
//                TorrentAlert<?> torrentAlert = (TorrentAlert<?>) alert;
//
//                switch (type) {
//                    case ADD_TORRENT:
//                        ZLog.i(TAG, "ADD_TORRENT");
//                        torrentHandle = session.find(torrentAlert.handle().infoHash());
//                        if (torrentHandle == null) return;
//                        String hash = torrentHandle.infoHash().toHex();
//                        if (magnets.contains(hash)) return;
//                        if (torrentHandle.isValid()) {
//                            magnets.add(hash);
//                            torrentHandle.resume();
//                        }
//                        break;
//
//                    case BLOCK_FINISHED:
//                        int progress = (int) (torrentAlert.handle().status().progress() * 100);
//                        if (onProgressCallback != null) {
//                            onProgressCallback.onProgress(progress);
//                        }
//                        break;
//
//                    case TORRENT_FINISHED:
//                        ZLog.i(TAG, "TORRENT_FINISHED");
//                        if (onCompleteCallback != null) {
//                            onCompleteCallback.onComplete();
//                        }
//                        break;
//
//                    case TORRENT_PAUSED:
//                        ZLog.i(TAG, "TORRENT_PAUSED");
//                        break;
//
//                    case PIECE_FINISHED:
//                        break;
//
//                    case TORRENT_ERROR:
//                    case FILE_ERROR:
//                    case PEER_ERROR:
//                    case DHT_ERROR:
//                        if (onErrorCallback != null) {
//                            Log.i(TAG+"ERROR", "TORRENT_ERROR or FILE_ERROR or PEER_ERROR or DHT_ERROR");
//                            onErrorCallback.onError();
//                        }
//                        break;
//
//                    default:
//                        ZLog.i(TAG, type.name());
//                        break;
//                }
//            }
//        });
//
//        session.start();
//
//        Priority[] priorities = Priority.array(Priority.IGNORE, torrentInfo.numFiles());
//        priorities[0] = Priority.TOP_PRIORITY;
//
//        try {
//            ZLog.i(TAG, "start");
//            session.download(
//                    torrentInfo,
//                    new File(downloadPath),
//                    null,
//                    priorities,
//                    null,
//                    TorrentFlags.SEQUENTIAL_DOWNLOAD
//            );
//            ZLog.i(TAG, "and");
//            boolean awaitResult = signal.await(alertListenTimeout, TimeUnit.MILLISECONDS);
//            if (!awaitResult) {
//                // 如果超时，调用失败回调
//                if (onErrorCallback != null) {
//                    Log.i(TAG+"ERROR", "awaitResult");
//                    onErrorCallback.onError();
//                }
//            }
//        } catch (InterruptedException e) {
//            ZLog.e(TAG, "Interrupted while waiting for alert", e);
//            Thread.currentThread().interrupt();
//            if (onErrorCallback != null) {
//                Log.i(TAG+"ERROR", "InterruptedException");
//                onErrorCallback.onError();
//            }
//        } catch (Exception e) {
//            ZLog.e(TAG, "Error during download", e);
//            if (onErrorCallback != null) {
//                Log.i(TAG+"ERROR", "Exception");
//                onErrorCallback.onError();
//            }
//        } finally {
//            ZLog.i(TAG, "finally");
//            downloadPathList.remove(downloadPath);
//            session.stop();
//            signal.countDown();
//
//        }
//    }
//
//    public void setOnProgressCallback(OnProgressCallback callback) {
//        this.onProgressCallback = callback;
//    }
//
//    public void setOnCompleteCallback(OnCompleteCallback callback) {
//        this.onCompleteCallback = callback;
//    }
//
//    public void setOnErrorCallback(OnErrorCallback callback) {
//        this.onErrorCallback = callback;
//    }
//
//    public void cancelDownload() {
//        session.stop();
//        signal.countDown();
//    }
//
//    public interface OnProgressCallback {
//        void onProgress(int progress);
//    }
//
//    public interface OnCompleteCallback {
//        void onComplete();
//    }
//
//    public interface OnErrorCallback {
//        void onError();
//    }
//}
//
