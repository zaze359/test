package com.zaze.demo.component.lifecycle

import android.graphics.Color
import android.util.Log
import androidx.annotation.ColorInt
import androidx.lifecycle.viewModelScope
import com.zaze.common.base.AbsViewModel
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZLogFace
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MyLifecycleViewModel : AbsViewModel(), ZLogFace {

    private var firstLogBuffer: ArrayList<String> = ArrayList()
    private var secondLogBuffer: ArrayList<String> = ArrayList()

    private val MAX_SIZE = 100

    private val channel = Channel<String>(capacity = Channel.UNLIMITED)
    private val _uiState = MutableStateFlow(LifecycleUiState.Refresh(""))
    val uiState: StateFlow<LifecycleUiState> = _uiState

    fun showLog() {
        viewModelScope.launch {
            for (log in channel) {
                when {
                    log.isEmpty() -> {
                        continue
                    }
                    secondLogBuffer.size >= MAX_SIZE -> { // 第二缓冲区已满，交换缓冲区
                        swapAndResetBuffer()
                        addLogToBuffer(secondLogBuffer, log)
                    }
                    firstLogBuffer.size >= MAX_SIZE -> { // 第一缓冲区已满，写入第二缓冲区
                        addLogToBuffer(secondLogBuffer, log)
                    }
                    else -> {
                        addLogToBuffer(firstLogBuffer, log)
                    }
                }
                val builder = StringBuilder()
                val offset = secondLogBuffer.size
                if (offset == 0) {
                    firstLogBuffer.forEach { str ->
                        builder.append(str)
                    }
                } else {
                    firstLogBuffer.subList(offset, MAX_SIZE).forEach { str ->
                        builder.append(str)
                    }
                    secondLogBuffer.forEach { str ->
                        builder.append(str)
                    }
                }
                _uiState.emit(LifecycleUiState.Refresh(builder.toString()))
            }
        }
    }

//    private fun showLog() {
//        viewModelScope.launch {
//            logState.emit(log)
//        }
//    }

    private fun swapAndResetBuffer() {
        firstLogBuffer = secondLogBuffer;
        secondLogBuffer.clear()
    }

    private fun addLogToBuffer(buffer: ArrayList<String>, log: String) {
        if (log.isEmpty()) {
            return
        }
        buffer.add(log)
    }

    private fun send(strTag: String?, strLog: String?, @ColorInt color: Int = Color.BLUE) {
        if (strLog?.contains("LifecycleActivity") == false) {
            return
        }
        viewModelScope.launch {
            channel.send("$strLog\n")
        }
    }

    override fun v(strTag: String?, strLog: String?) {
        send(strTag, strLog)
    }

    override fun d(strTag: String?, strLog: String?) {
        send(strTag, strLog)
    }

    override fun i(strTag: String?, strLog: String?) {
        send(strTag, strLog)
    }

    override fun w(strTag: String?, strLog: String?) {
        send(strTag, strLog)
    }

    override fun w(strTag: String?, strLog: String?, e: Throwable?) {
        send(strTag, strLog)
    }

    override fun e(strTag: String?, strLog: String?) {
        send(strTag, strLog)
    }

    override fun e(strTag: String?, strLog: String?, e: Throwable?) {
        send(strTag, strLog)
    }
}

sealed class LifecycleUiState {
    data class Refresh(val log: String) : LifecycleUiState()
}