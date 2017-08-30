package com.zaze.demo.component.provider.presenter.impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.zaze.common.base.ZBaseApplication;
import com.zaze.common.base.ZBasePresenter;
import com.zaze.demo.component.provider.presenter.ProviderPresenter;
import com.zaze.demo.component.provider.sqlite.User;
import com.zaze.demo.component.provider.sqlite.UserDao;
import com.zaze.demo.component.provider.view.ProviderView;
import com.zaze.utils.ZJsonUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-03-28 04:00 1.0
 */
public class ProviderPresenterImpl extends ZBasePresenter<ProviderView> implements ProviderPresenter {
    ContentResolver resolver;

    public ProviderPresenterImpl(ProviderView view) {
        super(view);
        resolver = ZBaseApplication.getInstance().getContentResolver();
    }

    @Override
    public void query() {
        Cursor cursor = resolver.query(User.CONTENT_URI,
                new String[]{UserDao.Properties.ID, UserDao.Properties.USER_ID, UserDao.Properties.USER_NAME},
                null, null, null);
        User user = new User();
        if (cursor != null) {
            if (cursor.moveToNext()) {
                user.setId(cursor.getLong(cursor.getColumnIndex(UserDao.Properties.ID)));
                user.setUserId(cursor.getLong(cursor.getColumnIndex(UserDao.Properties.USER_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(UserDao.Properties.USER_NAME)));
            }
            cursor.close();
        }
        ZLog.i(ZTag.TAG_PROVIDER, ZJsonUtil.objToJson(user));


    }

    @Override
    public void insert() {
        ContentValues values = new ContentValues();
//        values.put(User.KEY_ID, 1.81f);
        values.put(UserDao.Properties.USER_NAME, "Tom");
        values.put(UserDao.Properties.USER_ID, 21);
        Uri newUri = resolver.insert(User.CONTENT_URI, values);
    }

}
