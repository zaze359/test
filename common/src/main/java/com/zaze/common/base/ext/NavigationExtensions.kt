package com.zaze.common.base.ext

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.zaze.common.R

fun Fragment.navigate(@IdRes id: Int) = findNavController().navigate(id)

fun Fragment.findNavHostFragment(@IdRes id: Int): NavHostFragment {
    return childFragmentManager.findFragmentById(id) as NavHostFragment
}

fun Fragment.findActivityNavController(@IdRes id: Int): NavController {
    return requireActivity().findNavController(id)
}

fun AppCompatActivity.findNavController(@IdRes id: Int): NavController {
    val fragment = supportFragmentManager.findFragmentById(id) as NavHostFragment
    return fragment.navController
}

fun AppCompatActivity.currentNavFragment(navHostId: Int): Fragment? {
    val navHostFragment: NavHostFragment =
        supportFragmentManager.findFragmentById(navHostId) as NavHostFragment
    return navHostFragment.childFragmentManager.fragments.firstOrNull()
}

fun AppCompatActivity.findNavHostFragment(@IdRes id: Int): NavHostFragment {
    return supportFragmentManager.findFragmentById(id) as NavHostFragment
}

val normalNavOptions
    get() = navOptions {
        launchSingleTop = false
        anim {
            enter = R.anim.fragment_open_enter
            exit = R.anim.fragment_open_exit
            popEnter = R.anim.fragment_close_enter
            popExit = R.anim.fragment_close_exit
        }
    }
val fadeNavOptions
    get() = navOptions {
        anim {
            enter = android.R.anim.fade_in
            exit = android.R.anim.fade_out
            popEnter = android.R.anim.fade_in
            popExit = android.R.anim.fade_out
        }
    }