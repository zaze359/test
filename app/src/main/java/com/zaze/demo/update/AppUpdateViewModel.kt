package com.zaze.demo.update

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.zaze.common.base.AbsAndroidViewModel
import com.zaze.utils.permission.ExternalStoragePermission
import com.zaze.demo.core.bsdiff.AppPatchUtils
import com.zaze.demo.feature.applications.ApplicationManager
import com.zaze.utils.AppUtil
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AppUpdateViewModel @Inject constructor(application: Application) :
    AbsAndroidViewModel(application) {

    private val viewModelState = MutableStateFlow(AppUpdateViewModelState(false))
    val uiState = viewModelState.map(AppUpdateViewModelState::toUiState).stateIn(
        viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
    )
    private val dir = "/sdcard/zaze/bsdiff"
    private val createdDir = "${dir}/created"

    //        val dir = context.filesDir.absolutePath
    private val preOldApkPath = "${dir}/old.apk"
    private val preNewApkPath = "${dir}/new.apk"
    private val prePatchPath = "${dir}/patch.apk"
    private val createdApkPath = "${createdDir}/new.apk"
    private val packageName = "com.zaze.apps"

//    private val createdPatchPath = "${createdDir}/patch.apk"
//    fun createPatch() {
//        viewModelScope.launch(Dispatchers.IO) {
//            if(!checkStoragePermission()) return@launch
//            FileUtil.createParentDir(createdPatchPath)
//            ZLog.i(ZTag.TAG_DEBUG, "createPatch start: $createdPatchPath")
//            try {
//                val ret = AppPatchUtils.diff(preOldApkPath, preNewApkPath, createdPatchPath)
//                ZLog.i(ZTag.TAG_DEBUG, "createPatch end: $ret")
//            } catch (e: Throwable) {
//                e.printStackTrace()
//            }
//        }
//    }

    fun installOldApp() {
        viewModelScope.launch(Dispatchers.IO) {
            ApplicationManager.installApp(application, File(preOldApkPath))
        }
    }

    fun unInstallApp() {
        viewModelScope.launch(Dispatchers.IO) {
            AppUtil.unInstall(application, packageName)
        }
    }

    fun applyPatch() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!checkStoragePermission()) return@launch
            FileUtil.createParentDir(createdApkPath)
            ZLog.i(ZTag.TAG_DEBUG, "applyPatch start: $createdApkPath")
            try {
                val ret = AppPatchUtils.applyPatch(preOldApkPath, createdApkPath, prePatchPath)
                ZLog.i(ZTag.TAG_DEBUG, "applyPatch end: $ret")
                ApplicationManager.installApp(application, File(createdApkPath))
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun checkStoragePermission(): Boolean {
        val granted = ExternalStoragePermission.hasPermission(application)
        viewModelState.update {
            it.copy(hasExternalStoragePermission = granted)
        }
        return granted
    }
}

private data class AppUpdateViewModelState(
    val hasExternalStoragePermission: Boolean = true
) {
    fun toUiState(): AppUpdateUiState {
        return when {
            !hasExternalStoragePermission -> {
                AppUpdateUiState.NoPermission(
                    ExternalStoragePermission.getExternalStoragePermission()
                )
            }

            else -> {
                AppUpdateUiState.Default
            }
        }
    }
}

sealed class AppUpdateUiState {
    object Default : AppUpdateUiState()

    data class NoPermission(val permission: String) : AppUpdateUiState()
}