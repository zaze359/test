package com.zaze.demo.component.application

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zaze.common.adapter.BaseRecyclerAdapter
import com.zaze.common.base.BaseApplication
import com.zaze.common.util.ActivityUtil
import com.zaze.demo.R
import com.zaze.demo.debug.AppShortcut
import com.zaze.demo.debug.InvariantDeviceProfile
import com.zaze.utils.AppUtil
import com.zaze.utils.BmpUtil
import com.zaze.utils.ZStringUtil

/**
 * Description :

 * @author : ZAZE
 * *
 * @version : 2017-04-17 - 17:21
 */
class AppAdapter(context: Context, data: Collection<AppShortcut>) : BaseRecyclerAdapter<AppShortcut, AppAdapter.PackageHolder>(context, data) {

    private val iconDpi: Int

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

    override fun onBindView(holder: PackageHolder, value: AppShortcut, position: Int) {
        val packageName = ZStringUtil.parseString(value.packageName)
        // --------------------------------------------------
        holder.itemAppNameTv.text = "应用名 : ${value.name}"
        holder.itemAppVersionCodeTv.text = "版本号 ：${value.versionCode}"
        holder.itemAppVersionNameTv.text = "版本名 ：${value.versionName}"
        holder.itemAppPackageTv.text = "包名 : $packageName"
        holder.itemAppDirTv.text = "路径 : ${value.sourceDir}"
        holder.itemAppSignTv.text = "签名 : ${value.signingInfo}"
        // --------------------------------------------------
        var drawable: Drawable? = null
        val application = AppUtil.getApplicationInfo(context, packageName)
        if (application != null) {
            val resources = try {
                context.packageManager.getResourcesForApplication(application)
            } catch (e: PackageManager.NameNotFoundException) {
                null
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
        holder.itemAppIv.setImageBitmap(BmpUtil.drawable2Bitmap(drawable, iconSize))
        holder.itemView.setOnClickListener {
            if (!AppUtil.startApplication(context, packageName, null, false)) {
                // TODO 测试代码
                val intent = Intent(Intent.ACTION_MAIN, Uri.parse("$packageName://main"))
                intent.setPackage(packageName)
                val resolveInfos = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                if (resolveInfos != null && !resolveInfos.isEmpty()) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    ActivityUtil.startActivity(context, intent)
                }
            }
        }
    }

    inner class PackageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemAppIv: ImageView = itemView.findViewById(R.id.item_app_iv) as ImageView
        var itemAppNameTv: TextView = itemView.findViewById(R.id.item_app_name_tv) as TextView
        var itemAppPackageTv: TextView = itemView.findViewById(R.id.item_app_package_tv) as TextView
        var itemAppVersionCodeTv: TextView = itemView.findViewById(R.id.item_app_version_code_tv) as TextView
        var itemAppVersionNameTv: TextView = itemView.findViewById(R.id.item_app_version_name_tv) as TextView
        var itemAppDirTv: TextView = itemView.findViewById(R.id.item_app_dir_tv) as TextView
        var itemAppSignTv: TextView = itemView.findViewById(R.id.item_app_sign_tv) as TextView

    }
}
