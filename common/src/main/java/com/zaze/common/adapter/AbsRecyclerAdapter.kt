package com.zaze.common.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Description :
 * date : 2016-01-19 - 14:06
 *
 * @author : zaze
 * @version : 1.0
 */
abstract class AbsRecyclerAdapter<T : Any, VH : RecyclerView.ViewHolder> : ListAdapter<T, VH> {
    val context: Context

    constructor(context: Context, diffCallback: DiffUtil.ItemCallback<T>) : super(diffCallback) {
        this.context = context
    }

    constructor(context: Context, config: AsyncDifferConfig<T>) : super(config) {
        this.context = context
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val v: T? = getItem(position)
        if (v != null) {
            onBindView(holder, v, position)
        }
    }

//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }


    // --------------------------------------------------

    /**
     * view赋值
     *
     * @param holder   holder
     * @param value    value
     * @param position position
     */
    abstract fun onBindView(holder: VH, value: T, position: Int)

    // --------------------------------------------------
    open fun <T : View?> findView(parentView: View, resId: Int): T {
        return parentView.findViewById(resId)
    }

    open fun getColor(resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }

    open fun getString(resId: Int, vararg args: Any?): String? {
        return context.getString(resId, *args)
    }

    open fun getDrawable(id: Int): Drawable? {
        return ContextCompat.getDrawable(context, id)
    }

    open fun getBitmap(resId: Int): Bitmap? {
        return BitmapFactory.decodeResource(context.resources, resId)
    }
}