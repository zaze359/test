package com.zaze.common.widget.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zaze.common.databinding.LayoutDialogBinding
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-03-06 - 14:35
 */
class CustomDialog internal constructor(context: Context, builder: DialogProvider.Builder) {
    val dialog: Dialog
    private var _binding: LayoutDialogBinding? = null
    private val binding get() = _binding!!

    init {
        _binding = LayoutDialogBinding.inflate(LayoutInflater.from(context))
        //
        binding.dialogMessageTv.text = builder.message
        binding.dialogMessageTv.gravity = builder.messageGravity

        val dialogBuilder = MaterialAlertDialogBuilder(
            context, builder.theme
        ).setView(binding.root)
        dialogBuilder.setTitle(builder.title)
        builder.positive?.let {
            dialogBuilder.setPositiveButton(it, builder.positiveListener)
        }
        builder.negative?.let {
            dialogBuilder.setNegativeButton(it, builder.negativeListener)
        }
        dialog = dialogBuilder.create()
        dialog.setCancelable(builder.cancelable)
    }

    fun show() {
        val context = dialog.context
        if (context is Activity && context.isFinishing) {
            ZLog.w(ZTag.TAG_ERROR, "Activity isFinishing return it!")
            return
        }
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        dialog.show()
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
//        customDialogHolder.measure(dialog)
    }

    internal fun onDestroy() {
        _binding = null
    }

    fun dismiss() {
        dialog.dismiss()
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }
}