package com.zaze.common.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-06-03 - 17:15
 */
interface ChildViewAdapter<V, H> {
    /**
     * view 赋值
     *
     * @param value       value
     * @param itemHolder  itemHolder
     * @param position    position
     * @param convertView convertView
     * @param parent      parent
     */
    void setViewData(V value, H itemHolder, int position, View convertView, ViewGroup parent);

    /**
     * get view layout id
     *
     * @return int
     */
    int getViewLayoutId();

    /**
     * 构建 viewHolder
     *
     * @param convertView
     * @return H
     */
    H createViewHolder(View convertView);
}
