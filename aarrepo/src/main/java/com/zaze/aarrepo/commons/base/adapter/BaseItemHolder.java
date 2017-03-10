package com.zaze.aarrepo.commons.base.adapter;

import android.view.View;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-10 - 11:04
 */
public abstract class BaseItemHolder extends UltimateRecyclerviewViewHolder {

    public BaseItemHolder(View itemView, boolean isItem) {
        super(itemView);
        if (isItem) {
            initView(itemView);
        }
    }

    public BaseItemHolder(View itemView) {
        this(itemView, true);
    }

    protected abstract void initView(View itemView);
}
