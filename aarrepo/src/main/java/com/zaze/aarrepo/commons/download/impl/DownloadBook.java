package com.zaze.aarrepo.commons.download.impl;//package com.zz.library.commons.download.impl;
//
//import android.app.Activity;
//
//import com.zhitongyun.frame.app.net.BaseNetHelper;
//import com.zhitongyun.library.util.ThreadManager;
//import com.zhitongyun.publics.component.download.DownCallback;
//import com.zhitongyun.publics.db.dao.TDownload;
//import com.zhitongyun.publics.request.UnZipBook;
//
///**
// * Description : 下载书 相关
// * date : 2016-04-01 - 13:06
// *
// * @author : zaze
// * @version : 1.0
// */
//public class DownloadBook extends BaseDownload {
//    private Activity activity;
//
//    public DownloadBook(Activity activity, DownCallback callback) {
//        super(callback);
//        this.activity = activity;
//    }
//
//    @Override
//    public boolean toInstallDownFile(final TDownload download) {
//        final int id = (int) download.getDown_id();
//        BaseNetHelper unZipBook = new UnZipBook(id, download.getSave_path());
//        unZipBook.request(activity);
//        unZipBook.setNetCallback(new BaseNetHelper.NetCallback() {
//            @Override
//            public void callback(final int i, final Object o) {
//                if (o instanceof String) {
//                    saveInstallPath(id, (String) o);
//                    ThreadManager.getInstance().runInUIThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            onInstallFinish(0, i, (String) o, download.getSave_path());
//                        }
//                    });
//                }
//            }
//        });
//        return true;
//    }
//
//    @Override
//    public int getType() {
//        return DownType.BOOK;
//    }
//}
