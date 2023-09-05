package com.zaze.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

object VersionUtils {
    /**
     * @return true: API >= 23; 6.0+
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
    fun hasMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     * @return true: API >= 24; 7.0+
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
    fun hasNougat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    /**
     * @return true: API >= 25; 7.1.1+
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1)
    fun hasNougatMR(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1
    }

    /**
     * @return true: API >= 26; 8.0+
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    fun hasOreo(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    /**
     * @return true: API >= 27; 8.1+
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
    fun hasOreoMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1
    }

    /**
     * @return true: API >= 28; 9.0+
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
    fun hasP(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    /**
     * @return true: API >= 29; 10.0+
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    @JvmStatic
    fun hasQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    /**
     * @return true: API >= 30; 11.0+
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
    @JvmStatic
    fun hasR(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    /**
     * @return true: API >= 31; 12.0+
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    @JvmStatic
    fun hasS(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    /**
     * @return true: API >= 32; 12L+
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S_V2)
    @JvmStatic
    fun hasSv2(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2
    }

    /**
     * @return true: API >= 33; 13+
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    @JvmStatic
    fun hasTiramisu(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }
}


val Int.toSDK: Pair<String, String>?
    get() = when (this) {
        7 -> {
            "2.1" to "Eclair"
        }

        8 -> {
            "2,2" to "Froyo"
        }

        9 -> {
            "2.3" to "Gingerbread"
        }

        10 -> {
            "2.3.3" to "Eclair"
        }

        11 -> {
            "3.0" to "Honeycomb"
        }

        12 -> {
            "3.1" to "Honeycomb"
        }

        13 -> {
            "3.2" to "Honeycomb"
        }

        14 -> {
            "4.0" to "IceCreamSandwich"
        }

        15 -> {
            "4.0.3" to "IceCreamSandwich"
        }
        16 -> {
            "4.1" to "Jelly Bean"
        }
        17 -> {
            "4.2" to "Jelly Bean"
        }
        18 -> {
            "4.3" to "Jelly Bean"
        }
        19 -> {
            "4.4" to "KitKat"
        }
        20 -> {
            "4.4W" to "KitKat Wear"
        }
        21 -> {
            "5.0" to "Lollipop"
        }
        22 -> {
            "5.1" to "Lollipop"
        }
        23 -> {
            "6.0" to "Marshmallow"
        }
        24 -> {
            "7.0" to "Nougat"
        }
        25 -> {
            "7.1.1" to "Nougat"
        }
        26 -> {
            "8.0" to "Oreo"
        }
        27 -> {
            "8.1" to "Oreo"
        }
        28 -> {
            "9.0" to "Pie"
        }
        29 -> {
            "10.0" to "Q"
        }
        30 -> {
            "11.0" to "R"
        }
        31 -> {
            "12.0" to "S"
        }
        32 -> {
            "12L" to "Sv2"
        }
        33 -> {
            "13.0" to "Tiramisu"
        }
        else -> {
            null
        }
    }