package com.zaze.demo.component.fileexplorer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.zaze.common.adapter.third.ZUltimateRecycleAdapter
import com.zaze.demo.R
import com.zaze.demo.component.fileexplorer.FileEvent
import com.zaze.utils.ZFileUtil
import org.greenrobot.eventbus.EventBus

/**
 * Description :

 * @author : ZAZE
 * *
 * @version : 2017-07-12 - 13:26
 */
class FileAdapter(context: Context, data: Collection<FileEntity>) : ZUltimateRecycleAdapter<FileEntity, FileItemHolder>(context, data) {

    override fun getViewHolder(view: View, isItem: Boolean): FileItemHolder {
        return FileItemHolder(view, isItem)
    }

    override fun getViewLayoutId(): Int {
        return R.layout.list_item_file
    }

    override fun onBindViewHolder(holder: FileItemHolder, value: FileEntity, position: Int) {
        val path = value.absPath
        holder.itemFileNameTv.text = path
        val iconText: String
        val iconRes: Int
        if (ZFileUtil.isDirectory(path)) {
            iconText = "D"
            iconRes = R.drawable.bg_circle_blue
        } else if (ZFileUtil.isFile(path)) {
            iconText = "F"
            iconRes = R.drawable.bg_circle_green
        } else {
            iconText = "E"
            iconRes = R.drawable.bg_circle_red
        }
        holder.itemFileIconTv.text = iconText
        holder.itemFileIconTv.setBackgroundResource(iconRes)

    }

    override fun onItemClick(view: View, value: FileEntity, position: Int) {
        super.onItemClick(view, value, position)
        EventBus.getDefault().post(FileEvent(value.absPath))
    }


    override fun generateHeaderId(position: Int): Long {
        return 0
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder? {
        return null
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }
}
