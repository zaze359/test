package com.zaze.feature.sliding.conflict

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.AbsFragment
import com.zaze.common.base.ext.initToolbar
import com.zaze.feature.sliding.conflict.databinding.ActivitySlidingConfictBinding

class SlidingConflictActivity : AbsActivity() {

    lateinit var binding: ActivitySlidingConfictBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlidingConfictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar(binding.toolbar) {
            title = "测试滑动冲突"
        }

        val fragmentList = ArrayList<AbsFragment>()
        fragmentList.add(NestedRecyclerViewFragment())
        fragmentList.add(NestedViewPagerFragment())
        fragmentList.add(NestedRecyclerViewFragment())
        fragmentList.add(NestedViewPagerFragment())
        binding.slidingConflictViewPager.adapter = MyPager2Adapter(this, fragmentList)
    }

    inner class MyPager2Adapter(fa: FragmentActivity, list: ArrayList<AbsFragment>?) :
        FragmentStateAdapter(fa) {

        private val fragmentList = ArrayList<AbsFragment>()

        init {
            fragmentList.clear()
            if (list != null) {
                fragmentList.addAll(list)
            }
        }

        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun getItemId(position: Int): Long {
            return super.getItemId(position)
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }
    }

    @Deprecated("")
    inner class MyPagerAdapter(fm: FragmentManager, list: ArrayList<AbsFragment>?) :
        FragmentStatePagerAdapter(fm) {
        private val fragmentList = ArrayList<AbsFragment>()

        init {
            fragmentList.clear()
            if (list != null) {
                fragmentList.addAll(list)
            }
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItemPosition(`object`: Any): Int {
//            return POSITION_NONE
            return super.getItemPosition(`object`)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            return super.instantiateItem(container, position)
        }
    }
}