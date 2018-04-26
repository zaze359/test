
package com.zaze.demo.debug;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;

import java.util.LinkedList;

/**
 * 空闲时执行
 * 队列在 一个loop thread中运行.
 * 实际上item 不会加入队列，直到最后一个开始运行
 * 避免线程饥饿
 * 先进先出
 */
public class DeferredHandler {
    final LinkedList<Runnable> mQueue = new LinkedList<>();
    private MessageQueue mMessageQueue = Looper.myQueue();
    private Impl mHandler;


    public DeferredHandler() {
        this(null);
    }

    public DeferredHandler(Looper looper) {
        if (looper != null) {
            mHandler = new Impl(looper);
        } else {
            mHandler = new Impl();
        }
    }


    /**
     * Schedule runnable to run after everything that's on the queue right now.
     */
    public void post(Runnable runnable) {
        synchronized (mQueue) {
            mQueue.add(runnable);
            if (mQueue.size() == 1) {
                scheduleNextLocked();
            }
        }
    }

    /**
     * Schedule runnable to run when the queue goes idle.
     */
    public void postIdle(final Runnable runnable) {
        post(new IdleRunnable(runnable));
    }

    public void cancelAll() {
        synchronized (mQueue) {
            mQueue.clear();
        }
    }

    /**
     * Runs all queued Runnables from the calling thread.
     */
    public void flush() {
        LinkedList<Runnable> queue = new LinkedList<>();
        synchronized (mQueue) {
            queue.addAll(mQueue);
            mQueue.clear();
        }
        for (Runnable r : queue) {
            r.run();
        }
    }


    /**
     * 取出延迟队列中的第一个,  若是延迟执行的消息则加入到消息机制的延迟处理(IdleHandler)中
     * 否则就发送消息交由消息机制处理
     */
    void scheduleNextLocked() {
        if (mQueue.size() > 0) {
            Runnable peek = mQueue.getFirst();
            if (peek instanceof IdleRunnable) {
                mMessageQueue.addIdleHandler(mHandler);
            } else {
                mHandler.sendEmptyMessage(1);
            }
        }
    }


    class Impl extends Handler implements MessageQueue.IdleHandler {
        public Impl() {
        }

        public Impl(Looper looper) {
            super(looper);
        }

        @Override
        public boolean queueIdle() {
            handleMessage(null);
            return false;
        }

        @Override
        public void handleMessage(Message msg) {
            Runnable r;
            synchronized (mQueue) {
                if (mQueue.size() == 0) {
                    return;
                }
                r = mQueue.removeFirst();
            }
            r.run();
            synchronized (mQueue) {
                scheduleNextLocked();
            }
        }
    }

    private class IdleRunnable implements Runnable {
        Runnable mRunnable;

        IdleRunnable(Runnable r) {
            mRunnable = r;
        }

        @Override
        public void run() {
            mRunnable.run();
        }
    }
}

