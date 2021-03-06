package com.zaze.demo.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.zaze.demo.theme.widgets.Tintable
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-20 - 18:37
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
object ThemeUtils {

    var sRecycler: Field? = null
    private var sRecycleViewClearMethod: Method? = null


    fun refreshUI(context: Context) {
        val rootView =
                getWrapperActivity(context)?.window?.decorView?.findViewById<View>(android.R.id.content)
        refreshView(rootView)
    }

    private fun refreshView(view: View?) {
        if (view == null) {
            return
        }
        view.destroyDrawingCache()
        if (view is Tintable) {
            (view as Tintable).tint()
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    refreshView(view.getChildAt(i))
                }
            }
        } else {
            if (view is RecyclerView) {
                try {
                    if (sRecycler == null) {
                        sRecycler = RecyclerView::class.java.getDeclaredField("mRecycler")
                        sRecycler?.isAccessible = true
                    }
                    if (sRecycleViewClearMethod == null) {
                        sRecycleViewClearMethod =
                                Class.forName("android.support.v7.widget.RecyclerView\$Recycler")
                                        .getDeclaredMethod("clear")
                        sRecycleViewClearMethod?.isAccessible = true
                    }
                    sRecycleViewClearMethod?.invoke(sRecycler?.get(view))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                view.recycledViewPool.clear()
                view.invalidateItemDecorations()
            }
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    refreshView(view.getChildAt(i))
                }
            }
        }
    }

    // --------------------------------------------------
    private fun getWrapperActivity(context: Context?): Activity? {
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return getWrapperActivity(
                    context.baseContext
            )
        }
        return null
    }


}