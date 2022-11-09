package com.zaze.common.base.ext

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.AbsAndroidViewModel
import com.zaze.common.base.AbsViewModel
import com.zaze.common.base.BaseApplication

open class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (AbsAndroidViewModel::class.java.isAssignableFrom(modelClass)) {
            try {
                modelClass.getConstructor(Application::class.java)
                    .newInstance(BaseApplication.getInstance())
            } catch (e: Exception) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            }
        } else super.create(modelClass)
    }
}

/**
 * 在fragment中构建仅和fragment 关联的viewModel
 */
fun <T : ViewModel> Fragment.obtainFragViewModel(viewModelClass: Class<T>): T {
    return ViewModelProvider(this, ViewModelFactory())[viewModelClass]
}

/**
 * 在fragment中构建和activity 关联的viewModel
 */
@Deprecated("use obtainViewModelFactory ")
fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>): T {
    return activity?.let {
        ViewModelProvider(it, ViewModelFactory())[viewModelClass].also { vm ->
            initAbsViewModel(it, vm)
        }
    } ?: obtainFragViewModel(viewModelClass)
}

/**
 * 在activity中构建和activity 关联的viewModel
 */
@Deprecated("use obtainViewModelFactory ")
fun <T : ViewModel> AppCompatActivity.obtainViewModel(
    viewModelClass: Class<T>
) = ViewModelProvider(this, ViewModelFactory())[viewModelClass].also { vm ->
    initAbsViewModel(this, vm)
}

fun initAbsViewModel(owner: ComponentActivity?, viewModel: ViewModel) {
    if (owner is AbsActivity && viewModel is AbsViewModel) {
        viewModel._showMessage.observe(owner, Observer {
            owner.showToast(it)
        })
        viewModel._progress.observe(owner, Observer {
            owner.progress(it)
        })
        viewModel._tipDialog.observe(owner, Observer {
            it?.build(owner)?.show()
        })
        viewModel._finish.observe(owner, Observer {
            owner.finish()
        })
    }
}