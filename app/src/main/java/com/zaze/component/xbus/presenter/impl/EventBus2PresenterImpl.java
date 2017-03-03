package com.zaze.component.xbus.presenter.impl;

import com.zaze.component.xbus.event.EventMessage;
import com.zaze.component.xbus.presenter.EventBus2Presenter;
import com.zaze.aarrepo.commons.base.BasePresenter;
import com.zaze.component.xbus.view.EventBus2View;

import org.greenrobot.eventbus.EventBus;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-28 05:30 1.0
 */
public class EventBus2PresenterImpl extends BasePresenter<EventBus2View> implements EventBus2Presenter {

    public EventBus2PresenterImpl(EventBus2View view) {
        super(view);
    }

    @Override
    public void sendMessage() {
        EventBus.getDefault().post(new EventMessage("aaaaa"));
    }
}