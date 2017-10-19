package com.zaze.common.adapter.third;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.zaze.common.adapter.OnItemClickListener;
import com.zaze.utils.ZOnClickHelper;
import com.zaze.utils.ZViewUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description : 封装 UltimateViewAdapter
 * date : 2016-01-23 - 11:03
 *
 * @author : zaze
 * @version : 1.0
 */
public abstract class ZUltimateRecycleAdapter<V, VH extends RecyclerView.ViewHolder> extends UltimateViewAdapter<VH> {
    private Context context;
    private final List<V> dataList = new ArrayList<>();
    private View.OnClickListener onClickListener;
    private OnItemClickListener<V> onItemClickListener;

    private int page;
    private int pageSize = 10;

    public ZUltimateRecycleAdapter(Context context, Collection<V> data) {
        this.context = context;
        preSetData();
        this.setDataList(data, false);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object object = v.getTag();
                if (object instanceof Integer) {
                    int position = (Integer) object;
                    onItemClick(v, getItem(position), position);
                }
            }
        };
    }

    protected void preSetData() {

    }

    public void setDataList(Collection<V> data) {
        setDataList(data, true);
    }

    private void setDataList(Collection<V> data, boolean isNotify) {
        page = 0;
        this.dataList.clear();
        if (data != null && data.size() > 0) {
            this.dataList.addAll(data);
            page = 1;
        }
        if (isNotify) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getAdapterItemCount() {
        return this.dataList.size();
    }

    @Override
    public VH newFooterHolder(View view) {
        return getViewHolder(view, false);
    }

    @Override
    public VH newHeaderHolder(View view) {
        return getViewHolder(view, false);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getViewLayoutId(), parent, false);
        return this.getViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        int size = dataList.size();
        if (holder.itemView != null) {
            holder.itemView.setTag(position);
            ZOnClickHelper.setOnClickListener(holder.itemView, onClickListener);
        }
        if (position < getItemCount() && (customHeaderView != null ? position <= size : position < size) && (customHeaderView == null || position > 0)) {
            this.onBindView(holder, getItem(position), position);
        }
    }

    public V getItem(int position) {
        if (customHeaderView != null) {
            position--;
        }
        if (position < dataList.size()) {
            return dataList.get(position);
        } else {
            return null;
        }
    }

    public void loadMore(List<V> list) {
        if (list != null && !list.isEmpty()) {
            insertInternal(list, dataList);
            page++;
        } else {
        }
    }

    public void loadMore(V data) {
        if (data != null) {
            List<V> list = new ArrayList<>();
            list.add(data);
            loadMore(list);
        }
    }

    protected void onItemClick(@NonNull View view, @NonNull V value, int position) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(view, value, position);
        }
    }

    // --------------
    public void setOnItemClickListener(OnItemClickListener<V> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Context getContext() {
        return context;
    }

    public List<V> getDataList() {
        return dataList;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    //
    public int getColor(int resId) {
        return context.getResources().getColor(resId);
    }

    public String getString(int resId, Object... args) {
        return context.getString(resId, args);
    }

    public Drawable getDrawable(int id) {
        return context.getResources().getDrawable(id);
    }

    public Bitmap getBitmap(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    // --------------------------------------------------
    public <T extends View> T findView(View parentView, int resId) {
        return ZViewUtil.findView(parentView, resId);
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
     * @param view   view
     * @param isItem isItem
     * @return H
     */
    public abstract VH getViewHolder(View view, boolean isItem);

    /**
     * view赋值
     *
     * @param holder   holder
     * @param value    value
     * @param position position
     */
    public abstract void onBindView(VH holder, V value, int position);
}
