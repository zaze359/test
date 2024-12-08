package com.zaze.demo

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.zaze.common.base.AbsViewModel
import com.zaze.common.thread.ThreadPlugins
import com.zaze.demo.component.floating.FloatingLayout
import com.zaze.demo.data.repository.DemoRepository
import com.zaze.demo.data.entity.TableEntity
import com.zaze.demo.debug.torrent.TorrentDownloader
import com.zaze.demo.debug.test.TestByJava
import com.zaze.demo.usagestats.AppUsageTest
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.libtorrent4j.TorrentInfo
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

//        P2pClient.init()
//        P2pClient.discovery()
//        val bencode = "ZDEzOmFubm91bmNlLWxpc3RsZTc6Y29tbWVudDc6Y29tbWVudDEwOmNyZWF0ZWQgYnk3OmNyZWF0b3IxMzpjcmVhdGlvbiBkYXRlaTE3MzM0ODUwMjdlNDppbmZvZDY6bGVuZ3RoaTQyMTcxOTAxZTQ6bmFtZTM2OjEyNjZiYWVhZDcxODQ5NDNiN2VmOWZjNzkzNmJiNTZkLmFwazEyOnBpZWNlIGxlbmd0aGk1MjQyODhlNjpwaWVjZXMxNjIwOu+/vRIbVO+/ve+/vX3vv71i77+977+9SkTvv70MHe+/vSZb77+9VWXvv73vv73vv71Eyavvv71Q77+977+977+9Lxrvv70qH23vv70K77+9WSjvv73vv73vv71B77+9IO+/ve+/vVJT77+9BDVgbmgzFO+/vd+677+9Ae+/ve+/vWrvv71rXQTvv73vv70Ydn0FVO+/vUYc2bfvv70f77+977+9eu+/vU4CRksKHgoWK++/vUTvv73Dm++/vXbvv73vv73vv7042bPvv73vv70DT1/vv70KNu+/vVhs77+977+9E++/vXJg77+977+9Wnfvv71Z2JlEAmjvv71/FO+/ve+/vQbvv70zZmxme++/ve+/ve+/vVnvv73vv73vv73vv73vv70w77+977+977+9Uyxy77+9CmLvv70XCHc4BhIC77+9JO+/vQox77+9JO+/vQ7vv70heFbvv73vv73vv73vv71VD35f77+9SjpbKmob77+9Pe+/vUtyCEXvv73vv73vv73vv71k77+9KgloWGPvv70Q77+977+977+9N++/vWHvv70J2aYT77+977+977+977+9c3oXNRzvv71+UTZVKu+/vTvvv70+MiZqa3tq77+977+9W++/vXhOP++/vci7PWrvv73vv73vv71m77+9TQrvv71Aw4fvv71y77+977+9PkQ977+977+9dCEW77+977+9A0NrN++/vWQjKWgK77+9G++/ve+/vRrvv73vv73vv71B77+9B++/vVnvv73vv71Z77+977+9dTwkF1Pvv73vv70777+9ae+/vQjvv70p77+9J++/ve+/ve+/ve+/vSLXpO+/vU9MUBzvv73vv70L77+977+9RTTuqqpCEHB3Nu+/vQ5CWO+/ve+/ve+/vTV2GnME77+9Cu+/vXJs77+9Cu+/vWhBd++/vd+cKnvvv71X77+977+977+977+9BO+/ve+/vX5cAnzvv71Y77+977+977+9wqbvv70f77+9KB9W77+9I++/ve+/ve+/vRIade+/vSrvv70s77+9Ze+/vXbvv71saxPPlgrvv71sBu+/vSnvv71L77+9Ezky77+977+977+9Ze+/ve+/ve+/ve+/ve+/ve+/vWDvv70e77+977+9TTPvv70r77+977+9He+/vXbvv71fX++/vRFhf++/vX7vv73vv73vv70m77+9PWLvv73Fv++/vWzvv70sYGNz77+9cmHvv70wFjTvv73vv70ydStc77+9c++/vR/vv73vv73vv73vv73vv73vv71BDO+/ve+/vWnvv70977+9aHk477+9IO+/vSNzOh/vv70e77+9IQTvv73vv70M77+9B++/vVxb77+9S++/ve+/vUQrBu+/ve+/vQnvv70pUO+/ve+/ve+/vWZR77+9BH/vv73vv70FP0Tvv71qSVjvv70777+9CXTvv70377+977+9TO+/vQl8b3tB77+90JLvv70K77+9C1om77+977+9Ge+/ve+/vW/vv71KclXvv70eMzgrMS3Kvlsz77+977+977+9Ej4/77+977+9SjPvv70PeAfvv71877+9S3Xvv73vv73vv70nYgjvv71wKe+/vV1yY++/vWZkEVAG77+977+9Ee+/vTF2Cm3vv73vv70f77+9bT7vv73Elu+/vXLvv71ieErvv73vv71x77+9LStd77+9Y++/vW/vv70tIO+/ve+/vV/vv73vv71jZ++/vW7vv71m77+977+9Pe+/vdK677+9ExE777+9Fu+/ve+/vQfvv73vv73vv70l77+9ae+/ve+/vUDvv70fAe+/ve+/vRE977+9MR7vv73vv71d77+977+9KQxP77+977+9F++/vV4Z77+977+9aO+/vTYTK++/vT3vv73Mi++/ve+/vX0ge++/ve+/vTLvv73vv73vv73vv7163LV4b2Xvv71Q77+9Iu+/ve+/ve+/vcy177+977+977+9DAZ8KQvvv73vv73vv71GOO+/ve+/vXPvv73vv70eOe+/vQcvKA8+77+977+9GDd7PO+/ve+/vXZ477+9Be+/ve+/ve+/vVrvv71977+977+977+9VVZuIe+/ve+/vR/vv73vv71F77+977+9yoNdHe+/ve+/vTFt77+9MO+/ve+/vey9gzoh77+977+9elU877+977+977+977+977+977+965uUW3MC4ZS8TwdED++/ve+/vRrvv73vv73vv70n77+977+9E96377+9EnLvv73vv73vv73vv710LWnvv70iRhLvv70fPE5k77+9AUd877+9NjJS77+977+9Lu+/ve+/vXJJ77+977+977+977+9Xu+/vRwq77+9JCQDXFjvv73vv70o77+9dBI877+9VO+/vX8077+9A++/vcOd77+9GO+/vV5P77+9Wgvvv73vv73vv73vv73vv73bgCfvv73vv70K77+9Y1Md77+977+9a++/ve+/vSrvv713XwwKQe+/ve+/vSDvv73vv70qAe+/vQvvv73vv70Oc05RPu+/vQTvv70Lw5Lvv71W77+977+9Re+/vVnKtX7vv73vv73vv73vv70f77+9VO+/vS0177+9bQnvv73vv71HK21dQ++/vVxxSFQ577+9I37vv71WED4/Uu+/vWAD77+9fm3vv73vv70V77+9RVU9ZO+/ve+/vXfvv70o77+977+977+9cu+/ve+/vdCyAe+/ve+/ve+/ve+/vXLvv71W77+977+977+9EkJXHidgMAPvv71t77+977+977+9egN6ZO+/ve+/vXnvv71zdXsH77+9WO+/vRUIGu+/ve+/vQ5UMe+/vTnvv70eYjtvdciq77+977+977+9H++/vSQMd++/ve+/vT0h77+977+977+977+9Q37vv71A77+977+9Gu+/ve+/ve+/vd2E77+9Ou+/ve+/vQ/vv71A7KuxSO+/vRU/77+977+977+977+977+9Uu+/vRspBe+/ve+/ve+/ve+/vRJg77+977+9YWXvv71y77+9aVpE77+9LFjvv71WOiPvv704DwPvv71077+977+91Y9l77+9Te+/vWLvv73vv70B77+9VWN+77+9Je+/vXAJC++/ve+/vWXvv70hKifvv70P77+9eO+/ve+/vQnvv71wM++/ve+/vT4177+977+9PAtVKe+/vcS777+977+977+977+9e2Lvv701Fu+/vW/vv73vv70G77+977+977+977+9Ae+/ve+/ve+/ve+/vR5U77+9WTHvv70VRe+/ve+/vS/vv70M77+9PO+/ve+/ve+/vSXvv73vv71/z4huKe+/vREnQe+/vVTvv71B77+977+9XO+/vQHFmghu77+977+977+9dBvvv73vv717dAYyVElGYe+/vUB277+9T17vv70KBgUP77+9IG0jB++/vSzvv70C77+977+9dwjvv73vv70j77+977+9eO+/ve+/ve+/vRQ9xZfvv73vv71v77+9XDkq77+977+977+977+9eO+/ve+/ve+/ve+/vWTvv73vv73vv73vv73vv70T77+977+9Clhm77+977+977+9aknvv70C77+9Su+/vXcS77+9K++/vQ/vv70K77+9SWHvv73vv73vv70Iancg77+9OO+/vW52NDnJrC/vv71h77+977+977+9eX4u77+977+977+977+9FwPvv70ONBMwW0bvv70s77+9WVcVE++/vXgkHe+/vTlodEIn77+977+9fe+/ve+/vTUs77+977+9dVwhVu+/vWTvv73vv71qUUoZA++/ve+/vX3vv73vv73vv70d77+9W3kzBgo577+977+977+9HCzvv71ja++/vScj77+9J1/vv71fFe+/ve+/vQNlNDpuYW1lMzY6MTI2NmJhZWFkNzE4NDk0M2I3ZWY5ZmM3OTM2YmI1NmQuYXBrNTpub2Rlc2xsMTM6MTcyLjIxLjMzLjEyMmk2ODgxZWVsMTI6MTcyLjIxLjMzLjY0aTY4ODFlZWwxMjoxNzIuMjEuMzMuNDFpNjg4MWVlbDEyOjE3Mi4yMS4zMy4zMGk2ODgxZWVlODp1cmwtbGlzdDk3Omh0dHA6Ly9qemp4LXRlc3QtcmVzb3VyY2UuaGFpbGlhbmdlZHUuY29tL290aGVycy8yMDI0LzExLzI1LzEyNjZiYWVhZDcxODQ5NDNiN2VmOWZjNzkzNmJiNTZkLmFwa2U="
        val torrentFile = File("/sdcard/Android/data/com.zaze.demo/files/a.torrent")
        val downloadPath = "/sdcard/Android/data/com.zaze.demo/files/test/"
        FileUtil.reCreateDir(downloadPath)
        ThreadPlugins.runInIoThread({
            TorrentDownloader.getInstance().startDownload(
                torrentFile,
                null,
                downloadPath,
                emptyArray()
            )
        })


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
