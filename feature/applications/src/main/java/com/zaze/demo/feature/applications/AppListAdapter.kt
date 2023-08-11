package com.zaze.demo.feature.applications

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zaze.common.adapter.AbsRecyclerAdapter
import com.zaze.common.base.BaseApplication
import com.zaze.utils.*
import com.zaze.utils.date.DateUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File

/**
 * Description :
 * @author : ZAZE
 *
 * @version : 2017-04-17 - 17:21
 */
class AppListAdapter(context: Context) :
    AbsRecyclerAdapter<AppShortcut, AppListAdapter.PackageHolder>(context, diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<AppShortcut>() {
            override fun areItemsTheSame(oldItem: AppShortcut, newItem: AppShortcut): Boolean {
                return oldItem.packageName == newItem.packageName
            }
            override fun areContentsTheSame(oldItem: AppShortcut, newItem: AppShortcut): Boolean {
                return oldItem.toString() == newItem.toString()
            }

        }
        private const val iconSize = 72
        private val iconDpi by lazy { InvariantDeviceProfile().getLauncherIconDensity(iconSize) }
    }

    override fun createViewHolder(convertView: View): PackageHolder {
        return PackageHolder(convertView)
    }

    override fun getViewLayoutId(): Int {
        return R.layout.item_app_list
    }

    override fun onBindView(holder: PackageHolder, value: AppShortcut, position: Int) {
        val packageName = ZStringUtil.parseString(value.packageName)
        // --------------------------------------------------
        holder.itemAppNumTv.text = "${position + 1}"
        if (value.isInstalled && !value.sourceDir.isNullOrEmpty()) {
            holder.itemAppExportBtn.visibility = View.VISIBLE
            holder.itemAppExportBtn.setOnClickListener {
                val path = "/sdcard/zaze/apk/${value.appName}(${value.packageName}).apk"
                FileUtil.copy(File(value.sourceDir), File(path))
                ToastUtil.toast(context, "成功复制到 $path")
            }
        } else {
            holder.itemAppExportBtn.visibility = View.GONE
        }

        holder.itemAppNameTv.text = "应用名 : ${value.appName}"
        holder.itemAppVersionCodeTv.text = "版本号 ：${value.versionCode}"
        holder.itemAppVersionNameTv.text = "版本名 ：${value.versionName}"
        holder.itemAppPackageTv.text = "包名 : $packageName"
        holder.itemAppDirTv.text = "路径 : ${value.sourceDir}"
        holder.itemAppSignTv.text = "签名 : ${value.getSignatures(context)}"
        holder.itemAppTimeTv.text =
            "应用时间 : ${formatTime(value.firstInstallTime)}/${formatTime(value.lastUpdateTime)}"
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
            drawable = getDrawable(R.drawable.ic_app_default)
        }
//        Settings.ACTION_WIFI_SETTINGS
        holder.itemAppIv.setImageBitmap(BmpUtil.drawable2Bitmap(drawable, iconSize))
        holder.itemView.setOnClickListener {
            AppUtil.getAppMetaData(context, packageName)?.let { bundle ->
                bundle.keySet().forEach {
                    ZLog.i(ZTag.TAG, "$it >> ${bundle.get(it)}")
                }
            }
//            AppUtil.startApplication(context, packageName)
        }
    }

    private fun formatTime(timeMillis: Long): String {
        return DateUtil.timeMillisToString(timeMillis, "yyyy-MM-dd HH:mm:ss")
    }

    inner class PackageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemAppNumTv: TextView = itemView.findViewById(R.id.item_app_num_tv)
        var itemAppExportBtn: Button = itemView.findViewById(R.id.item_app_export_btn)
        var itemAppIv: ImageView = itemView.findViewById(R.id.item_app_iv)
        var itemAppNameTv: TextView = itemView.findViewById(R.id.item_app_name_tv)
        var itemAppPackageTv: TextView = itemView.findViewById(R.id.item_app_package_tv)
        var itemAppVersionCodeTv: TextView = itemView.findViewById(R.id.item_app_version_code_tv)
        var itemAppVersionNameTv: TextView = itemView.findViewById(R.id.item_app_version_name_tv)
        var itemAppDirTv: TextView = itemView.findViewById(R.id.item_app_dir_tv)
        var itemAppSignTv: TextView = itemView.findViewById(R.id.item_app_sign_tv)
        var itemAppTimeTv: TextView = itemView.findViewById(R.id.item_app_time_tv)
    }
}
