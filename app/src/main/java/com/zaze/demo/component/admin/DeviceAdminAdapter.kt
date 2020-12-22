package com.zaze.demo.component.admin

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zaze.common.adapter.BaseRecyclerAdapter
import com.zaze.demo.R

/**
 * Description :
 * @author : zaze
 * @version : 2020-12-21 - 15:04
 */
class DeviceAdminAdapter(context: Context, data: Collection<AdminItem>?) : BaseRecyclerAdapter<AdminItem, DeviceAdminAdapter.AdminHolder>(context, data) {

    override fun getViewLayoutId(): Int {
        return R.layout.device_admin_item
    }

    override fun createViewHolder(convertView: View): AdminHolder {
        return AdminHolder(convertView)
    }

    override fun onBindView(holder: AdminHolder, value: AdminItem, position: Int) {
        holder.deviceAdminItemNameTv.text = value.name
        holder.deviceAdminItemNameTv.setOnClickListener {
            value.click.invoke()
        }
    }

    class AdminHolder(view: View) : RecyclerView.ViewHolder(view) {
        val deviceAdminItemNameTv = view.findViewById<TextView>(R.id.deviceAdminItemNameTv)
    }
}