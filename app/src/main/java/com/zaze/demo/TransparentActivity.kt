package com.zaze.demo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.core.content.ContextCompat
import com.zaze.common.base.AbsActivity
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TransparentActivity : AbsActivity() {
    companion object {
        private const val TAG = "TransparentActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
//        window.setBackgroundDrawableResource(R.color.transparent)
        window.setLayout(0, 0)
        MainScope().launch {
            Log.i(TAG, "TransparentActivity run thread do something")
            delay(1000L)
            Log.i(TAG, "TransparentActivity something done")
        }
        finish()

    }
}