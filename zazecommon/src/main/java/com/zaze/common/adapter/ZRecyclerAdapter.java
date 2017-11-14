package com.zaze.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaze.utils.ZViewUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description :
 * date : 2016-01-19 - 14:06
 *
 * @author : zaze
 * @version : 1.0
 */
public abstract class ZRecyclerAdapter<V, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {
    private Context context;
    private final List<V> dataList = new ArrayList<>();

    public ZRecyclerAdapter(Context context, Collection<V> data) {
        this.context = context;
        setDataList(data, false);
    }

    public void setDataList(Collection<V> data) {
        setDataList(data, true);
    }

    private void setDataList(Collection<V> data, boolean isNotify) {
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
        onBindView(holder, getItem(position), position);
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

    public Context getContext() {
        return context;
    }

    public List<V> getDataList() {
        return dataList;
    }

    // --------------------------------------------------

    public <T extends View> T findView(View parent, int resId) {
        return ZViewUtil.findView(parent, resId);
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
    public abstract H createViewHolder(View convertView);

    /**
     * view赋值
     *
     * @param holder   holder
     * @param value    value
     * @param position position
     */
    public abstract void onBindView(H holder, V value, int position);
}
