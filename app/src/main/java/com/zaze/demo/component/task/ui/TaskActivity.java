package com.zaze.demo.component.task.ui;


import android.os.Bundle;
import android.view.View;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.task.presenter.TaskPresenter;
import com.zaze.demo.component.task.presenter.impl.TaskPresenterImpl;
import com.zaze.demo.component.task.view.TaskView;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.ZCallback;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.Task;
import com.zaze.utils.task.TaskEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-03-23 11:37 1.0
 */
public class TaskActivity extends BaseActivity implements TaskView {
    private TaskPresenter presenter;
    private String tag = "test";
    private Task testTask = Task.create(tag);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);
        presenter = new TaskPresenterImpl(this);
        Task.setNeedLog(true);
        findViewById(R.id.task_execute_test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushSyncTask();
            }
        });
        findViewById(R.id.task_push_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushTask();
            }
        });


        findViewById(R.id.task_execute_async_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testTask.executeOnAsync().execute();
            }
        });
        findViewById(R.id.task_auto_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testTask.executeOnAsyncAuto().execute();
            }
        });
        findViewById(R.id.task_multi_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testTask.executeOnAsyncMulti().execute();
            }
        });
    }

    public void pushSyncTask() {
        for (int i = 0; i < 100; i++) {
            aaa(i);
        }
    }

    private void aaa(final int taskId) {
//        ThreadManager.getInstance().runInMultiThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(500);
//                    ZLog.i(ZTag.TAG_DEBUG, "taskId : " + taskId);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        testTask.push(new TaskEntity(String.valueOf(taskId)) {
            @Override
            public void onExecute(TaskEntity task) throws Exception {
                Thread.sleep(500);
                ZLog.i(ZTag.TAG_DEBUG, "==================================================");
                ZLog.i(ZTag.TAG_DEBUG, "taskId : " + taskId);
                ZLog.i(ZTag.TAG_DEBUG, "this.hashCode : " + this.hashCode());
                ZLog.i(ZTag.TAG_DEBUG, "task.getTaskId : " + task.getTaskId());
                ZLog.i(ZTag.TAG_DEBUG, "==================================================");
            }
        }).executeOnAsyncAuto().execute();
    }

    public void pushTask() {
        ThreadManager.getInstance().runInSingleThread(new Runnable() {
            @Override
            public void run() {
                List<TaskEntity> list = new ArrayList<>();
                for (int i = 1; i <= 1000; i++) {
                    list.add(new TaskEntity(String.valueOf(i)) {
                        @Override
                        public void onExecute(TaskEntity task) throws Exception {
                            String taskId = task.getTaskId();
                            final Thread thread = Thread.currentThread();
                            synchronized (thread) {
                                ZLog.i(ZTag.TAG_DEBUG, "onExecute start : " + taskId);
                                onExecuteTask(taskId, new ZCallback<String>() {

                                    @Override
                                    public void onNext(String s) {
                                        ZLog.i(ZTag.TAG_DEBUG, "onExecute onNext : " + s);
                                        synchronized (thread) {
                                            thread.notify();
                                        }
                                    }

                                    @Override
                                    public void onCompleted() {
                                    }
                                });
                                thread.wait(5000);
                                ZLog.i(ZTag.TAG_DEBUG, "onExecute finish : " + taskId);
                            }

                        }
                    });
                }
                testTask.push(list);
            }
        });
    }

    private void onExecuteTask(final String taskId, final ZCallback<String> callback) {
        ThreadManager.getInstance().runInMultiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callback.onNext(taskId);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Task.clear();
    }
}
