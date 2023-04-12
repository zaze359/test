package com.zaze.demo

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AppTest {
    private val packageName = "com.zaze.demo"

    lateinit var uiDevice: UiDevice

    @Before
    fun startApp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        // 回到桌面
        uiDevice.pressHome()
        val launcher = uiDevice.launcherPackageName
        println("launcherPackageName: $launcher")
        // 等待
        uiDevice.wait(Until.hasObject(By.pkg(launcher).depth(0)), 10_000);
        // 启动应用
        val intent: Intent? = context.packageManager
            .getLaunchIntentForPackage(packageName)
            ?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        // 等待启动完成
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), 10_000);
    }

    @Test
    fun doSomeThing() {
        uiDevice.findObject(By.res(packageName, "mainTestBtn"))?.text = "测试修改"
    }

}