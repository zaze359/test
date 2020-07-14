package com.zaze.demo.component.file.explorer

import android.os.Bundle
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.replaceFragmentInActivity
import com.zaze.common.base.ext.setupActionBar
import com.zaze.demo.R
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.android.synthetic.main.file_explorer_activity.*

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-05-05 04:42 1.0
 */
class FileExplorerActivity : AbsActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.file_explorer_activity)
        setupActionBar(fileExplorerToolbar) {
            setTitle("文件管理")
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        if (!FileUtil.isSdcardEnable()) {
            ZLog.e(ZTag.TAG_DEBUG, "sdcard 不可用")
            return
        }
        replaceFragmentInActivity(FileListFragment(), R.id.fileExplorerLayout)
    }

    override fun init(savedInstanceState: Bundle?) {}
}