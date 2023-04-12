package com.zaze.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

abstract class BaseArrayAdapter<V, H: IViewHolder<V>>(
    context: Context,
    list: MutableList<V>,
    private val resourceId: Int,
    private val createViewHolder: (view: View, value: V, position: Int) -> H
) : ArrayAdapter<V>(context, resourceId, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View =
            convertView ?: LayoutInflater.from(context).inflate(resourceId, parent, false)
        val value: V = getItem(position) ?: return view
        //
        val itemHolder: H = (view.tag as? H) ?: createViewHolder(view, value, position)
        itemHolder.onBindView(value, position)
        return view
    }
}