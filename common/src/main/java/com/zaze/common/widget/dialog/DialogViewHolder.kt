package com.zaze.common.widget.dialog

import android.app.Dialog
import android.os.Build
import android.view.*
import com.zaze.core.designsystem.util.ThemeUtils
import com.zaze.utils.DisplayUtil
import kotlin.math.min

/**
 * Description :
 * @author : zaze
 * @version : 2021-11-30 - 10:45
 */
abstract class DialogViewHolder(val builder: DialogProvider.Builder) {
    private val width: Int by lazy {
        min(DisplayUtil.getDisplayProfile().widthPixels * 0.8f, DisplayUtil.pxFromDp(480f)).toInt()
    }

    abstract fun onCreateView(inflater: LayoutInflater, container: ViewGroup?): View

    abstract fun onViewCreated(dialog: Dialog?)

    open fun onCreateDialog(dialog: Dialog) {
//        MyLog.i(LcTag.TAG, "CustomDialogHolder onCreateDialog")
        dialog.setCancelable(builder.cancelable)
        dialog.window?.let { window ->
            val systemUiVisibility = ThemeUtils.setLayoutFullScreen(window, true)
            window.decorView.setOnSystemUiVisibilityChangeListener {
//                MyLog.i(LcTag.TAG, "dialog visibility: ${it == View.SYSTEM_UI_FLAG_VISIBLE}")
                window.decorView.systemUiVisibility = systemUiVisibility
            }
            if (builder.applicationOverlay) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // TYPE_SYSTEM_OVERLAY, TYPE_SYSTEM_ERROR
                    window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                } else {
                    window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
                }
                window.setGravity(Gravity.CENTER)
            }
        }
    }

    open fun measure(dialog: Dialog?) {
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}