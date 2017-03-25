package com.zaze.demo.component.task.ui;


import android.os.Bundle;
import android.view.View;

import com.zaze.aarrepo.commons.base.ZBaseActivity;
import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.commons.task.TaskCallback;
import com.zaze.aarrepo.commons.task.TaskEntity;
import com.zaze.aarrepo.commons.task.executor.TaskExecutorManager;
import com.zaze.aarrepo.utils.JsonUtil;
import com.zaze.aarrepo.utils.ZTag;
import com.zaze.demo.R;
import com.zaze.demo.component.task.presenter.TaskPresenter;
import com.zaze.demo.component.task.presenter.impl.TaskPresenterImpl;
import com.zaze.demo.component.task.view.TaskView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-03-23 11:37 1.0
 */
public class TaskActivity extends ZBaseActivity implements TaskView {
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
                    ZLog.v(ZTag.TAG_TASK, JsonUtil.objToJson(entity));
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
                    ZLog.v(ZTag.TAG_TASK, JsonUtil.objToJson(entity));
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
                ZLog.v(ZTag.TAG_TASK, JsonUtil.objToJson(entity));
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
        for (int i = 0; i < 10000; i++) {
            TaskExecutorManager.getInstance().pushTask(new TaskEntity(String.valueOf(i)), new TaskCallback() {
                @Override
                public void onExecute(TaskEntity entity) {
                    ZLog.v(ZTag.TAG_TASK, JsonUtil.objToJson(entity));
//                    if ("50".equals(entity.getTaskId())) {
//                        TaskExecutorManager.getInstance().shutdownAutoExecuteTask();
//                    }
                }
            });
        }
        TaskExecutorManager.getInstance().autoExecuteTask();
    }

    @OnClick(R.id.task_multi_btn)
    public void multiExecute(View view) {
        for (int i = 0; i < 10000; i++) {
            TaskExecutorManager.getInstance().pushTask(new TaskEntity(String.valueOf(i)), new TaskCallback() {
                @Override
                public void onExecute(TaskEntity entity) {
                    ZLog.v(ZTag.TAG_TASK, JsonUtil.objToJson(entity));
//                    TaskExecutorManager.getInstance().executeMulti(1);
                    TaskExecutorManager.getInstance().executeSyncNext();
                }
            });
        }
        TaskExecutorManager.getInstance().executeMulti(10);
    }
}
