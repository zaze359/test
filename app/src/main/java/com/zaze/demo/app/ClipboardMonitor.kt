package com.zaze.demo.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ClipboardManager.OnPrimaryClipChangedListener
import android.content.Context
import java.util.concurrent.atomic.AtomicInteger

class ClipboardMonitor {
    private val initState = AtomicInteger(0)

    var enable = false

    fun init(context: Context, onPrimaryClipChanged: (ClipData?) -> Unit = {}) {
        if (initState.compareAndSet(0, 1)) {
            val mClipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            mClipboardManager?.addPrimaryClipChangedListener {
                if (mClipboardManager.hasPrimaryClip()) {
                    this._onPrimaryClipChanged(mClipboardManager.primaryClip, onPrimaryClipChanged)
                } else {
                    this._onPrimaryClipChanged(null, onPrimaryClipChanged)
                }
            }
        }
    }

    private fun _onPrimaryClipChanged(
        clipData: ClipData?,
        onPrimaryClipChanged: (ClipData?) -> Unit
    ) {
        if (enable) onPrimaryClipChanged(clipData)
    }
}