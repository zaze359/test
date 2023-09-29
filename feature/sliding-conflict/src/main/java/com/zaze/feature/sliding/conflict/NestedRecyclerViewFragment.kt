package com.zaze.feature.sliding.conflict

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaze.common.base.AbsFragment
import com.zaze.feature.sliding.conflict.databinding.FragmentNestedRecyclerViewBinding
import com.zaze.utils.IdGenerator

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
                IdGenerator.generateId(),
                IdGenerator.generateId(),
                IdGenerator.generateId(),
                IdGenerator.generateId(),
                IdGenerator.generateId(),
                IdGenerator.generateId(),
                IdGenerator.generateId(),
                IdGenerator.generateId(),
                IdGenerator.generateId(),
                IdGenerator.generateId(),
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
                400
            )
            textView.gravity = Gravity.CENTER
            return ViewHolder(textView)
        }

        override fun getItemCount(): Int {
            return ids.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            (holder.itemView as TextView).text = "RecyclerView: ${ids[position]}"
        }

    }
}


