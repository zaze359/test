package com.zaze.utils.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import com.zaze.utils.IntentFactory

object ExternalStoragePermission {

    fun hasPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return Environment.isExternalStorageManager()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
            return true
        }
        return PermissionHelper.hasPermissions(context, getExternalStoragePermissions())
    }

    fun getExternalStoragePermissions(): Array<String> {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                emptyList()
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                mutableListOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                )
            }

            else -> {
                mutableListOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        }.toTypedArray()
    }

    @Deprecated("use registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) ")
    fun checkSelfPermission(context: Context): Boolean {
        return PermissionHelper.checkSelfPermission(context, getExternalStoragePermissions())
    }

    fun createSettingIntent(context: Context): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            IntentFactory.manageAllFilesAccessPermission()
        } else {
            IntentFactory.applicationDetailsSettings(context.packageName)
        }
    }

    fun getExternalStoragePermission(isWrite: Boolean = true): String {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                if (isWrite) Manifest.permission.MANAGE_EXTERNAL_STORAGE else Manifest.permission.READ_EXTERNAL_STORAGE
            }

            else -> {
                if (isWrite) Manifest.permission.WRITE_EXTERNAL_STORAGE else Manifest.permission.READ_EXTERNAL_STORAGE
            }
        }
    }
}