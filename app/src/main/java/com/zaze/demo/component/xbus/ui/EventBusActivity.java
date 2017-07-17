package com.zaze.demo.component.xbus.ui;


import android.os.Bundle;

import com.zaze.aarrepo.commons.base.ZBaseActivity;
import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.utils.ActivityUtil;
import com.zaze.aarrepo.utils.ZTag;
import com.zaze.demo.R;
import com.zaze.demo.component.xbus.event.EventMessage;
import com.zaze.demo.component.xbus.presenter.EventBusPresenter;
import com.zaze.demo.component.xbus.presenter.impl.EventBusPresenterImpl;
import com.zaze.demo.component.xbus.view.EventBusView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-28 04:41 1.0
 */
public class EventBusActivity extends ZBaseActivity implements EventBusView {
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
        ZLog.v(ZTag.TAG_DEBUG, "getEventMessage : " + message.toString());
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
