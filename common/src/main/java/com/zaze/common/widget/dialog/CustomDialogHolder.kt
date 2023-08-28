package com.zaze.common.widget.dialog

import android.app.Dialog
import android.view.*
import android.widget.TextView
import com.zaze.common.R

/**
 * Description :
 * @author : ZAZE
 * @version : 2020-04-21 - 13:24
 */
class CustomDialogHolder(builder: DialogProvider.Builder) : DialogViewHolder(builder) {
    private lateinit var dialogTitleTv: TextView
    private lateinit var dialogMessageTv: TextView
    private lateinit var dialogSureBtn: TextView
    private lateinit var dialogCancelBtn: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?): View {
//        ZLog.i(ZTag.TAG, "CustomDialogHolder onCreateView")
        val view = inflater.inflate(R.layout.custom_dialog_layout, container, false)
        dialogTitleTv = view.findViewById(R.id.title_tv)
        dialogMessageTv = view.findViewById(R.id.content_tv)
        dialogSureBtn = view.findViewById(R.id.dialogSureBtn)
        dialogCancelBtn = view.findViewById(R.id.cancel_btn)
        return view
    }

    override fun onViewCreated(dialog: Dialog?) {
//        ZLog.i(ZTag.TAG, "CustomDialogHolder onViewCreated")
        builder.message?.let {
            dialogMessageTv.gravity = builder.messageGravity
            dialogMessageTv.text = it
        }
        builder.title?.let {
            dialogTitleTv.visibility = View.VISIBLE
            dialogTitleTv.text = it
        } ?: let {
            dialogTitleTv.visibility = View.GONE
        }
        builder.negative?.let {
            dialogCancelBtn.visibility = View.VISIBLE
            dialogCancelBtn.text = it
        } ?: let {
            dialogCancelBtn.visibility = View.GONE
        }
        builder.positive?.let {
            dialogSureBtn.visibility = View.VISIBLE
            dialogSureBtn.text = it
        } ?: let {
            dialogSureBtn.visibility = View.GONE
        }

        dialogSureBtn.setOnClickListener {
            if (dialog?.isShowing == true) {
                dialog.dismiss()
            }
            builder.positiveListener?.invoke(dialogSureBtn)
        }
        dialogCancelBtn.setOnClickListener {
            if (dialog?.isShowing == true) {
                dialog.dismiss()
            }
            builder.negativeListener?.invoke(dialogCancelBtn)
        }
    }
}