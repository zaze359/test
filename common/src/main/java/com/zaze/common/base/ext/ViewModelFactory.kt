package com.zaze.common.base.ext

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.zaze.common.base.AbsAndroidViewModel
import com.zaze.common.base.BaseApplication
import java.lang.reflect.InvocationTargetException

class ViewModelFactory(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (AbsAndroidViewModel::class.java.isAssignableFrom(modelClass)) {
            try {
                modelClass.getConstructor(Application::class.java).newInstance(mApplication)
            } catch (e: NoSuchMethodException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: InstantiationException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            }
        } else super.create(modelClass)
    }
}

/**
 * 在fragment中构建仅和fragment 关联的viewModel
 */
fun <T : ViewModel> Fragment.obtainFragViewModel(viewModelClass: Class<T>): T {
    return ViewModelProviders.of(this, ViewModelFactory(BaseApplication.getInstance())).get(viewModelClass)
}

/**
 * 在fragment中构建和activity 关联的viewModel
 */
fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>): T {
    return activity?.let {
        ViewModelProviders.of(it, ViewModelFactory(it.application)).get(viewModelClass)
    }
            ?: ViewModelProviders.of(this, ViewModelFactory(BaseApplication.getInstance())).get(viewModelClass)
}

/**
 * 在activity中构建和activity 关联的viewModel
 */
fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory(application)).get(viewModelClass)