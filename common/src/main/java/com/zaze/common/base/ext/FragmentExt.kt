package com.zaze.common.base.ext

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import kotlin.reflect.KClass

/**
 * Description :
 * @author : zaze
 * @version : 2021-04-29 - 10:27
 */

@MainThread
inline fun <reified VM : ViewModel> Fragment.myViewModels() = createMyViewModelLazy(
    activity = { activity },
    viewModelClass = VM::class,
    storeProducer = { this.viewModelStore },
    factoryProducer = { obtainViewModelFactory() }
)

@MainThread
inline fun <reified VM : ViewModel> Fragment.myActivityViewModels() =
    customActivityViewModels<VM>({ this.activity }) {
        obtainViewModelFactory()
    }

inline fun <reified VM : ViewModel> Fragment.customActivityViewModels(
    noinline activity: () -> ComponentActivity?,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = createMyViewModelLazy(activity, VM::class, { requireActivity().viewModelStore },
    factoryProducer ?: { requireActivity().defaultViewModelProviderFactory }
)

@MainThread
fun <VM : ViewModel> Fragment.createMyViewModelLazy(
    activity: () -> ComponentActivity?,
    viewModelClass: KClass<VM>,
    storeProducer: () -> ViewModelStore,
    factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return MyViewModelLazy(activity, viewModelClass, storeProducer, factoryPromise)
}
