package com.zaze.demo.util

import android.content.pm.ApplicationInfo
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zaze.common.adapter.BaseRecyclerAdapter

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-27 - 09:24
 */
val ApplicationInfo.isSystemApp: Boolean
    get() = this.flags and ApplicationInfo.FLAG_SYSTEM > 0

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