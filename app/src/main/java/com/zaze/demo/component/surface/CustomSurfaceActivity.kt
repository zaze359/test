package com.zaze.demo.component.surface

import android.view.Choreographer
import android.view.SurfaceHolder
import com.zaze.common.base.AbsActivity

class CustomSurfaceActivity : AbsActivity(), SurfaceHolder.Callback, Choreographer.FrameCallback {







    // ------------------------------

    override fun surfaceCreated(holder: SurfaceHolder) {
        TODO("Not yet implemented")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        TODO("Not yet implemented")
    }

    // ------------------------------
    override fun doFrame(frameTimeNanos: Long) {
        TODO("Not yet implemented")
    }
}