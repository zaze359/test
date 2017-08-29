package com.zaze.demo.component.readpackage.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.zaze.aarrepo.commons.base.ZBaseApplication
import com.zaze.aarrepo.commons.base.adapter.BaseItemHolder
import com.zaze.aarrepo.commons.base.adapter.BaseUltimateAdapter
import com.zaze.aarrepo.utils.AppUtil
import com.zaze.aarrepo.utils.StringUtil
import com.zaze.demo.R
import com.zaze.demo.model.entity.PackageEntity

/**
 * Description :

 * @author : ZAZE
 * *
 * @version : 2017-04-17 - 17:21
 */
class ReadPackageAdapter(context: Context, data: Collection<PackageEntity>) : BaseUltimateAdapter<PackageEntity, ReadPackageAdapter.PackageHolder>(context, data) {

    override fun getViewHolder(view: View, isItem: Boolean): PackageHolder {
        return PackageHolder(view, isItem)
    }

    override fun getViewLayoutId(): Int {
        return R.layout.list_item_app
    }

    override fun onBindViewHolder(holder: PackageHolder, value: PackageEntity, position: Int) {
        val packageName = StringUtil.parseString(value.packageName)
        // --------------------------------------------------
        holder.itemAppNameTv!!.text = "应用名 : ${value.appName}"
        holder.itemAppVersionCodeTv!!.text = "版本号 ：${value.versionCode}"
        holder.itemAppVersionNameTv!!.text = "版本名 ：${value.versionName}"
        holder.itemAppPackageTv!!.text = "包名 : $packageName"
        holder.itemAppDirTv!!.text = "路径 : ${value.sourceDir}"
        // --------------------------------------------------
        var drawable = AppUtil.getAppIcon(ZBaseApplication.getInstance(), packageName)
        if (drawable == null) {
            drawable = getDrawable(R.mipmap.ic_launcher)
        }
        holder.itemAppIv!!.setImageDrawable(drawable)
    }

    override fun generateHeaderId(position: Int): Long {
        return 0
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder? {
        return null
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    val packageListStr: String
        get() {
            val packageEntityList = dataList
            val builder = StringBuilder()
            for (entity in packageEntityList) {
                if (builder.isNotEmpty()) {
                    builder.append(",\n")
                }
//                builder.append("${entity.appName} : ${entity.packageName}")
                builder.append(entity.packageName)
            }
            return builder.toString()
        }

    inner class PackageHolder(itemView: View, isItem: Boolean) : BaseItemHolder(itemView, isItem) {
        var itemAppIv: ImageView? = null
        var itemAppNameTv: TextView? = null
        var itemAppPackageTv: TextView? = null
        var itemAppVersionCodeTv: TextView? = null
        var itemAppVersionNameTv: TextView? = null
        var itemAppDirTv: TextView? = null

        override fun initView(itemView: View) {
            itemAppIv = findView(view, R.id.item_app_iv)
            itemAppNameTv = findView(view, R.id.item_app_name_tv)
            itemAppPackageTv = findView(view, R.id.item_app_package_tv)
            itemAppVersionCodeTv = findView(view, R.id.item_app_version_code_tv)
            itemAppVersionNameTv = findView(view, R.id.item_app_version_name_tv)
            itemAppDirTv = findView(view, R.id.item_app_dir_tv)
        }
    }
}
