package com.zaze.demo.feature.settings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zaze.common.adapter.AbsRecyclerAdapter
import com.zaze.demo.feature.settings.databinding.SettingsItemSettingsBinding
import com.zaze.demo.feature.settings.model.SettingsItem
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

class MainSettingAdapter(context: Context, val onItemClick: (SettingsItem) -> Unit) :
    AbsRecyclerAdapter<SettingsItem, MainSettingAdapter.SettingsItemHolder>(context, diffCallback) {
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<SettingsItem>() {
            override fun areItemsTheSame(oldItem: SettingsItem, newItem: SettingsItem): Boolean {
                return false
//                return oldItem.titleRes == newItem.titleRes
            }

            override fun areContentsTheSame(oldItem: SettingsItem, newItem: SettingsItem): Boolean {
                return false
//                return oldItem.toString() == newItem.toString()
            }
        }
    }

    override fun onBindView(holder: SettingsItemHolder, value: SettingsItem, position: Int) {
        val binding = holder.binding
        binding.titleTv.setText(value.titleRes)
        binding.summaryTv.setText(value.summaryRes)
        binding.iconIv.setImageResource(value.iconRes)
        binding.iconIv.setIconBackgroundColor(getColor(value.iconBackgroundColor))
        ZLog.i(ZTag.TAG, "onBindView: $value")
        binding.root.setOnClickListener {
            onItemClick(value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsItemHolder {
        return SettingsItemHolder(
            SettingsItemSettingsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class SettingsItemHolder(val binding: SettingsItemSettingsBinding) :
        RecyclerView.ViewHolder(binding.root)


}