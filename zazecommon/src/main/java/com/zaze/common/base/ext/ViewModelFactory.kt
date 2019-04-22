package com.zaze.common.base.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.zaze.common.base.BaseApplication

/**
 * 在fragment中构建仅和fragment 关联的viewModel
 */
fun <T : ViewModel> Fragment.obtainFragViewModel(viewModelClass: Class<T>): T {
    return ViewModelProviders.of(this, ViewModelProvider.AndroidViewModelFactory.getInstance(BaseApplication.getInstance())).get(viewModelClass)
}

/**
 * 在fragment中构建和activity 关联的viewModel
 */
fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>): T {
    return activity?.let {
        ViewModelProviders.of(it, ViewModelProvider.AndroidViewModelFactory.getInstance(it.application)).get(viewModelClass)
    }
            ?: ViewModelProviders.of(this, ViewModelProvider.AndroidViewModelFactory.getInstance(BaseApplication.getInstance())).get(viewModelClass)
}

/**
 * 在activity中构建和activity 关联的viewModel
 */
fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(viewModelClass)