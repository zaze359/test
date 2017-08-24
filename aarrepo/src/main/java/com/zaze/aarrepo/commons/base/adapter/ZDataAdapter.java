package com.zaze.aarrepo.commons.base.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

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
abstract class ZDataAdapter<V> extends BaseAdapter {
    private Context context;
    private final List<V> dataList = new ArrayList<V>();

    //
    ZDataAdapter(Context context, Collection<V> data) {
        this.context = context;
        setDataList(data, false);
    }

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

    public Context getContext() {
        return context;
    }

    public List<V> getDataList() {
        return dataList;
    }

    public int getColor(int resId) {
        return context.getResources().getColor(resId);
    }

    public String getString(int resId, Object... args) {
        return context.getString(resId, args);
    }

}
