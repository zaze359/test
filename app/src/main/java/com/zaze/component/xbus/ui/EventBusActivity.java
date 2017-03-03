package com.zaze.component.xbus.ui;


import android.os.Bundle;

import com.zaze.R;
import com.zaze.aarrepo.commons.base.BaseActivity;
import com.zaze.aarrepo.commons.log.LogKit;
import com.zaze.aarrepo.utils.ActivityUtil;
import com.zaze.component.xbus.event.EventMessage;
import com.zaze.component.xbus.presenter.EventBusPresenter;
import com.zaze.component.xbus.presenter.impl.EventBusPresenterImpl;
import com.zaze.component.xbus.view.EventBusView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-28 04:41 1.0
 */
public class EventBusActivity extends BaseActivity implements EventBusView {
    private EventBusPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);
        EventBus.getDefault().register(this);
        presenter = new EventBusPresenterImpl(this);
        ActivityUtil.startActivity(this, EventBus2Activity.class);
    }

    @Subscribe
    public void getEventMessage(EventMessage message) {
        LogKit.v("getEventMessage : " + message.toString());
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
