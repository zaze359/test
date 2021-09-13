package com.zaze.demo.util

import android.content.pm.ApplicationInfo

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-27 - 09:24
 */
fun ApplicationInfo.isSystemApp(): Boolean {
    return this.flags and ApplicationInfo.FLAG_SYSTEM > 0
}

/**
 * RecyclerView
 * app:items --- Collection<V>
 * BaseRecyclerAdapter.setDataList()
 */
//@BindingAdapter("app:items")
//fun <V> RecyclerView.setData(items: Collection<V>?) {
//    adapter?.let {
//        if (adapter is BaseRecyclerAdapter<*, *>) {
//            (it as BaseRecyclerAdapter<V, *>).setDataList(items)
//        }
//    }
//}