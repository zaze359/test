package com.zaze.common.base.ext

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel

/**
 * Description :
 * @author : zaze
 * @version : 2021-04-29 - 10:27
 */
fun Fragment.obtainViewModelFactory(): ViewModelFactory {
    return object : ViewModelFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return super.create(modelClass).also { vm ->
                initAbsViewModel(this@obtainViewModelFactory.activity, vm)
            }
        }
    }
}

fun Fragment.obtainActivityViewModelFactory(): ViewModelFactory {
    return ViewModelFactory()
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.myViewModel() = viewModels<VM> {
    obtainViewModelFactory()
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.myActivityViewModels() = activityViewModels<VM> {
    obtainActivityViewModelFactory()
}