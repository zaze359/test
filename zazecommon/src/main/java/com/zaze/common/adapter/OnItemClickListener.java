package com.zaze.common.adapter;

import android.view.View;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-12-12 - 00:46
 */
public interface OnItemClickListener<V> {
    /**
     * 点击item回调
     *
     * @param view     view
     * @param value    value
     * @param position position
     */
    void onItemClick(View view, V value, int position);
}
