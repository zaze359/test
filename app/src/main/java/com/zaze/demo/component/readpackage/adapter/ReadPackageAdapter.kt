package com.zaze.demo.component.readpackage.adapter

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zaze.common.adapter.BaseRecyclerAdapter
import com.zaze.common.base.BaseApplication
import com.zaze.demo.R
import com.zaze.demo.debug.InvariantDeviceProfile
import com.zaze.demo.model.entity.PackageEntity
import com.zaze.utils.AppUtil
import com.zaze.utils.BmpUtil
import com.zaze.utils.ZStringUtil

/**
 * Description :

 * @author : ZAZE
 * *
 * @version : 2017-04-17 - 17:21
 */
class ReadPackageAdapter(context: Context, data: Collection<PackageEntity>) : BaseRecyclerAdapter<PackageEntity, ReadPackageAdapter.PackageHolder>(context, data) {

    val iconDpi: Int

    val iconSize = 72

    init {
        iconDpi = InvariantDeviceProfile().getLauncherIconDensity(iconSize)
    }

    override fun createViewHolder(convertView: View): PackageHolder {
        return PackageHolder(convertView)
    }

    override fun getViewLayoutId(): Int {
        return R.layout.app_recycle_item
    }

    override fun onBindView(holder: PackageHolder, value: PackageEntity, position: Int) {
        val packageName = ZStringUtil.parseString(value.packageName)
        // --------------------------------------------------
        holder.itemAppNameTv!!.text = "应用名 : ${value.appName}"
        holder.itemAppVersionCodeTv!!.text = "版本号 ：${value.versionCode}"
        holder.itemAppVersionNameTv!!.text = "版本名 ：${value.versionName}"
        holder.itemAppPackageTv!!.text = "包名 : $packageName"
        holder.itemAppDirTv!!.text = "路径 : ${value.sourceDir}"
        // --------------------------------------------------
        var drawable: Drawable? = null
        val application = AppUtil.getApplicationInfo(context, packageName)
        if (application != null) {
            var resources: Resources?
            try {
                resources = context.packageManager.getResourcesForApplication(application)
            } catch (e: PackageManager.NameNotFoundException) {
                resources = null
            }
            if (resources != null) {
                drawable = AppUtil.getAppIcon(resources, application.icon, iconDpi)
            }
        }
        if (drawable == null) {
            drawable = AppUtil.getAppIcon(BaseApplication.getInstance(), packageName)
        }
        if (drawable == null) {
            drawable = getDrawable(R.mipmap.ic_launcher)
        }
        holder.itemAppIv!!.setImageBitmap(BmpUtil.drawable2Bitmap(drawable, iconSize))
    }

    inner class PackageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemAppIv: ImageView? = null
        var itemAppNameTv: TextView? = null
        var itemAppPackageTv: TextView? = null
        var itemAppVersionCodeTv: TextView? = null
        var itemAppVersionNameTv: TextView? = null
        var itemAppDirTv: TextView? = null

        init {
            itemAppIv = itemView.findViewById(R.id.item_app_iv)
            itemAppNameTv = itemView.findViewById(R.id.item_app_name_tv)
            itemAppPackageTv = itemView.findViewById(R.id.item_app_package_tv)
            itemAppVersionCodeTv = itemView.findViewById(R.id.item_app_version_code_tv)
            itemAppVersionNameTv = itemView.findViewById(R.id.item_app_version_name_tv)
            itemAppDirTv = itemView.findViewById(R.id.item_app_dir_tv)
        }
    }
}
