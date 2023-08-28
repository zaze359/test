package com.zaze.common.base.ext

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Description :
 * @author : zaze
 * @version : 2021-04-29 - 10:27
 */
fun Fragment.obtainViewModelFactory(delegateFactory: ViewModelProvider.Factory? = null): ViewModelProvider.Factory {
    return object : ViewModelFactory(requireActivity().application, delegateFactory) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return super.create(modelClass).also { vm ->
                initAbsViewModel(requireActivity(), vm)
            }
        }
    }
}

fun <T : Toolbar> Fragment.setupActionBar(
    toolbar: T,
    action: ActionBar.(toolbar: T) -> Unit = {}
) {
    (requireActivity() as AppCompatActivity).setupActionBar(toolbar, action)
}

fun <T : Toolbar> Fragment.initToolbar(
    toolbar: T,
    action: ActionBar.(toolbar: T) -> Unit = {}
) {
    setupActionBar(toolbar) {
        action(this, it)
        setHomeButtonEnabled(true)
        setDisplayHomeAsUpEnabled(true)
    }
}