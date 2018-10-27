package com.zaze.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description :
 * date : 2016-01-19 - 14:06
 *
 * @author : zaze
 * @version : 1.0
 */
public abstract class BaseRecyclerAdapter<V, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> implements ResourceAdapter {
    private Context context;
    private final List<V> dataList = new ArrayList<>();

    public BaseRecyclerAdapter(Context context, Collection<V> data) {
        this.context = context;
        setDataList(data, false);
    }

    public void setDataList(Collection<V> data) {
        setDataList(data, true);
    }

    protected void setDataList(Collection<V> data, boolean isNotify) {
        dataList.clear();
        if (data != null && data.size() > 0) {
            dataList.addAll(data);
        }
        if (isNotify) {
            notifyDataSetChanged();
        }
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getViewLayoutId(), parent, false);
        return createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(H holder, int position) {
        onBindView(holder, getItem(position), position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public V getItem(int position) {
        if (position < 0 || position >= dataList.size()) {
            return null;
        } else {
            return dataList.get(position);
        }
    }

    public Context getContext() {
        return context;
    }

    public List<V> getDataList() {
        return dataList;
    }

    // --------------------------------------------------

    /**
     * get view layout id
     *
     * @return int
     */
    public abstract int getViewLayoutId();

    /**
     * 构建viewHolder
     *
     * @param convertView convertView
     * @return H
     */
    public abstract H createViewHolder(@NonNull View convertView);

    /**
     * view赋值
     *
     * @param holder   holder
     * @param value    value
     * @param position position
     */
    public abstract void onBindView(H holder, V value, int position);

    // --------------------------------------------------
    @Override
    public <T extends View> T findView(View parentView, int resId) {
        return parentView.findViewById(resId);
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
