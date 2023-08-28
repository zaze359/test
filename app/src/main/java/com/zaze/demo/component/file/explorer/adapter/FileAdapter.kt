package com.zaze.demo.component.file.explorer.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.Color
import com.zaze.common.adapter.BaseRecyclerAdapter
import com.zaze.demo.R
import com.zaze.demo.component.file.explorer.FileListViewModel
import java.io.File

/**
 * Description :

 * @author : ZAZE
 * *
 * @version : 2017-07-12 - 13:26
 */
class FileAdapter(context: Context, data: Collection<File>?) :
    BaseRecyclerAdapter<File, FileItemHolder>(context, data) {
    private var viewModel: FileListViewModel? = null
    override fun createViewHolder(convertView: View): FileItemHolder {
        return FileItemHolder(convertView)
    }

    fun setViewMode(viewModel: FileListViewModel) {
        this.viewModel = viewModel
    }

    override fun getViewLayoutId(): Int {
        return R.layout.list_item_file
    }

    override fun onBindView(holder: FileItemHolder, value: File, position: Int) {
        val iconText: String
        val iconRes: Int

        @ColorInt
        val iconTintColor: Int
        when {
            value.isDirectory -> {
//            iconText = "D"
//            iconRes = R.drawable.bg_circle_blue
                iconText = ""
                iconRes = R.drawable.ic_folder_black_48dp
                iconTintColor = getColor(R.color.black_dark)
            }

            value.isFile -> {
                iconText = "F"
                iconRes = R.drawable.bg_circle
                iconTintColor = getColor(R.color.green_light)
            }

            else -> {
                iconText = "E"
                iconRes = R.drawable.bg_circle
                iconTintColor = getColor(R.color.red_light)
            }
        }
        holder.itemFileIconTv.text = iconText
        holder.itemFileIconTv.setBackgroundResource(iconRes)
        holder.itemFileIconTv.backgroundTintList = ColorStateList.valueOf(iconTintColor)
        holder.itemFileNameTv.text = value.name
        holder.itemView.setOnClickListener {
            viewModel?.openFileOrDir(value)
        }
    }
}
