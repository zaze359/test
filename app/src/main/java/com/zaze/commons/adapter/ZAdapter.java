package com.zaze.commons.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * date : 2015-11-30 - 09:40
 * @author : zaze
 * @version : 1.0
 */
public abstract class ZAdapter<V, H> extends BaseAdapter {
    protected Context context;
    protected final List<V> dataList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    //
    public ZAdapter(Context context, List<V> data) {
        this.context = context;
        setDataList(data);
    }
    public void setDataList(List<V> data) {
        dataList.clear();
        if(data != null && data.size() > 0) {
            dataList.addAll(data);
        }
    }

    @Override
    public int getCount() {
        if(dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public V getItem(int position) {
        if(dataList != null) {
            return dataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        H itemHolder;
        try {
            if(convertView == null) {
                convertView = View.inflate(context, getViewLayoutId(), null);
                itemHolder = createViewHolder(convertView);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (H) convertView.getTag();
            }
            setViewData(getItem(position), itemHolder, position, convertView, parent);
            convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onItemClickListener != null) {
                            onItemClickListener.onItemClick(getItem(position), v, position);
                        }
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
  
    public interface OnItemClickListener<V> {
        void onItemClick(V value, View view, int position);
    }
    
    public void setOnItemClickListener(OnItemClickListener<V> clickListener) {
        this.onItemClickListener = clickListener;
    }
    public abstract void setViewData(V value, H itemHolder, int position, View convertView, ViewGroup parent);
    public abstract int getViewLayoutId();
    public abstract H createViewHolder(View convertView);
    //
}
