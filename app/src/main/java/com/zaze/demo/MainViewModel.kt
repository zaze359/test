package com.zaze.demo

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.zaze.common.base.AbsFragment
import com.zaze.common.base.AbsViewModel
import com.zaze.demo.debug.LogDirListener
import com.zaze.demo.debug.kotlin.KData
import com.zaze.demo.debug.test.*
import com.zaze.demo.usagestats.AppUsageTest
import com.zaze.utils.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-14 - 16:23
 */
class MainViewModel : AbsViewModel() {
    val fragmentListData = MutableLiveData<ArrayList<AbsFragment>>()
    private val dirListener = LogDirListener(FileUtil.getSDCardRoot() + "/")

    fun init() {
        dirListener.startWatching()
    }

    fun loadFragments() {
        viewModelScope.launch(Dispatchers.Default) {
            val fragmentList = ArrayList<AbsFragment>()
            fragmentList.add(DemoFragment.newInstance("0"))
            fragmentList.add(DemoFragment.newInstance("1"))
            fragmentListData.postValue(fragmentList)
        }
    }

    override fun onCleared() {
        super.onCleared()
        dirListener.stopWatching()
    }

}