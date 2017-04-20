package com.zaze.demo.component.provider.presenter.impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.zaze.aarrepo.commons.base.ZBaseApplication;
import com.zaze.aarrepo.commons.base.ZBasePresenter;
import com.zaze.demo.component.provider.presenter.ProviderPresenter;
import com.zaze.demo.component.provider.sqlite.User;
import com.zaze.demo.component.provider.view.ProviderView;

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
                new String[]{User.KEY_ID, User.KEY_USER_ID, User.KEY_USERNAME},
                null, null, null);
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public void insert() {
        ContentValues values = new ContentValues();
//        values.put(User.KEY_ID, 1.81f);
        values.put(User.KEY_USERNAME, "Tom");
        values.put(User.KEY_USER_ID, 21);
        Uri newUri = resolver.insert(User.CONTENT_URI, values);
    }

}
