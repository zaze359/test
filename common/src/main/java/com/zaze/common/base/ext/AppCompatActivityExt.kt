package com.zaze.common.base.ext

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import com.zaze.common.R
import com.zaze.common.util.ScreenUtils

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-04 - 21:45
 */
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
fun AppCompatActivity.setupActionBar(toolbar: Toolbar, action: ActionBar.() -> Unit = {}) {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        action()
    }
}

// --------------------------------------------------
/**
 * 设置沉浸式
 * [isFullScreen] isFullScreen
 * [color] color
 */
fun AppCompatActivity.setImmersion(
    isFullScreen: Boolean = false,
    color: Int = R.color.colorPrimary
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        return
    }
    if (isFullScreen) {
        ScreenUtils.addLayoutFullScreen(window)
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
    } else {
        window.statusBarColor = ContextCompat.getColor(this, color)
    }
}

fun AppCompatActivity.setImmersionOnWindowFocusChanged(
    isFullScreen: Boolean = false,
    hasFocus: Boolean
) {
    if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isFullScreen) {
        ScreenUtils.addLayoutFullScreen(window)
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}

// --------------------------------------------------
fun ComponentActivity.obtainViewModelFactory(): ViewModelFactory {
    return object : ViewModelFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return super.create(modelClass).also { vm ->
                initAbsViewModel(this@obtainViewModelFactory, vm)
            }
        }
    }
}

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.myViewModels(): Lazy<VM> = viewModels {
    obtainViewModelFactory()
}
// --------------------------------------------------