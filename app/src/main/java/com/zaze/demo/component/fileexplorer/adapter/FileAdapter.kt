package com.zaze.demo.component.fileexplorer.adapter

import android.content.Context
import android.view.View
import com.zaze.common.adapter.BaseRecyclerAdapter
import com.zaze.demo.R
import com.zaze.demo.component.fileexplorer.FileEvent
import com.zaze.utils.FileUtil
import org.greenrobot.eventbus.EventBus

/**
 * Description :

 * @author : ZAZE
 * *
 * @version : 2017-07-12 - 13:26
 */
class FileAdapter(context: Context, data: Collection<FileEntity>) : BaseRecyclerAdapter<FileEntity, FileItemHolder>(context, data) {
    override fun createViewHolder(convertView: View): FileItemHolder {
        return FileItemHolder(convertView)
    }


    override fun getViewLayoutId(): Int {
        return R.layout.list_item_file
    }

    override fun onBindView(holder: FileItemHolder, value: FileEntity, position: Int) {
        val path = value.absPath
        val iconText: String
        val iconRes: Int
        when {
            FileUtil.isDirectory(path) -> {
//            iconText = "D"
//            iconRes = R.drawable.bg_circle_blue
                iconText = ""
                iconRes = R.mipmap.ic_folder_black_48dp

            }
            FileUtil.isFile(path) -> {
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
        holder.itemFileNameTv.text = value.fileName
        holder.itemView.setOnClickListener {
            EventBus.getDefault().post(FileEvent(value.absPath))
        }
    }
}
