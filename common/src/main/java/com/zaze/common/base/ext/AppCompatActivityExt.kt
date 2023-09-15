package com.zaze.common.base.ext

import androidx.activity.ComponentActivity
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-04 - 21:45
 */
fun AppCompatActivity.findFragment(frameId: Int): Fragment? {
    return supportFragmentManager.findFragmentById(frameId)
}

fun AppCompatActivity.replaceFragment(frameId: Int, fragment: Fragment, tag: String? = null) {
    supportFragmentManager.transact {
        replace(frameId, fragment, tag)
    }
}

fun AppCompatActivity.addFragment(frameId: Int, fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(frameId, fragment, tag)
    }
}

fun AppCompatActivity.showFragment(fragment: Fragment?) {
    if (fragment == null) {
        return
    }
    supportFragmentManager.transact {
        show(fragment)
    }
}

fun AppCompatActivity.hideFragment(fragment: Fragment?) {
    if (fragment == null) {
        return
    }
    supportFragmentManager.transact {
        hide(fragment)
    }
}

fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun FragmentManager.transactAllowingStateLoss(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}

// --------------------------------------------------
fun <T : Toolbar> AppCompatActivity.setupActionBar(
    toolbar: T,
    action: ActionBar.(toolbar: T) -> Unit = {}
) {
    this.setSupportActionBar(toolbar)
    supportActionBar?.run {
        action(toolbar)
    }
}

fun <T : Toolbar> AppCompatActivity.initToolbar(
    toolbar: T,
    action: ActionBar.(toolbar: T) -> Unit = {}
) {
    setupActionBar(toolbar) {
        it.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        action(this, it)
        setHomeButtonEnabled(true)
        setDisplayHomeAsUpEnabled(true)
    }
}

//@MainThread
//@Deprecated("此方法不支持 hilt，使用原生的viewModels 并 重写 ComponentActivity.getDefaultViewModelProviderFactory() 来处理。")
//inline fun <reified VM : ViewModel> ComponentActivity.myViewModels(): Lazy<VM> = viewModels {
//    obtainViewModelFactory()
//}
// --------------------------------------------------
// --------------------------------------------------
fun ComponentActivity.obtainViewModelFactory(delegateFactory: ViewModelProvider.Factory? = null): ViewModelProvider.Factory {
    return object : ViewModelFactory(application, delegateFactory) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return super.create(modelClass).also { vm ->
                initAbsViewModel(this@obtainViewModelFactory, vm)
            }
        }
    }
}
