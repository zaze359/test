package com.zaze.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.BaseAdapter;

import com.zaze.common.util.ViewUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description :
 * date : 2015-11-30 - 09:24
 *
 * @author : zaze
 * @version : 1.0
 */
abstract class BaseDataAdapter<V> extends BaseAdapter implements ResourceAdapter {
    private Context context;
    private final List<V> dataList = new ArrayList<V>();

    BaseDataAdapter(Context context, Collection<V> data) {
        this.context = context;
        setDataList(data, false);
    }

    public void setDataList(List<V> data) {
        setDataList(data, true);
    }

    private void setDataList(Collection<V> data, boolean isNotify) {
        this.dataList.clear();
        if (data != null && data.size() > 0) {
            this.dataList.addAll(data);
        }
        if (isNotify) {
            notifyDataSetChanged();
        }
    }

    // --------------------------------------------------
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public V getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // --------------------------------------------------
    public Context getContext() {
        return context;
    }

    public List<V> getDataList() {
        return dataList;
    }

    // --------------------------------------------------
    @Override
    public <T extends View> T findView(View parentView, int resId) {
        return ViewUtil.findView(parentView, resId);
    }

    @Override
    public int getColor(int resId) {
        return ContextCompat.getColor(context, resId);
    }

    @Override
    public String getString(int resId, Object... args) {
        return context.getString(resId, args);
    }

    @Override
    public Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(context, id);
    }

    @Override
    public Bitmap getBitmap(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }
}
