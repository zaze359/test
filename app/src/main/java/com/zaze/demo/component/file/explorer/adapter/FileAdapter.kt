package com.zaze.demo.component.file.explorer.adapter

import android.content.Context
import android.view.View
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
class FileAdapter(context: Context, data: Collection<File>?) : BaseRecyclerAdapter<File, FileItemHolder>(context, data) {
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
        when {
            value.isDirectory -> {
//            iconText = "D"
//            iconRes = R.drawable.bg_circle_blue
                iconText = ""
                iconRes = R.drawable.ic_folder_black_48dp

            }
            value.isFile -> {
                iconText = "F"
                iconRes = R.drawable.bg_circle_green
            }
            else -> {
                iconText = "E"
                iconRes = R.drawable.bg_circle_red
            }
        }
        holder.itemFileIconTv.text = iconText
        holder.itemFileIconTv.setBackgroundResource(iconRes)
        holder.itemFileNameTv.text = value.name
        holder.itemView.setOnClickListener {
            viewModel?.openFileOrDir(value)
        }
    }
}
