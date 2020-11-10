package com.zaze.demo.component.coroutine

import android.os.Bundle
import com.zaze.common.base.AbsActivity
import com.zaze.demo.R
import kotlinx.android.synthetic.main.coroutine_act.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * Description :
 * @author : ZAZE
 * @version : 2020-08-13 - 13:50
 */
class CoroutineActivity : AbsActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coroutine_act)
        coroutineBtn.setOnClickListener {
            f4()
        }
    }

    fun f1() {
        GlobalScope.launch { // 在后台启动⼀个新的协程并继续
            delay(1000L) // ⾮阻塞的等待 1 秒钟（默认时间单位是毫秒）
            println("World !") // 在延迟后打印输出
        }
        println("Hello,") // 协程已在等待时主线程还在继续
    }

    fun f2() = runBlocking { // this: CoroutineScope
        launch { // 在 runBlocking 作⽤域中启动⼀个新协程
            delay(1000L)
            println("World!")
        }
        println("Hello,")
    }

    fun f3() = runBlocking {
        // this: CoroutineScope
        launch {
            delay(200L)
            println("Task from runBlocking") // 222
        }
        coroutineScope { // 创建⼀个协程作⽤域
            launch {
                delay(500L)
                println("Task from nested launch") // 333
            }
            delay(100L)
            println("Task from coroutine scope") // 111 这⼀⾏会在内嵌 launch 之前输出
        }
        coroutineScope { // 创建⼀个协程作⽤域
            launch {
                delay(500L)
                println("222222") // 333
            }
            delay(100L)
            println("111111") // 111 这⼀⾏会在内嵌 launch 之前输出
        }
        println("Coroutine scope is over") // 这⼀⾏在内嵌 launch 执⾏完毕后才输出
    }

    fun f4() = runBlocking {
        val time = measureTimeMillis {
            val one = async { doSomethingUsefulOne() }
            val two = async { doSomethingUsefulTwo() }
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")
    }

    suspend fun doSomethingUsefulOne(): Int {
        delay(2000L) // 假设我们在这⾥做了⼀些有⽤的事
        return 13
    }

    suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // 假设我们在这⾥也做了⼀些有⽤的事
        return 29
    }
}