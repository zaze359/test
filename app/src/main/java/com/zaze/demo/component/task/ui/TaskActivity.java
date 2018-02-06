package com.zaze.demo.component.task.ui;


import android.os.Bundle;
import android.view.View;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.task.presenter.TaskPresenter;
import com.zaze.demo.component.task.presenter.impl.TaskPresenterImpl;
import com.zaze.demo.component.task.view.TaskView;
import com.zaze.utils.ThreadManager;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;
import com.zaze.utils.task.Executor;
import com.zaze.utils.task.Task;
import com.zaze.utils.task.TaskEntity;

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
    private Task testTask = Task.create(tag);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        presenter = new TaskPresenterImpl(this);
        Task.setNeedLog(true);
    }

    @OnClick(R.id.task_execute_test_btn)
    public void pushSyncTask(View view) {
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

        testTask.push(new TaskEntity(String.valueOf(taskId))).executeOnAsyncAuto().execute(new Executor<TaskEntity>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onExecute(TaskEntity task) throws Exception {
                Thread.sleep(500);
                ZLog.i(ZTag.TAG_DEBUG, "==================================================");
                ZLog.i(ZTag.TAG_DEBUG, "taskId : " + taskId);
                ZLog.i(ZTag.TAG_DEBUG, "this.hashCode : " + this.hashCode());
                ZLog.i(ZTag.TAG_DEBUG, "task.getTaskId : " + task.getTaskId());
                ZLog.i(ZTag.TAG_DEBUG, "==================================================");
            }

            @Override
            public void onError() {

            }

            @Override
            public void onComplete() {

            }
        });
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
                testTask.push(list);
            }
        });
    }

    @OnClick(R.id.task_execute_async_btn)
    public void pushAsyncTask(View view) {
        testTask.executeOnAsync().execute(new Executor<TaskEntity>() {
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
        testTask.executeOnAsyncAuto().execute(new Executor<TaskEntity>() {
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
        testTask.executeOnAsyncMulti().execute(new Executor<TaskEntity>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onExecute(TaskEntity task) throws Exception {
                Thread.sleep(500);
            }

            @Override
            public void onError() {

            }

            @Override
            public void onComplete() {
                testTask.notifyExecute();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Task.clear();
    }
}
