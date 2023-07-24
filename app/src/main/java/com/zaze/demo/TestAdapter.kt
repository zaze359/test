package com.zaze.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TestAdapter(private val dataList: List<String>) : RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    /**
     * 创建 ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent,false)
        return TestViewHolder(itemView)
    }

    /**
     * 返回 item数量
     */
    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     *  ViewHolder进行数据绑定。
     */
    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.itemNameTv.text = dataList[position]
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: TestViewHolder) {
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: TestViewHolder) {
        super.onViewDetachedFromWindow(holder)
    }

    class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTv: TextView = itemView.findViewById(R.id.item_name_tv)
    }
}