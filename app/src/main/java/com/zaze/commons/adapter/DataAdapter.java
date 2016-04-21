package com.zaze.commons.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
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
    protected final ArrayList<V> dataList = new ArrayList<V>();
    //
    public DataAdapter(Context context, List<V> data) {
        this.context = context;
        setDataList(data);
    }
    public void setDataList(List<V> data) {
        dataList.clear();
        if(data != null && data.size() > 0) {
            dataList.addAll(data);
        }
    }
    
    public V getCilde(int position) {
        if(dataList != null) {
            return dataList.get(position);
        }
        return null;
    }
    
    @Override
    public int getCount() {
        if(dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(dataList != null) {
            return dataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
