package com.zaze.demo.feature.storage.sp

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.zaze.common.base.AbsAndroidViewModel
import com.zaze.core.designsystem.components.snackbar.SnackbarManager
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedPreferencesViewModel @Inject constructor(application: Application) :
    AbsAndroidViewModel(application) {

    companion object {
        private const val KEY = "sp_key"
    }

    private val sharedPreferences by lazy {
        application.getSharedPreferences("mySp", Context.MODE_PRIVATE)
    }


    fun apply() {
        sharedPreferences.edit()
            .putString(KEY, "apply string")
            .apply()
        showMessage("apply end")
    }

    fun commit() {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPreferences.edit()
                .putString(KEY, "commit string")
                .commit()
            showMessage("commit end")
        }
    }

    fun remove() {
        sharedPreferences.edit()
            .remove(KEY)
            .apply()
    }

    fun clear() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    fun read() {
        viewModelScope.launch(Dispatchers.IO) {
            val value = sharedPreferences.getString(KEY, "not found") ?: ""
            showMessage(value)
            sharedPreferences.all.forEach {
                ZLog.d(ZTag.TAG_DEBUG, "${it.key}=${it.value}")
            }
        }
    }

    private fun showMessage(message: String) {
        ZLog.d(ZTag.TAG_DEBUG, message)
//        messages.update {
//            val newList = it.toMutableList()
//            newList
//        }
        SnackbarManager.showMessage(message)
    }
}