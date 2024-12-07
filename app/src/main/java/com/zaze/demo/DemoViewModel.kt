package com.zaze.demo

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfRenderer
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.zaze.common.base.AbsViewModel
import com.zaze.demo.component.floating.FloatingLayout
import com.zaze.demo.component.p2p.P2pClient
import com.zaze.demo.data.repository.DemoRepository
import com.zaze.demo.data.entity.TableEntity
import com.zaze.demo.debug.test.TestByJava
import com.zaze.demo.usagestats.AppUsageTest
import com.zaze.utils.FileUtil
import com.zaze.utils.TraceHelper
import com.zaze.utils.ext.writeToFileLimited
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

private const val s = ""

@HiltViewModel
class DemoViewModel @Inject constructor(private val demoRepository: DemoRepository) :
    AbsViewModel() {

    private val demoList = MutableLiveData<List<TableEntity>>()
    val demosData: LiveData<List<TableEntity>> = demoList

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            demoList.postValue(demoRepository.loadDemos())
        }
    }

    private var appUsageTest: AppUsageTest? = null
    private var floatingLayout: FloatingLayout? = null
    fun test(activity: Activity) {
        val deviceName = Settings.Global.getString(activity.contentResolver, "device_name")
        ZLog.i(ZTag.TAG_DEBUG, "deviceName：${deviceName}")

        P2pClient.init()
        P2pClient.discovery()


//        viewModelScope.launch(Dispatchers.IO) {
//            val pdfFile = File("/sdcard/pdf/sample.pdf")
//            if (FileUtil.exists(pdfFile)) {
//                TraceHelper.trace("PdfRenderer to image", {
//                    try {
//                        val pdfRenderer = PdfRenderer(
//                            ParcelFileDescriptor.open(
//                                pdfFile,
//                                ParcelFileDescriptor.MODE_READ_WRITE
//                            )
//                        )
//                        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//                        ZLog.i(ZTag.TAG_DEBUG, "pdf 开始解析：${pdfRenderer.pageCount}")
//                        repeat(pdfRenderer.pageCount) { index ->
//                            val page = pdfRenderer.openPage(index)
//                            val ptBase = 72F // 1pt = 72英寸
////                            val width = page.width
////                            val height = page.height
//                            val width =
//                                (page.width / ptBase * activity.resources.displayMetrics.densityDpi).toInt()
//                            val height =
//                                (page.height / ptBase * activity.resources.displayMetrics.densityDpi).toInt()
//                            val outFile =
//                                File("${activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath}/pdf/pdf_$index.png")
//                            ZLog.i(
//                                ZTag.TAG_DEBUG,
//                                "pdf page($index): ${width}x$height; outFile >> ${outFile.absolutePath}"
//                            )
//                            //
//                            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//                            val sCanvas = Canvas(bitmap)
//                            sCanvas.drawColor(Color.WHITE)
//                            sCanvas.drawBitmap(bitmap, 0F, 0F, paint)
//                            val rect = Rect(0, 0, width, height)
//                            // 渲染到 bitmap 上
//                            page.render(bitmap, rect, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
//                            // 保存到本地
//                            bitmap.writeToFileLimited(
//                                outFile = outFile,
//                                format = Bitmap.CompressFormat.PNG,
//                                limitSize = 200 * 1024
//                            )
//                            page.close()
//                        }
//                        pdfRenderer.close()
//                        ZLog.i(ZTag.TAG_DEBUG, "pdf finish")
//                    } catch (e: Throwable) {
//                        e.printStackTrace()
//                    }
//                })
//            }
//        }
        // ----------------------------------------------------------------------
        // ----------------------------------------------------------------------
////        WallpaperManager.getInstance(activity).setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.jljt))
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            WallpaperManager.getInstance(activity).clearWallpaper()
//        } else {
//            WallpaperManager.getInstance(activity).clear()
//        }

        // ----------------------------------------------------------------------
//        val builder = TimePickerWidget.Builder()
//            .startPicker("00:00", "23:59")
//            .endPicker("00:00", "23:59")
//        builder.title("修改时段")
//        builder.beginAt("02:00")
//            .endAt("06:00")
//        builder.build(activity, object : TimePickerWidget.TimePickerListener {
//            override fun onCancel() {}
//            override fun onSure(dialog: Dialog, beginTime: Long, endTime: Long) {
//                dialog.dismiss()
//            }
//        }).apply {
//            show()
//        }

//        if (appUsageTest == null) {
//            appUsageTest = AppUsageTest(activity).apply {
//                run()
//            }
//        }
//
//        if (SystemSettings.canDrawOverlays(activity)) {
//            floatingLayout?.hide()
//            floatingLayout = FloatingLayout(activity).apply {
//                addView(LayoutInflater.from(context).inflate(R.layout.floating_view, this, false))
//            }
//            floatingLayout?.show()
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            activity.startActivity(IntentFactory.SpecialPermission.manageOverlayPermission())
//        }

//
//
//        FloatingManager.getInstance(requireActivity())
//            .addView(TextView(requireActivity()).apply {
//                this.text = "floatingView"
//            }, WindowManager.LayoutParams(200, 100))


        listOf(
            TestByJava(),
//            TestBattery(),
//            TestFile(),
//            TestUserHandle(),
//            TestCommand(),
        ).forEach {
            it.doTest(activity)
        }

        // --------------------------------------------------
        Snackbar.make(activity.window.decorView, "LENGTH_INDEFINITE", Snackbar.LENGTH_LONG)
//            .setTextColor(Color.WHITE)
            .setAction("action") { }
//            .setActionTextColor(Color.RED)
            .show()
//        Snackbar.make(activity.window.decorView, "", Snackbar.LENGTH_LONG)
//        Snackbar.make(activity.window.decorView, "", Snackbar.LENGTH_SHORT)

        // --------------------------------------------------
        // --------------------------------------------------

//            val wakeLockTask = PowerLockTask(this@MainActivity)
//            Thread(wakeLockTask).start()
//            Thread(AlarmTask(this)).start()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
//                AppUtil.getInstalledApplications(this).forEach {
//                    ZLog.i(ZTag.TAG, "${it.packageName} isIgnoringBatteryOptimizations: ${pm.isIgnoringBatteryOptimizations(it.packageName)}")
//                }
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                startActivity(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).also {
//                    it.data = Uri.parse("package:" + this.getPackageName())
//                })
//            }
    }

    fun getDefaultLauncher(context: Context) {
        val activities = ArrayList<ComponentName>()
        context.packageManager.getPreferredActivities(arrayListOf(IntentFilter(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }), activities, null)
        for (componentName in activities) {
            Log.i("getPreferredActivities", "componentName : $componentName")
        }
        val defaultLauncher =
            context.packageManager.resolveActivity(Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
            }, PackageManager.MATCH_DEFAULT_ONLY)

        Log.i("defaultLauncher", "defaultLauncher : ${defaultLauncher?.activityInfo?.packageName}")
    }
}

//object DemoViewModelFactory {
//    fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//        @Suppress("UNCHECKED_CAST")
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return DemoViewModel(DemoRepository(Dispatchers.IO)) as T
//        }
//    }
//}
