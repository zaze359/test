package com.zaze.dynamic.hook

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import com.zaze.dynamic.wrapper.AssetManagerWrapper

class HookResources {

    companion object {
        fun build(superRes: Resources, assets: AssetManager): Resources {
            return Resources(assets, superRes.displayMetrics, superRes.configuration)
        }

        fun build(context: Context, apkPath: String): Resources? {
            return AssetManagerWrapper.build(apkPath)?.mBase?.let {
                build(context.resources, it)
            }
        }
    }


}