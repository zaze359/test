package com.zaze.core.nativelib

import android.content.Context
import android.os.Build
import android.os.Debug
import com.zaze.common.dynamic.findLibraryCompat
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger


object JvmtiHelper {
    const val jvmtiAgentLibName = "libmynative-lib.so"

    private val initState = AtomicInteger(0)

    fun init(context: Context) {
        ZLog.i(ZTag.TAG_DEBUG, "JvmtiHelper init")
        if(initState.compareAndSet(0, 1)) {
            var agentPath = findAgentLib(context) ?: return
            // 关联时会检测 lib库路径，不能包含 =，所以需要将 so拷贝一下。
            agentPath = copyLibToData(context, agentPath)
            attach(agentPath, context.applicationContext.classLoader)
            System.load(agentPath)
//            System.loadLibrary("zjvmti-agent")
        }
    }

    private fun findAgentLib(context: Context): String? {
        return context.classLoader.findLibraryCompat("mynative-lib")
    }

    /**
     * 连接 JVM Ti
     */
    private fun attach(agentPath: String, classLoader: ClassLoader) {
        ZLog.i(ZTag.TAG_DEBUG, "JvmtiHelper attachAgent: $agentPath")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) { // 9.0+
            Debug.attachJvmtiAgent(agentPath, null, classLoader)
            return
        }
        // 需要反射进行关联。
        try {
            val vmDebugClass = Class.forName("dalvik.system.VMDebug")
            val attachAgentMethod = vmDebugClass.getMethod("attachAgent", String::class.java)
            attachAgentMethod.isAccessible = true
            attachAgentMethod.invoke(null, agentPath)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun copyLibToData(context: Context, sourcePath: String): String {
        val jvmtiDir = File(context.filesDir, "jvmti")
        if (!jvmtiDir.exists()) {
            jvmtiDir.mkdirs()
        }
        val jvmtiAgentFile = File(jvmtiDir.absolutePath, jvmtiAgentLibName)
        if (jvmtiAgentFile.exists()) {
            jvmtiAgentFile.delete()
        }
        // copy
        FileUtil.copy(File(sourcePath), jvmtiAgentFile)
        return jvmtiAgentFile.absolutePath
    }
}