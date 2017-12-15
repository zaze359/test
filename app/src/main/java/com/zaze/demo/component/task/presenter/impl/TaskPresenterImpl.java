package com.zaze.demo.component.task.presenter.impl;

import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.demo.component.task.presenter.TaskPresenter;
import com.zaze.demo.component.task.view.TaskView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-03-23 11:37 1.0
 */
public class TaskPresenterImpl extends BaseMvpPresenter<TaskView> implements TaskPresenter {

    public TaskPresenterImpl(TaskView view) {
        super(view);
    }

}
