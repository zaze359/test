package com.zaze.feature.sliding.conflict

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.zaze.common.base.AbsFragment
import com.zaze.feature.sliding.conflict.databinding.FragmentNestedViewPagerBinding
import com.zaze.utils.log.ZLog
import java.util.concurrent.atomic.AtomicInteger

class NestedViewPagerFragment : AbsFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNestedViewPagerBinding.inflate(inflater, container, false)
        binding.nestedViewPager.adapter = MyAdapter(
            intArrayOf(
                IdGenerator.generateViewId(),
                IdGenerator.generateViewId(),
                IdGenerator.generateViewId()
            )
        )
        binding.nestedViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
//                ZLog.i("onPageScrolled: ")
            }

            override fun onPageSelected(position: Int) {
                ZLog.i("NestedViewPagerFragment", "onPageSelected: $position")
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        return binding.root
    }
    inner class MyAdapter(val ids: IntArray) : PagerAdapter() {

        override fun getCount(): Int {
            return ids.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val textView = TextView(container.context)
            textView.text = "${ids[position]}"
            container.addView(textView, position)
            return textView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeViewAt(position)
        }
    }
}


