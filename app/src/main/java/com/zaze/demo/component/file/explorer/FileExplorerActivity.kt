package com.zaze.demo.component.file.explorer

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.replaceFragment
import com.zaze.common.base.ext.setupActionBar
import com.zaze.demo.R
import com.zaze.demo.databinding.FileExplorerActivityBinding
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-05-05 04:42 1.0
 */
class FileExplorerActivity : AbsActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<FileExplorerActivityBinding>(
            this,
            R.layout.file_explorer_activity
        )
        setupActionBar(binding.fileExplorerToolbar) {
            setTitle("文件管理")
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        if (!FileUtil.isSdcardEnable()) {
            ZLog.e(ZTag.TAG_DEBUG, "sdcard 不可用")
            return
        }
        replaceFragment(R.id.fileExplorerLayout, FileListFragment())
    }
}