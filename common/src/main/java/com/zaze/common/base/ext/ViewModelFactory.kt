package com.zaze.common.base.ext

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.AbsAndroidViewModel
import com.zaze.common.base.AbsViewModel
import com.zaze.common.base.BaseApplication
import kotlin.reflect.KClass

class MyViewModelLazy<VM : ViewModel>(
    private val activity: () -> ComponentActivity?,
    private val viewModelClass: KClass<VM>,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory
) : Lazy<VM> {
    private var cached: VM? = null
    private var observed = false

    override val value: VM
        get() {
            val viewModel = cached
            return if (viewModel == null) {
                val factory = factoryProducer()
                val store = storeProducer()
                ViewModelProvider(store, factory)[viewModelClass.java].also {
                    cached = it
                }
            } else {
                viewModel
            }.apply {
                observe(activity(), this)
            }
        }

    private fun observe(owner: ComponentActivity?, viewModel: ViewModel) {
        if (observed) {
            return
        }
        if (owner == null) {
            // Fragment not attached to an activity.
            observed = false
        } else {
            observed = true
            initAbsViewModel(owner, viewModel)
        }
    }

    override fun isInitialized(): Boolean = cached != null
}

fun obtainViewModelFactory(application: Application, delegateFactory: ViewModelProvider.Factory? = null): ViewModelFactory {
    return ViewModelFactory(application, delegateFactory)
}

open class ViewModelFactory(private val application: Application?, private val delegateFactory: ViewModelProvider.Factory? = null) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (application != null && AbsAndroidViewModel::class.java.isAssignableFrom(modelClass)) {
            try {
                modelClass.getConstructor(Application::class.java)
                    .newInstance(BaseApplication.getInstance())
            } catch (e: Exception) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            }
        } else delegateFactory?.create(modelClass) ?: super.create(modelClass)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return delegateFactory?.create(modelClass, extras) ?: super.create(modelClass, extras)
    }
}



/**
 * 在fragment中构建仅和fragment 关联的viewModel
 */
fun <T : ViewModel> Fragment.obtainFragViewModel(viewModelClass: Class<T>): T {
    return ViewModelProvider(this, ViewModelFactory(this.requireActivity().application))[viewModelClass]
}

/**
 * 在fragment中构建和activity 关联的viewModel
 */
@Deprecated("use obtainViewModelFactory ")
fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>): T {
    return requireActivity().let {
        ViewModelProvider(it, ViewModelFactory(this.requireActivity().application))[viewModelClass].also { vm ->
            initAbsViewModel(it, vm)
        }
    }
}

/**
 * 在activity中构建和activity 关联的viewModel
 */
@Deprecated("use myViewModels")
fun <T : ViewModel> AppCompatActivity.obtainViewModel(
    viewModelClass: Class<T>
) = ViewModelProvider(this, ViewModelFactory(this.application))[viewModelClass].also { vm ->
    initAbsViewModel(this, vm)
}

fun initAbsViewModel(owner: ComponentActivity?, viewModel: ViewModel) {
    if (owner is AbsActivity && viewModel is AbsViewModel) {
        viewModel._showMessage.observe(owner) {
            owner.showToast(it)
        }
        viewModel._progress.observe(owner) {
            owner.progress(it)
        }
        viewModel._tipDialog.observe(owner) {
            it?.build(owner)?.show()
        }
        viewModel._finish.observe(owner) {
            owner.finish()
        }
    }
}