package com.zz.library.commons.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description :
 * date : 2016-01-19 - 14:06
 *
 * @author : zaze
 * @version : 1.0
 */
public abstract class ZRecyclerAdapter<V, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {
    protected Context context;
    protected final List<V> dataList = new ArrayList<>();

    public ZRecyclerAdapter(Context context, List<V> data) {
        this.context = context;
        setDataList(data);
    }

    public ZRecyclerAdapter(Context context, V[] data) {
        this.context = context;
        setDataList(data);
    }

    public void setDataList(List<V> data) {
        dataList.clear();
        if (data != null && data.size() > 0) {
            dataList.addAll(data);
        }
    }

    public void setDataList(V[] data) {
        dataList.clear();
        if (data != null) {
            Collections.addAll(dataList, data);
        }
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getViewLayoutId(), null);
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(p);
        return createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(H holder, int position) {
        if (dataList != null && dataList.size() > 0) {
            onBindViewHolder(holder, dataList.get(position), position);
        } else {
            onBindViewHolder(holder, null, position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    public V getItem(int position) {
        if (dataList != null) {
            return dataList.get(position);
        }
        return null;
    }

    public abstract int getViewLayoutId();

    public abstract H createViewHolder(View convertView);

    public abstract void onBindViewHolder(H holder, V value, int position);
}
