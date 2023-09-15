package com.zaze.demo

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.zaze.common.base.AbsViewModel
import com.zaze.demo.component.floating.FloatingLayout
import com.zaze.utils.permission.PermissionHandler
import com.zaze.demo.data.repository.DemoRepository
import com.zaze.demo.data.entity.TableEntity
import com.zaze.demo.debug.test.TestByJava
import com.zaze.demo.usagestats.AppUsageTest
import com.zaze.utils.IntentFactory
import com.zaze.utils.SystemSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        PermissionHandler

//        if (appUsageTest == null) {
//            appUsageTest = AppUsageTest(activity).apply {
//                run()
//            }
//        }
        if (SystemSettings.canDrawOverlays(activity)) {
            floatingLayout?.hide()
            floatingLayout = FloatingLayout(activity).apply {
                addView(LayoutInflater.from(context).inflate(R.layout.floating_view, this, false))
            }
            floatingLayout?.show()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.startActivity(IntentFactory.manageOverlayPermission())
        }

        listOf(
            TestByJava(),
//            TestBattery(),
//            TestFile(),
//            TestUserHandle(),
//            TestCommand(),
        ).forEach {
            it.doTest(activity)
        }

//
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
