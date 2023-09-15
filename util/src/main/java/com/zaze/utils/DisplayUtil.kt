package com.zaze.utils

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager

import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description : from in.srain.cube.util
 * date : 2016-03-28 - 15:44
 */
object DisplayUtil {
    private lateinit var displayProfile: DisplayProfile
    private var matchedDisplayProfile: DisplayProfile? = null

    // --------------------------------------------------
    // --------------------------------------------------
    @JvmStatic
    @JvmOverloads
    fun init(application: Application, baseWidthPixels: Int = -1) {
        displayProfile = DisplayProfile(application.resources.displayMetrics)
        val metrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            (application.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getRealMetrics(
                metrics
            )
        } else {
            metrics.setTo(application.resources.displayMetrics)
        }
        displayProfile.updateRealMetrics(metrics)
        if (baseWidthPixels > 0) {
            metrics.density = (metrics.widthPixels / baseWidthPixels).toFloat()
            metrics.scaledDensity = metrics.density
            metrics.densityDpi = (DisplayMetrics.DENSITY_DEFAULT * metrics.density).toInt()
            matchedDisplayProfile = DisplayProfile(metrics)
        }
        ZLog.i(ZTag.TAG_DEBUG, "displayProfile : $displayProfile")
    }

    @JvmStatic
    fun replaceResource(resources: Resources): Resources {
        matchedDisplayProfile?.let {
            resources.displayMetrics.setTo(it.metrics)
        }
        return resources
    }

    @JvmStatic
    fun getMetrics(): DisplayMetrics {
        return getDisplayProfile().metrics
    }

    @JvmStatic
    fun getScreenWidthDp(): Float {
        return getDisplayProfile().widthDp
    }

    @JvmStatic
    fun getScreenHeightDp(): Float {
        return getDisplayProfile().heightDp
    }

    fun getDisplayProfile(): DisplayProfile {
        return matchedDisplayProfile ?: displayProfile
    }

    // --------------------------------------------------
    @JvmStatic
    @JvmOverloads
    fun getDensityDpiName(metrics: DisplayMetrics = getMetrics()): String {
        val densityDpi = metrics.densityDpi
        return when {
            densityDpi <= DisplayMetrics.DENSITY_LOW -> "ldpi"
            densityDpi <= DisplayMetrics.DENSITY_MEDIUM -> "mdpi"
            densityDpi <= DisplayMetrics.DENSITY_TV -> "tv"
            densityDpi <= DisplayMetrics.DENSITY_HIGH -> "hdpi"
            densityDpi <= DisplayMetrics.DENSITY_XHIGH -> "xhdpi"
            densityDpi <= DisplayMetrics.DENSITY_XXHIGH -> "xxhdpi"
            densityDpi <= DisplayMetrics.DENSITY_XXXHIGH -> "xxxdpi"
            else -> "????dpi"
        }
    }


    // --------------------------------------------------
    // --------------------------------------------------
    /**
     * @param dp dp
     * @return px
     *
     */
    @Deprecated("{@link DisplayUtil#pxFromDp(float dp)}")
    @JvmStatic
    @JvmOverloads
    fun dp2px(dp: Float, metrics: DisplayMetrics = getMetrics()): Float {
        return dp * metrics.density
    }

    // --------------------------------------------------

    /**
     * px 转 dpi
     *
     * @param px
     * @return
     */
    @JvmStatic
    @JvmOverloads
    fun dpiFromPx(px: Int, metrics: DisplayMetrics = getMetrics()): Float {
        return px / metrics.density
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    @JvmStatic
    @JvmOverloads
    fun pxFromDp(dp: Float, metrics: DisplayMetrics = getMetrics()): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics)
    }

    /**
     * sp 转 px
     *
     * @param sp sp
     * @return
     */
    @JvmStatic
    @JvmOverloads
    fun pxFromSp(sp: Float, metrics: DisplayMetrics = getMetrics()): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics)
    }

    data class DisplayProfile(val metrics: DisplayMetrics) {
        val widthPixels: Int
        val heightPixels: Int
        val widthDp: Float
        val heightDp: Float
        val density: Float
        val densityDpi: Int
        var realWidthPixels: Int = 0
        var realHeightPixels: Int = 0

        init {
            widthPixels = metrics.widthPixels
            heightPixels = metrics.heightPixels
            updateRealMetrics(metrics)
            density = metrics.density
            densityDpi = metrics.densityDpi
            widthDp = dpiFromPx(widthPixels, metrics)
            heightDp = dpiFromPx(heightPixels, metrics)
            // ------------------------------------------------------
        }

        fun updateRealMetrics(realMetrics: DisplayMetrics) {
            realWidthPixels = realMetrics.widthPixels
            realHeightPixels = realMetrics.heightPixels
        }

        fun getDensityDpiName(): String {
            return getDensityDpiName(metrics)
        }

        override fun toString(): String {
            return "DisplayProfile(metrics=$metrics, widthPixels=$widthPixels, heightPixels=$heightPixels, widthDp=$widthDp, heightDp=$heightDp, density=$density, densityDpi=$densityDpi, realWidthPixels=$realWidthPixels, realHeightPixels=$realHeightPixels)"
        }
    }
}
