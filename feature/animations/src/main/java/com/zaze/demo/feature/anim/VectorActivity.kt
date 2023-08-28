package com.zaze.demo.feature.anim

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.zaze.demo.feature.anim.databinding.ActivityVectorBinding

class VectorActivity : AppCompatActivity() {

    private lateinit var beginAnim: AnimatedVectorDrawable
    private lateinit var finishAnim: AnimatedVectorDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityVectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        beginAnim = ResourcesCompat.getDrawable(
            resources,
            R.drawable.download_vector_start,
            theme
        ) as AnimatedVectorDrawable
        finishAnim = ResourcesCompat.getDrawable(
            resources,
            R.drawable.download_vector_finish,
            theme
        ) as AnimatedVectorDrawable

//            beginAnim.registerAnimationCallback(object : Animatable2.AnimationCallback() {
//                override fun onAnimationEnd(drawable: Drawable?) {
//                    super.onAnimationEnd(drawable)
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !finishAnim.isRunning) {
//                        vector_iv.setImageDrawable(finishAnim)
//                        finishAnim.start()
//                    }
//                }
//            })
        binding.vectorBeginBtn.setOnClickListener {
            if (!beginAnim.isRunning) {
                binding.vectorIv.setImageDrawable(beginAnim)
                beginAnim.start()
            }
        }

        binding.vectorFinishBtn.setOnClickListener {
            beginAnim.stop()
            if (!finishAnim.isRunning) {
                binding.vectorIv.setImageDrawable(finishAnim)
                finishAnim.start()
            }
        }

    }
}
