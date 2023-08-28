package com.zaze.common.widget.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class CustomDialogFragment : DialogFragment() {
    private var customDialogHolder: DialogViewHolder? = null
    private var builder: DialogProvider.Builder? = null

    fun init(viewHolder: DialogViewHolder): CustomDialogFragment {
        this.customDialogHolder = viewHolder
        this.builder = viewHolder.builder
        this.arguments = builder?.extras
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return customDialogHolder?.onCreateView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customDialogHolder?.onViewCreated(dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        builder?.let {
            isCancelable = it.cancelable
        }
        customDialogHolder?.onCreateDialog(dialog)
        return dialog
    }

    override fun getTheme(): Int {
        return builder?.theme ?: super.getTheme()
    }

    override fun onStart() {
        super.onStart()
        customDialogHolder?.measure(dialog)
    }

    fun show(manager: FragmentManager) {
        super.show(manager, builder?.tag)
    }
}