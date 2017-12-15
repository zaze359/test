package com.zaze.demo.component.notification.presenter.impl;

import com.zaze.demo.component.notification.presenter.NotificationPresenter;
import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.demo.component.notification.view.NotificationView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-08 03:56 1.0
 */
public class NotificationPresenterImpl extends BaseMvpPresenter<NotificationView> implements NotificationPresenter {

    public NotificationPresenterImpl(NotificationView view) {
        super(view);
    }

}
