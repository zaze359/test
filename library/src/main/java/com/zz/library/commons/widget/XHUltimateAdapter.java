package com.zz.library.commons.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : 封装 UltimateViewAdapter
 * date : 2016-01-23 - 11:03
 *
 * @author : zaze
 * @version : 1.0
 */
public abstract class XHUltimateAdapter<V, VH extends RecyclerView.ViewHolder> extends UltimateViewAdapter<VH> {
    protected Context context;
    protected final List<V> dataList = new ArrayList<>();
    private View.OnClickListener onClickListener;
    private OnItemClickListener<V> onItemClickListener;

    public XHUltimateAdapter(Context context, List<V> data) {
        this.context = context;
        preSetData();
        this.setDataList(data);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    Object object = v.getTag();
                    if (object instanceof Integer) {
                        int position = (Integer) object;
                        onItemClickListener.onItemClick(v, getItem(position), position);
                    }
                }
            }
        };

    }

    protected void preSetData() {

    }

    public void setDataList(List<V> data) {
        this.dataList.clear();
        if (data != null && data.size() > 0) {
            this.dataList.addAll(data);
        }
    }

    @Override
    public int getAdapterItemCount() {
        return this.dataList.size();
    }


//    @Override
//    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == VIEW_TYPES.FOOTER) {
//            VH viewHolder = getViewHolder(customLoadMoreView, false);
//            if (getAdapterItemCount() == 0)
//                viewHolder.itemView.setVisibility(View.GONE);
//            return viewHolder;
//        } else if (viewType == VIEW_TYPES.HEADER) {
//            if (customHeaderView != null)
//                return getViewHolder(customHeaderView, false);
//        } else if (viewType == VIEW_TYPES.CHANGED_FOOTER) {
//            VH viewHolder = getViewHolder(customLoadMoreView, false);
//            if (getAdapterItemCount() == 0)
//                viewHolder.itemView.setVisibility(View.GONE);
//            return viewHolder;
//        }
//        return onCreateViewHolder(parent);
//    }

//    @Deprecated
//    @Override
//    public VH getViewHolder(View view) {
//        return null;
//    }

    @Override
    public VH newFooterHolder(View view) {
//        return getViewHolder(view);
        return null;
    }

    @Override
    public VH newHeaderHolder(View view) {
//        return getViewHolder(view);
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getViewLayoutId(), parent, false);
//        ViewGroup.LayoutParams p = view.getLayoutParams();
//        if (p == null) {
//            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        } else {
//            p.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            p.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        }
//        view.setLayoutParams(p);
        view.setOnClickListener(onClickListener);
//        return this.getViewHolder(view, true);
        return this.getViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        int size = dataList.size();
        if (holder.itemView != null) {
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(onClickListener);
        }
        if (position < getItemCount() && (customHeaderView != null ? position <= size : position < size) && (customHeaderView == null || position > 0)) {
            if (size > 0) {
                this.onBindViewHolder(holder, this.dataList.get(position), position);
            } else {
                this.onBindViewHolder(holder, null, position);
            }
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

//    public void loadMore(Collection<V> data) {
//        if (data != null && data.size() > 0) {
//            for (V v : data) {
//                insert(v, this.getAdapterItemCount());
//            }
//        }
//    }

    //
    public abstract VH getViewHolder(View view);

//    public abstract VH getViewHolder(View view, boolean isItem);

    public abstract int getViewLayoutId();

    public abstract void onBindViewHolder(VH holder, V value, int position);

    //
    public interface OnItemClickListener<V> {
        void onItemClick(View view, V value, int position);
    }

    public void setOnItemClickListener(OnItemClickListener<V> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
