package com.zaze.demo.component.task.ui;


import android.os.Bundle;
import android.view.View;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.task.presenter.TaskPresenter;
import com.zaze.demo.component.task.presenter.impl.TaskPresenterImpl;
import com.zaze.demo.component.task.view.TaskView;
import com.zaze.utils.ZJsonUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.TaskCallback;
import com.zaze.utils.task.TaskEntity;
import com.zaze.utils.task.executor.TaskExecutorManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-03-23 11:37 1.0
 */
public class TaskActivity extends BaseActivity implements TaskView {
    private TaskPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        TaskExecutorManager.getInstance().setNeedLog(true);
        presenter = new TaskPresenterImpl(this);
    }

    @OnClick(R.id.task_btn)
    public void pushAndExecute(View view) {
        pushTask("test");
        TaskExecutorManager.getInstance().executeSyncNext();
    }

    @OnClick(R.id.task_push_sync_btn)
    public void pushSyncTask(View view) {
        for (int i = 0; i < 1000; i++) {
            TaskExecutorManager.getInstance().pushTask(new TaskEntity(String.valueOf(i)), new TaskCallback() {
                @Override
                public void onExecute(TaskEntity entity) {
                    ZLog.v(ZTag.TAG_TASK, ZJsonUtil.objToJson(entity));
                    TaskExecutorManager.getInstance().executeSyncNext();
                }
            });
        }
    }

    @OnClick(R.id.task_push_async_btn)
    public void pushAsyncTask(View view) {
        for (int i = 0; i < 1000; i++) {
            TaskExecutorManager.getInstance().pushTask(new TaskEntity(String.valueOf(i)), new TaskCallback() {
                @Override
                public void onExecute(TaskEntity entity) {
                    ZLog.v(ZTag.TAG_TASK, ZJsonUtil.objToJson(entity));
                    TaskExecutorManager.getInstance().executeAsyncNext();
                }
            });
        }
    }

    private void pushTask(final String tag) {
        ZLog.v("pushTask", tag);
        TaskExecutorManager.getInstance().pushTask(tag, new TaskEntity(), new TaskCallback() {
            @Override
            public void onExecute(TaskEntity entity) {
                ZLog.v(ZTag.TAG_TASK, ZJsonUtil.objToJson(entity));
            }
        });
    }

    // --------------------------------------------------
    @OnClick(R.id.task_execute_btn)
    public void executeNext(View view) {
        TaskExecutorManager.getInstance().executeSyncNext();
    }

    @OnClick(R.id.task_auto_btn)
    public void autoExecute(View view) {
        String tag = "aaaaa";
        for (int i = 0; i < 1000; i++) {
            TaskExecutorManager.getInstance().pushTask(tag, new TaskEntity(String.valueOf(i)), new TaskCallback() {
                @Override
                public void onExecute(TaskEntity entity) {
                    ZLog.v(ZTag.TAG_TASK, ZJsonUtil.objToJson(entity));
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        for (int i = 1000; i < 2000; i++) {
            TaskExecutorManager.getInstance().pushTask(new TaskEntity(String.valueOf(i)), new TaskCallback() {
                @Override
                public void onExecute(TaskEntity entity) {
                    ZLog.v(ZTag.TAG_TASK, ZJsonUtil.objToJson(entity));
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        TaskExecutorManager.getInstance().autoExecuteTask();
        TaskExecutorManager.getInstance().autoExecuteTask(tag);
    }

    @OnClick(R.id.task_multi_btn)
    public void multiExecute(View view) {
        for (int i = 0; i < 10000; i++) {
            TaskExecutorManager.getInstance().pushTask(new TaskEntity(String.valueOf(i)), new TaskCallback() {
                @Override
                public void onExecute(TaskEntity entity) {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ZLog.v(ZTag.TAG_TASK, ZJsonUtil.objToJson(entity));
                    TaskExecutorManager.getInstance().executeMulti(1);
                }
            });
        }
        TaskExecutorManager.getInstance().executeMulti(10);
    }
}
