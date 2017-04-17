package com.zaze.demo.component.provider.presenter.impl;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.zaze.aarrepo.commons.base.ZBaseApplication;
import com.zaze.aarrepo.commons.base.ZBasePresenter;
import com.zaze.demo.component.provider.presenter.ProviderPresenter;
import com.zaze.demo.component.provider.view.ProviderView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-03-28 04:00 1.0
 */
public class ProviderPresenterImpl extends ZBasePresenter<ProviderView> implements ProviderPresenter {
    ContentResolver resolver;
    String KEY_ID = "_id";
    String KEY_NAME = "name";
    String KEY_AGE = "age";
    String KEY_HEIGHT = "height";


    public ProviderPresenterImpl(ProviderView view) {
        super(view);
        resolver = ZBaseApplication.getInstance().getContentResolver();
    }


    private void query() {
//        Uri uri = Uri.parse(CONTENT_URI_STRING + "/" + "2");
    }

    private void insert() {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, "Tom");
        values.put(KEY_AGE, 21);
        values.put(KEY_HEIGHT, 1.81f);
//        Uri newUri = resolver.insert(CONTENT_URI, values);
    }

}
