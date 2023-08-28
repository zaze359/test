package com.zaze.demo.feature.image

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import coil.load
import coil.transform.CircleCropTransformation
import com.zaze.common.base.AbsActivity
import com.zaze.demo.feature.image.coil.BlurTransformation
import com.zaze.demo.feature.image.coil.GrayscaleTransformation
import com.zaze.demo.feature.image.databinding.ActivityDrawableBinding

class DrawableActivity : AbsActivity() {

    lateinit var binding: ActivityDrawableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawableBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.drawableFirstIv.load(R.drawable.jljt) {
//            // 开启图片采样, 淡入淡出
//            crossfade(true)
//            transformations(listOf(
//                CircleCropTransformation(), // 图形裁剪
////                BlurTransformation(context = applicationContext, radius = 2F, sampling = 2F), //高斯模糊效果
//                GrayscaleTransformation() // 灰度图
//            ))
//        }

        // 先创建三个drawable
        val originDrawable = loadDrawable()
        val tintDrawable = loadDrawable()
        // 同资源的drawable默认状态是共享的，通过 mutate() 单独新建一个状态
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