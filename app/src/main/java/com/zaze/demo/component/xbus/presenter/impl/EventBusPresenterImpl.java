package com.zaze.demo.component.xbus.presenter.impl;

import com.zaze.demo.component.xbus.presenter.EventBusPresenter;
import com.zaze.aarrepo.commons.base.ZBasePresenter;
import com.zaze.demo.component.xbus.view.EventBusView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-28 04:41 1.0
 */
public class EventBusPresenterImpl extends ZBasePresenter<EventBusView> implements EventBusPresenter {

    public EventBusPresenterImpl(EventBusView view) {
        super(view);
    }

}
