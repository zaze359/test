package com.zaze.demo.util

import android.content.pm.ApplicationInfo
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zaze.common.adapter.BaseRecyclerAdapter

/**
 * RecyclerView
 * app:items --- Collection<V>
 * BaseRecyclerAdapter.setDataList()
 */
@BindingAdapter("items")
fun <V> RecyclerView.setData(items: Collection<V>?) {
    adapter?.let {
        if (adapter is BaseRecyclerAdapter<*, *>) {
            (it as BaseRecyclerAdapter<V, *>).setDataList(items)
        }
    }
}