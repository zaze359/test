package com.zaze.demo.component.xbus.ui;


import android.os.Bundle;

import com.zaze.common.base.ZBaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.xbus.presenter.EventBus2Presenter;
import com.zaze.demo.component.xbus.presenter.impl.EventBus2PresenterImpl;
import com.zaze.demo.component.xbus.view.EventBus2View;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-02-28 05:30 1.0
 */
public class EventBus2Activity extends ZBaseActivity implements EventBus2View {
    private EventBus2Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus2);
        presenter = new EventBus2PresenterImpl(this);
        presenter.sendMessage();
        this.finish();
    }

}
