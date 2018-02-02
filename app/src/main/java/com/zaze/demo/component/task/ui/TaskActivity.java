package com.zaze.demo.component.task.ui;


import android.os.Bundle;
import android.view.View;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.task.presenter.TaskPresenter;
import com.zaze.demo.component.task.presenter.impl.TaskPresenterImpl;
import com.zaze.demo.component.task.view.TaskView;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.task.Executor;
import com.zaze.utils.task.Task;
import com.zaze.utils.task.TaskEntity;
import com.zaze.utils.task.TaskSchedulers;

import java.util.ArrayList;
import java.util.List;

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
    private String tag = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        presenter = new TaskPresenterImpl(this);
        Task.setNeedLog(true);
    }

    @OnClick(R.id.task_push_btn)
    public void pushAndExecute(View view) {
        ThreadManager.getInstance().runInSingleThread(new Runnable() {
            @Override
            public void run() {
                List<TaskEntity> list = new ArrayList<>();
                for (int i = 1; i <= 1000; i++) {
                    list.add(new TaskEntity(String.valueOf(i)));
                }
                Task.create(tag).push(list);
            }
        });
    }

    @OnClick(R.id.task_execute_sync_btn)
    public void pushSyncTask(View view) {
        Task.create(tag).executeOn(TaskSchedulers.SYNC).execute(new Executor<TaskEntity>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onExecute(TaskEntity task) throws Exception {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @OnClick(R.id.task_execute_async_btn)
    public void pushAsyncTask(View view) {
        Task.create(tag).executeOn(TaskSchedulers.ASYNC).execute(new Executor<TaskEntity>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onExecute(TaskEntity task) throws Exception {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    // --------------------------------------------------

    @OnClick(R.id.task_auto_btn)
    public void autoExecute(View view) {
        Task.create(tag).executeOn(TaskSchedulers.ASYNC_AUTO).execute(new Executor<TaskEntity>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onExecute(TaskEntity task) throws Exception {
                Thread.sleep(300);
            }

            @Override
            public void onError() {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @OnClick(R.id.task_multi_btn)
    public void multiExecute(View view) {
        Task.create(tag).executeOn(TaskSchedulers.ASYNC_MULTI).execute(new Executor<TaskEntity>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onExecute(TaskEntity task) throws Exception {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Task.clear();
    }
}
