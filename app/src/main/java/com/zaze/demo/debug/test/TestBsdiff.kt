package com.zaze.demo.debug.test

import android.content.Context
import com.zaze.demo.core.bsdiff.AppPatchUtils
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File

class TestBsdiff : ITest {
    private val TAG = ZTag.TAG_DEBUG + ":TestBsdiff"
    private val dir = "/sdcard/zaze"
    private val bsdiffDir = "${dir}/bsdiff"
    private val createdDir = "${bsdiffDir}/created"

    private val oldApkPath = "${bsdiffDir}/old.apk"
    private val newApkPath = "${bsdiffDir}/new.apk"
    private val patchPath = "${bsdiffDir}/apk.patch"
    private val createdApkPath = "${createdDir}/new.apk"
    private val createdPatchPath = "${createdDir}/apk.patch"

    private val soLibName = "libbsdiff-android.so"
    private val soPath = "${dir}/so/${soLibName}"

    override fun doTest(context: Context) {
        log("=== Bsdiff Test Start ===")
        log("oldApkPath: $oldApkPath")
        log("newApkPath: $newApkPath")
        log("patchPath: $patchPath")
        log("createdApkPath: $createdApkPath")
        log("createdPatchPath: $createdPatchPath")

        if (!checkFiles()) {
            log("Files not prepared, skipping test")
            return
        }

        testDiff()
        testApplyPatch()
        log("=== Bsdiff Test End ===")
    }

    private fun checkFiles(): Boolean {
        val oldExists = FileUtil.exists(oldApkPath)
        val newExists = FileUtil.exists(newApkPath)
        val patchExists = FileUtil.exists(patchPath)

        log("old.apk exists: $oldExists")
        log("new.apk exists: $newExists")
        log("apk.patch exists: $patchExists")

        return oldExists && (newExists || patchExists)
    }

    private fun testDiff() {
        if (!FileUtil.exists(newApkPath)) {
            log("new.apk not found, skip diff test")
            return
        }

        log("=== Testing diff ===")
        try {
            FileUtil.createParentDir(createdPatchPath)
            val startTime = System.currentTimeMillis()
            val ret = AppPatchUtils.diff(oldApkPath, newApkPath, createdPatchPath)
            val costTime = System.currentTimeMillis() - startTime
            log("diff result: $ret, cost: ${costTime}ms")

            if (ret == 0) {
                val patchSize = File(createdPatchPath).length()
                val oldSize = File(oldApkPath).length()
                val newSize = File(newApkPath).length()
                log("patch size: ${patchSize / 1024}KB")
                log("compression ratio: ${String.format("%.2f", patchSize * 100.0 / newSize)}%")
            }
        } catch (e: Throwable) {
            ZLog.e(TAG, "diff error", e)
        }
    }

    private fun testApplyPatch() {
        if (!FileUtil.exists(patchPath)) {
            log("apk.patch not found, skip applyPatch test")
            return
        }

        log("=== Testing applyPatch ===")
        try {
            FileUtil.createParentDir(createdApkPath)
            val startTime = System.currentTimeMillis()
            val ret = AppPatchUtils.applyPatch(oldApkPath, createdApkPath, patchPath)
            val costTime = System.currentTimeMillis() - startTime
            log("applyPatch result: $ret, cost: ${costTime}ms")

            if (ret == 0) {
                val newSize = File(createdApkPath).length()
                log("generated new.apk size: ${newSize / 1024}KB")
            }
        } catch (e: Throwable) {
            ZLog.e(TAG, "applyPatch error", e)
        }
    }

    private fun copyLibToData(context: Context, sourcePath: String, libName: String): String {
        val soDir = File(context.filesDir, "so")
        val soFile = File(soDir.absolutePath, libName)
        if (soFile.exists()) {
            soFile.delete()
        }
        FileUtil.copy(File(sourcePath), soFile)
        return soFile.absolutePath
    }

    private fun log(message: String) {
        ZLog.i(TAG, message)
    }
}
