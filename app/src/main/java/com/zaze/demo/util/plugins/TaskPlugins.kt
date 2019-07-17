package com.zaze.demo.util.plugins

import com.zaze.utils.task.MultiNum
import com.zaze.utils.task.Task
import com.zaze.utils.task.TaskAsyncMulti

/**
 * Description : 任务
 * @author : ZAZE
 * @version : 2018-10-22 - 21:09
 */
object TaskPlugins {
    private val downloadAppTask = Task.create<Any>("download_apk").executeOnAsyncMulti()
    private val assistTask = Task.create<Any>("request_assist_task").executeOnAsyncAuto()

    // --------------------------------------------------

    /**
     * 下载应用
     */
    @JvmStatic
    fun downloadApp(): TaskAsyncMulti<Any> {
        downloadAppTask.setMax(MultiNum.TWO).setNotifyCount(MultiNum.MIN)
        return downloadAppTask
    }

    // --------------------------------------------------

    @JvmStatic
    fun assist(): Task<Any> {
        return assistTask
    }

    @JvmStatic
    fun clearAllTask() {
        Task.clear()
    }
}