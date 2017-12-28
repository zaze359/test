package com.zaze.common.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

/**
 * Description :
 * date : 2015-11-30 - 09:40
 *
 * @author : zaze
 * @version : 1.0
 */
public abstract class BaseListAdapter<V, H> extends BaseDataAdapter<V> implements ChildViewAdapter<V, H> {
    private OnItemClickListener<V> onItemClickListener;

    public BaseListAdapter(Context context, Collection<V> data) {
        super(context, data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        H itemHolder;
        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(getViewLayoutId(), parent, false);
                itemHolder = createViewHolder(convertView);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (H) convertView.getTag();
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, getItem(position), position);
                }
            });
            setViewData(getItem(position), itemHolder, position, convertView, parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    protected void onItemClick(View view, V value, int position) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(view, value, position);
        }
    }

    public void setOnItemClickListener(OnItemClickListener<V> clickListener) {
        this.onItemClickListener = clickListener;
    }
}
