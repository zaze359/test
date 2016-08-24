package com.zz.library.commons.adapter;

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
public abstract class DataAdapter<V extends Object> extends BaseAdapter {
    protected Context context;
    protected final List<V> dataList = new ArrayList<V>();

    //
    public DataAdapter(Context context, Collection<V> data) {
        this.context = context;
        setDataList(data);
    }

    public void setDataList(Collection<V> data) {
        dataList.clear();
        if (data != null && data.size() > 0) {
            dataList.addAll(data);
        }
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
}
