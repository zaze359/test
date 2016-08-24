package com.zz.library.commons.adapter;

import android.content.Context;
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
public abstract class SimpleListAdapter<V, H> extends DataAdapter<V> implements ChildViewAdapter<V, H> {
    private OnItemClickListener onItemClickListener;

    //
    public SimpleListAdapter(Context context, Collection<V> data) {
        super(context, data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        H itemHolder;
        try {
            if (convertView == null) {
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
                    if (onItemClickListener != null) {
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
}
