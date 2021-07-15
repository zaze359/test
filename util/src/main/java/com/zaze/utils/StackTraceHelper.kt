package com.zaze.utils

import java.io.PrintWriter
import java.io.StringWriter
import java.util.*

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
object StackTraceHelper {
    private val STACK_CALLERS: MutableSet<String>

    init {
        STACK_CALLERS = HashSet()
        registerStackCaller(StackTraceHelper::class.java.name)
    }

    @JvmStatic
    fun registerStackCaller(logCaller: String) {
        STACK_CALLERS.add(logCaller)
    }

    /**
     * @return 直接调用方法的堆栈信息
     */
    fun currentStackTraceElement() = getStackTraceElement(1)

    /**
     * @return 间接调用的堆栈信息
     */
    @JvmStatic
    fun callerStackTraceElement(): StackTraceElement {
        return Throwable().stackTrace.firstOrNull { stackTraceElement ->
            STACK_CALLERS.none { caller ->
                stackTraceElement.className.startsWith(caller)
            }
        } ?: currentStackTraceElement()
    }

    /**
     * @param position 堆栈位置
     * @return 堆栈信息
     */
    fun getStackTraceElement(position: Int): StackTraceElement {
        return Throwable().stackTrace[position]
    }

    /**
     * @param e 异常
     * @return 获取报错的堆栈信息
     */
    fun getErrorMsg(e: Throwable?): String {
        return if (e != null) {
            val stringWriter = StringWriter()
            val printWriter = PrintWriter(stringWriter, false)
            try {
                e.printStackTrace(printWriter)
                printWriter.flush()
                stringWriter.toString()
            } finally {
                printWriter.close()
            }
        } else {
            ""
        }
    }
}