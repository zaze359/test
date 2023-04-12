package com.zaze.common.adapter

import android.view.View

interface IViewHolder<V> {

    fun onBindView(value: V, position: Int)

}