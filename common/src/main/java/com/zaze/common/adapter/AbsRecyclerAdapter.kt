package com.zaze.common.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zaze.core.designsystem.theme.ext.isColorLight
import com.zaze.utils.ext.getBitmap
import com.zaze.utils.ext.getColor
import com.zaze.utils.ext.getDrawable
import com.zaze.utils.ext.getString

/**
 * Description :
 * date : 2016-01-19 - 14:06
 *
 * @author : zaze
 * @version : 1.0
 */
abstract class AbsRecyclerAdapter<V : Any, VH : RecyclerView.ViewHolder> : ListAdapter<V, VH> {
    val context: Context

    constructor(context: Context, diffCallback: DiffUtil.ItemCallback<V>? = null) : super(
        diffCallback ?: object :
            DiffUtil.ItemCallback<V>() {
            // 对象是否相同
            override fun areItemsTheSame(oldItem: V, newItem: V): Boolean {
                return oldItem == newItem
            }

            // 内容是否相同
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: V, newItem: V): Boolean {
                return oldItem.toString() == newItem.toString()
            }
        }) {
        this.context = context
    }

    constructor(context: Context, config: AsyncDifferConfig<V>) : super(config) {
        this.context = context
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val v: V? = getItem(position)
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
    abstract fun onBindView(holder: VH, value: V, position: Int)

    // --------------------------------------------------
    open fun <T : View?> findView(parentView: View, resId: Int): T {
        return parentView.findViewById(resId)
    }

    @ColorInt
    open fun getColor(@ColorRes resId: Int): Int {
        return resId.getColor(context)
    }

    open fun getString(@StringRes resId: Int, vararg args: Any?): String? {
        return resId.getString(context, args)
    }

    open fun getDrawable(@DrawableRes resId: Int): Drawable? {
        return resId.getDrawable(context)
    }

    open fun getBitmap(@DrawableRes resId: Int): Bitmap? {
        return resId.getBitmap(context)
    }
}