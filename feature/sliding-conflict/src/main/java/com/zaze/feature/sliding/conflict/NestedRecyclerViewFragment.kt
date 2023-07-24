package com.zaze.feature.sliding.conflict

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.zaze.common.base.AbsFragment
import com.zaze.feature.sliding.conflict.databinding.FragmentNestedRecyclerViewBinding
import com.zaze.feature.sliding.conflict.databinding.FragmentNestedViewPagerBinding
import com.zaze.utils.log.ZLog
import java.util.concurrent.atomic.AtomicInteger

class NestedRecyclerViewFragment : AbsFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNestedRecyclerViewBinding.inflate(inflater, container, false)
        binding.nestedRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.nestedRecyclerView.adapter = MyAdapter(
            intArrayOf(
                IdGenerator.generateViewId(),
                IdGenerator.generateViewId(),
                IdGenerator.generateViewId(),
                IdGenerator.generateViewId(),
                IdGenerator.generateViewId(),
                IdGenerator.generateViewId(),
                IdGenerator.generateViewId(),
            )
        )
        return binding.root
    }

    inner class MyAdapter(val ids: IntArray) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val textView = TextView(parent.context)
            textView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            return ViewHolder(textView)
        }

        override fun getItemCount(): Int {
            return ids.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            (holder.itemView as TextView).text = "${ids[position]}"
        }

    }
}


