package com.zaze.demo.component.animation

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import com.zaze.demo.R
import kotlinx.android.synthetic.main.activity_vector.*


class VectorActivity : AppCompatActivity() {

    lateinit var beginAnim: AnimatedVectorDrawable
    lateinit var finishAnim: AnimatedVectorDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vector)
        beginAnim = ResourcesCompat.getDrawable(resources, R.drawable.download_vector_start, theme) as AnimatedVectorDrawable
        finishAnim = ResourcesCompat.getDrawable(resources, R.drawable.download_vector_finish, theme) as AnimatedVectorDrawable

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            beginAnim.registerAnimationCallback(object : Animatable2.AnimationCallback() {
//                override fun onAnimationEnd(drawable: Drawable?) {
//                    super.onAnimationEnd(drawable)
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !finishAnim.isRunning) {
//                        vector_iv.setImageDrawable(finishAnim)
//                        finishAnim.start()
//                    }
//                }
//            })
        }
        vector_begin_btn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !beginAnim.isRunning) {
                vector_iv.setImageDrawable(beginAnim)
                beginAnim.start()
            }
        }

        vector_finish_btn.setOnClickListener {
            beginAnim.stop()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !finishAnim.isRunning) {
                vector_iv.setImageDrawable(finishAnim)
                finishAnim.start()
            }
        }

    }
}
