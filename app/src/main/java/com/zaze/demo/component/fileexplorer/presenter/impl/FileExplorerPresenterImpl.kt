package com.zaze.demo.component.fileexplorer.presenter.impl

import com.zaze.aarrepo.commons.base.ZBasePresenter
import com.zaze.aarrepo.commons.log.ZLog
import com.zaze.aarrepo.utils.RootCmd
import com.zaze.aarrepo.utils.ZTag
import com.zaze.demo.component.fileexplorer.FileEvent
import com.zaze.demo.component.fileexplorer.adapter.FileEntity
import com.zaze.demo.component.fileexplorer.presenter.FileExplorerPresenter
import com.zaze.demo.component.fileexplorer.view.FileExplorerView
import java.io.File

/**
 * Description :

 * @author : zaze
 * *
 * @version : 2017-05-05 04:42 1.0
 */
class FileExplorerPresenterImpl(view: FileExplorerView) : ZBasePresenter<FileExplorerView>(view), FileExplorerPresenter {

    private var curDirPath = "/"

    override fun loadFileList() {
        val commandResult = RootCmd.execCmdForRes("ls $curDirPath")
        val fileList = ArrayList<FileEntity>()
        if (RootCmd.isSuccess(commandResult.result)) {
            for (fileName in commandResult.msgList) {
                val fileEntity = FileEntity()
                fileEntity.fileName = "/$fileName"
                fileEntity.absPath = curDirPath + fileName
                ZLog.i(ZTag.TAG_DEBUG, "file : ${fileEntity.absPath}")
                fileList.add(fileEntity)
            }
            view.showFileList(fileList)
        } else {
            ZLog.i(ZTag.TAG_ERROR, commandResult.errorMsg)
        }
    }

    override fun backToParent() {
        val file = File(curDirPath)
        if (file.parentFile != null) {
            curDirPath = file.parentFile.absolutePath
            loadFileList()
        }
    }

    override fun openFileOrDir(fileEvent: FileEvent?) {
        if (fileEvent != null) {
            val absPath = fileEvent.absPath
            val file = File(absPath)
            if (file.isDirectory) {
                ZLog.i(ZTag.TAG_DEBUG, "打开文件夹 : $absPath")
                curDirPath = "$absPath/"
                loadFileList()
            } else if (file.isFile) {
                ZLog.i(ZTag.TAG_DEBUG, "打开文件 : $absPath")
            } else {
                ZLog.e(ZTag.TAG_DEBUG, "ERROR : $absPath")
            }
        }
    }
}
