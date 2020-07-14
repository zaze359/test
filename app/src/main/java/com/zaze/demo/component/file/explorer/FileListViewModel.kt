package com.zaze.demo.component.file.explorer

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.zaze.common.base.AbsViewModel
import com.zaze.common.base.ext.set
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.File

/**
 * Description :
 * @author : ZAZE
 * @version : 2020-05-06 - 11:26
 */
class FileListViewModel : AbsViewModel() {
    val curFileData = MutableLiveData<File>()
    val fileListData = MutableLiveData<List<File>>()

    fun init() {
        loadFileList(curFileData.value ?: Environment.getExternalStorageDirectory())
    }

    fun loadFileList(curFile: File) {
        if (!curFile.isDirectory || !curFile.exists()) {
            return
        }
        curFileData.set(curFile)
        fileListData.set(curFile.listFiles()?.toList())
    }

    fun back() {
        val curFile = curFileData.value
        if (curFile == null || curFile.parentFile == null) {
            return
        }
        loadFileList(curFile.parentFile)
    }

    fun openFileOrDir(file: File) {
        when {
            file.isDirectory -> {
                ZLog.i(ZTag.TAG_DEBUG, "打开文件夹: ${file.absolutePath}")
                loadFileList(file)
            }
            file.isFile -> {
                ZLog.i(ZTag.TAG_DEBUG, "打开文件: ${file.absolutePath}")
            }
            else -> ZLog.e(ZTag.TAG_DEBUG, "不是文件也不是文件夹: ${file.absolutePath}")
        }
    }

}