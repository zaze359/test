package com.zaze.demo.feature.drawable

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.zaze.common.base.AbsActivity
import com.zaze.demo.feature.drawable.databinding.ActivityDrawableBinding
import com.zaze.utils.log.ZLog


class DrawableActivity : AbsActivity() {

    lateinit var binding: ActivityDrawableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 先创建三个drawable
        val originDrawable = loadDrawable()
        val tintDrawable = loadDrawable()
        val mutateDrawable = loadDrawable()?.mutate()
        //
        binding.drawableFirstIv.setImageDrawable(originDrawable)
        binding.drawableSecondIv.setImageDrawable(originDrawable)
        binding.drawableThirdIv.setImageDrawable(originDrawable)
        binding.drawableFourthIv.setImageDrawable(originDrawable)

        binding.drawableFirstIv.setOnClickListener {
            tintDrawable?.let {
//                DrawableCompat.wrap(it)
                DrawableCompat.setTint(it, Color.BLACK)
                it.alpha = 50
                binding.drawableSecondIv.setImageDrawable(it)
            }
            mutateDrawable?.let {
                // 这里修改透明度，并不会影响其他Drawable。
                it.alpha = 200
                binding.drawableThirdIv.setImageDrawable(it)
            }
//            binding.drawableFirstIv.setImageDrawable(originDrawable)
            loadDrawable()?.let {
                // 这里是在修改了状态后创建的 drawable，会发现创建的 drawable 也被修改了
                // 它和 tintDrawable 是相同的
                binding.drawableFourthIv.setImageDrawable(it)
            }
        }
    }

    private fun loadDrawable(): Drawable? {
        return AppCompatResources.getDrawable(this, R.drawable.jljt)
//        return ContextCompat.getDrawable(this, R.drawable.jljt)
    }


}