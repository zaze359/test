package com.zaze.common.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate
import com.zaze.common.R
import com.zaze.core.designsystem.theme.ThemeMode
import com.zaze.core.designsystem.theme.ext.exitFullscreen
import com.zaze.core.designsystem.theme.ext.isColorLight
import com.zaze.core.designsystem.theme.ext.setImmersiveFullscreen
import com.zaze.core.designsystem.theme.ext.setImmersiveSurface
import com.zaze.core.designsystem.theme.ext.setLightNavigationBar
import com.zaze.core.designsystem.theme.ext.setLightStatusBar
import com.zaze.core.designsystem.theme.ext.surfaceColor
import com.zaze.core.designsystem.theme.toNightMode
import com.zaze.core.designsystem.theme.toThemeMode
import com.zaze.core.designsystem.util.ThemeStore
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-15 - 15:27
 */
@AndroidEntryPoint
abstract class AbsThemeActivity : AbsPermissionsActivity() {

    private val handler = Handler(Looper.getMainLooper())

    private val themeRunnable = Runnable {
        if(isFullScreen()) {
            setImmersiveFullscreen()
        }
    }

    @Inject
    lateinit var themeStore: ThemeStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTheme()
        setImmersiveSurface(isFullScreen())
        val isLight = surfaceColor().isColorLight
        setLightNavigationBar(isLight)
        setLightStatusBar(isLight)
    }

    private fun updateTheme() = runBlocking {
        val themeMode = themeStore.themeMode.toThemeMode()
        setTheme(themeMode.toThemeRes(themeStore.materialYou))
        AppCompatDelegate.setDefaultNightMode(themeMode.toNightMode())
    }

    @StyleRes
    private fun ThemeMode?.toThemeRes(materialYou: Boolean): Int = when {
        materialYou -> {  // 使用自适应的 Theme.Material3.DynamicColors.DayNight
            R.style.Theme_MyApp_MD3
        }

        else -> { //
            when (this) {
                ThemeMode.LIGHT -> R.style.Theme_MyApp_Light
                ThemeMode.DARK -> R.style.Theme_MyApp_Dark
                else -> R.style.Theme_MyApp_Adaptive
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            handler.removeCallbacks(themeRunnable)
            handler.postDelayed(themeRunnable, 300)
        } else {
            handler.removeCallbacks(themeRunnable)
        }
    }

    /**
     * 是否全屏
     */
    protected open fun isFullScreen(): Boolean {
        return false
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(themeRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        exitFullscreen()
    }
}