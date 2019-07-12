package com.zaze.demo.component.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-03-02 - 19:41
 */
public class HandlerActivity extends BaseActivity {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ZLog.d(ZTag.TAG_DEBUG, "大道无情");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handler_activity);
        findViewById(R.id.handler_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZLog.i(ZTag.TAG_DEBUG, "sendEmptyMessage start");
                mHandler.sendEmptyMessage(0);
                ZLog.i(ZTag.TAG_DEBUG, "sendEmptyMessage end");
            }
        });
        ZLog.i(ZTag.TAG_DEBUG, "test start");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ZLog.i(ZTag.TAG_DEBUG, "test run");
            }
        });
        ZLog.i(ZTag.TAG_DEBUG, "test end");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Looper.myQueue().addIdleHandler(new IdleForever());
        Looper.myQueue().addIdleHandler(new IdleOnce());
    }

    //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    class IdleForever implements MessageQueue.IdleHandler {
        /**
         * @return true : 保持此Idle一直在Handler中, 每次线程执行完后都会在这执行
         */
        @Override
        public boolean queueIdle() {
            ZLog.d(ZTag.TAG_DEBUG, "我,天地难葬!");
            return true;
        }

    }

    class IdleOnce implements MessageQueue.IdleHandler {
        /**
         * @return false : 执行一次后就从Handler线程中remove掉。
         */
        @Override
        public boolean queueIdle() {
            ZLog.d(ZTag.TAG_DEBUG, "我真的还想再活五百年~!!!");
            return false;
        }
    }
}
