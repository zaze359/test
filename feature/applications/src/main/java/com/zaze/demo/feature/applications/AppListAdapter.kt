package com.zaze.demo.feature.applications

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zaze.common.adapter.AbsRecyclerAdapter
import com.zaze.demo.feature.applications.databinding.ItemAppListBinding
import com.zaze.utils.*
import com.zaze.utils.compress.ZipUtil
import com.zaze.utils.date.DateUtil
import com.zaze.utils.ext.BitmapExt
import com.zaze.utils.ext.isAAB
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

    override fun onBindView(holder: PackageHolder, value: AppShortcut, position: Int) {
        val packageName = ZStringUtil.parseString(value.packageName)
        // --------------------------------------------------
        val binding = holder.binding
        binding.itemAppNumTv.text = "${position + 1}"
        val sourceDir = value.applicationInfo?.sourceDir
        if (value.isInstalled && !sourceDir.isNullOrEmpty()) {
            binding.itemAppExportBtn.visibility = View.VISIBLE
            binding.itemAppExportBtn.setOnClickListener {
                val out = exportInstalledApp(value)
                ToastUtil.toast(context, "备份至: $out")
            }
        } else {
            binding.itemAppExportBtn.visibility = View.GONE
        }
        binding.itemAppNameTv.text = "应用名 : ${value.appName}"
        binding.itemAppVersionCodeTv.text = "版本号 ：${value.versionCode}"
        binding.itemAppVersionNameTv.text = "版本名 ：${value.versionName}"
        binding.itemAppPackageTv.text = "包名 : $packageName"
        binding.itemAppDirTv.text = "路径 : ${sourceDir}"
        binding.itemAppSignTv.text = "签名 : ${value.getSignatures(context)}"
        binding.itemAppTimeTv.text =
            "应用时间 : ${formatTime(value.firstInstallTime)}/${formatTime(value.lastUpdateTime)}"
        // --------------------------------------------------
        val drawable = AppUtil.getAppIcon(context, packageName, iconDpi)
        val bitmap = if (drawable == null) {
            BitmapExt.decodeToBitmap(iconSize, iconSize) {
                BitmapFactory.decodeResource(context.resources, R.drawable.ic_app_default, it)
            }
        } else {
            BmpUtil.drawable2Bitmap(drawable, iconSize)
        }
//        Settings.ACTION_WIFI_SETTINGS
        binding.itemAppIv.setImageBitmap(bitmap)
        binding.root.setOnClickListener {
//            AppUtil.getAppMetaData(context, packageName)?.let { bundle ->
//                bundle.keySet().forEach {
//                    ZLog.i(ZTag.TAG, "$it >> ${bundle.get(it)}")
//                }
//            }
            //
//            AppUtil.startApplication(context, packageName)

            ZLog.i(
                ZTag.TAG,
                "getInstallerPackageName: ${
                    context.packageManager.getInstallerPackageName(packageName)
                }"
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val installSourceInfo = context.packageManager.getInstallSourceInfo(packageName)
                ZLog.i(
                    ZTag.TAG,
                    "installingPackageName: ${installSourceInfo.installingPackageName}"
                )
                ZLog.i(
                    ZTag.TAG,
                    "initiatingPackageName: ${installSourceInfo.initiatingPackageName}"
                )
                ZLog.i(
                    ZTag.TAG,
                    "originatingPackageName: ${installSourceInfo.originatingPackageName}"
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ZLog.i(ZTag.TAG, "packageSource: ${installSourceInfo.packageSource}")
                }
                ZLog.i(
                    ZTag.TAG,
                    "initiatingPackageSigningInfo: ${installSourceInfo.initiatingPackageSigningInfo}"
                )
            }
//            value.packageInfo?.splitNames?.forEach {
//                ZLog.i(ZTag.TAG, "packageInfo splitNames: $it")
//            }
            ZLog.i(ZTag.TAG, "applicationInfo sourceDir: ${value.applicationInfo?.publicSourceDir}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                value.applicationInfo?.let {
//                    it.splitNames?.forEach {
//                        ZLog.i(ZTag.TAG, "applicationInfo splitNames: $it")
//                    }
                    it.splitSourceDirs?.forEach {
                        ZLog.i(ZTag.TAG, "applicationInfo splitSourceDirs: $it")
                    }
//                    it.splitPublicSourceDirs?.forEach {
//                        ZLog.i(ZTag.TAG, "applicationInfo splitPublicSourceDirs: $it")
//                    }

                }
            }
        }
    }

    fun exportInstalledApp(value: AppShortcut): String {

        val outApkPath = "${context.getExternalFilesDir(null)}/zaze/apk/${value.packageName}/${value.packageName}_${value.versionCode}.apk"
        value.applicationInfo?.let { app ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && app.isAAB) {
                val sourceFiles = app.splitSourceDirs.toMutableList()
                sourceFiles.add(app.sourceDir)
                // 是 aab, 导出为 apks
                ZipUtil.compressFiles(sourceFiles.filter { !it.isNullOrEmpty() }
                    .map { File(it) }.toTypedArray(), "${outApkPath}s")
                return "${outApkPath}s"
            } else {
                FileUtil.copy(File(app.sourceDir), File(outApkPath))
            }
        }
        return outApkPath
    }


    private fun formatTime(timeMillis: Long): String {
        return DateUtil.timeMillisToString(timeMillis, "yyyy-MM-dd HH:mm:ss")
    }

    inner class PackageHolder(val binding: ItemAppListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageHolder {
        return PackageHolder(
            ItemAppListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}
