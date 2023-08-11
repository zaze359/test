package com.zaze.dynamic.hook

import android.os.Build

object HookActivityManager {
    /**
     * Class<*>：接口信息
     * Any：原始 ActivityManager 实例
     */
    private fun swapActivityManager(swap: (Class<*>, Any) -> Any) {
        val singletonClass = Class.forName("android.util.Singleton")
        // 获取 Singleton<IActivityTaskManager>.mInstance 字段
        val mInstanceField =
            singletonClass.getDeclaredField("mInstance").apply { isAccessible = true }

        val singletonGetMethod = singletonClass.getDeclaredMethod("get")
        //
        val (className, fieldName) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            "android.app.ActivityManager" to "IActivityManagerSingleton"
        } else {
            "android.app.ActivityManagerNative" to "gDefault"
        }
        // 获取 ActivityManager.class
        val activityManagerClass = Class.forName(className)
        // 获取 IActivityManagerSingleton 字段信息
        val activityManagerSingletonField =
            activityManagerClass.getDeclaredField(fieldName).apply { isAccessible = true }
        // 获取 ActivityManager.IActivityManagerSingleton 实例
        val activityManagerSingletonInstance = activityManagerSingletonField[activityManagerClass]
        // 调用 Singleton.get() 方法来获取 IActivityManager 原始实例，保证一定存在值
        val activityManagerInstance = singletonGetMethod.invoke(activityManagerSingletonInstance)
        val iActivityManagerInterface = Class.forName("android.app.IActivityManager")
        // 替换 Singleton.mInstance 的值
        mInstanceField[activityManagerSingletonInstance] =
            swap(iActivityManagerInterface, activityManagerInstance!!)
    }
}