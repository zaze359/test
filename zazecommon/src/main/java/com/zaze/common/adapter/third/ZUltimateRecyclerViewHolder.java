package com.zaze.common.adapter.third;

import android.view.View;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-08-29 - 19:03
 */
public class ZUltimateRecyclerViewHolder extends UltimateRecyclerviewViewHolder {

    public ZUltimateRecyclerViewHolder(View itemView, boolean isItem) {
        super(itemView);
        if (isItem) {
            initView(itemView);
        }
    }

    protected void initView(View view) {
    }


}
