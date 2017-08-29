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
    void setViewData(V value, H itemHolder, int position, View convertView, ViewGroup parent);

    int getViewLayoutId();

    H createViewHolder(View convertView);
}
