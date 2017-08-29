package com.zaze.aarrepo.commons.base.adapter

import android.view.View

/**
 * Description :

 * @author : ZAZE
 * *
 * @version : 2017-03-10 - 11:04
 */
abstract class BaseItemHolder(itemView: View, isItem: Boolean) : UltimateRecyclerviewViewHolder<View>(itemView) {

    init {
        if (isItem) {
            initView(itemView)
        }
    }

    protected abstract fun initView(itemView: View)
}
