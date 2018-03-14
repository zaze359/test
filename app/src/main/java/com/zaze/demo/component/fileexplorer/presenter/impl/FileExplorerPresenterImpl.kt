package com.zaze.demo.component.fileexplorer.presenter.impl

import android.os.Environment
import com.zaze.common.base.mvp.BaseMvpPresenter
import com.zaze.demo.component.fileexplorer.FileEvent
import com.zaze.demo.component.fileexplorer.adapter.FileEntity
import com.zaze.demo.component.fileexplorer.presenter.FileExplorerPresenter
import com.zaze.demo.component.fileexplorer.view.FileExplorerView
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File

/**
 * Description :

 * @author : zaze
 * *
 * @version : 2017-05-05 04:42 1.0
 */
class FileExplorerPresenterImpl(view: FileExplorerView) : BaseMvpPresenter<FileExplorerView>(view), FileExplorerPresenter {

    private var curFile = Environment.getRootDirectory()

    override fun loadFileList() {
        if (curFile.exists()) {
            if (curFile.isDirectory) {
                val list = ArrayList<FileEntity>()
                val fileList = curFile.listFiles()
                if (fileList != null && !fileList.isEmpty()) {
                    for (file in fileList) {
                        val fileEntity = FileEntity()
                        fileEntity.fileName = file.name
                        fileEntity.absPath = file.absolutePath
                        ZLog.i(ZTag.TAG_DEBUG, "file : ${fileEntity.absPath}")
                        list.add(fileEntity)
                    }
                }
                view.showFileList(list)
            }
        }
    }

    override fun backToParent() {
        if (curFile.parentFile != null && curFile.parentFile.exists()) {
            curFile = curFile.parentFile
        }
        loadFileList()
    }

    override fun openFileOrDir(fileEvent: FileEvent?) {
        if (fileEvent != null) {
            val absPath = fileEvent.absPath
            val file = File(absPath)
            if (file.isDirectory) {
                ZLog.i(ZTag.TAG_DEBUG, "打开文件夹 : $absPath")
                curFile = file
                loadFileList()
            } else if (file.isFile) {
                ZLog.i(ZTag.TAG_DEBUG, "打开文件 : $absPath")
            } else {
                ZLog.e(ZTag.TAG_DEBUG, "ERROR : $absPath")
            }
        }
    }
}
