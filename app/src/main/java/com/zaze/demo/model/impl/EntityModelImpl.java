package com.zaze.demo.model.impl;


import com.zaze.demo.component.device.ui.DeviceActivity;
import com.zaze.demo.component.gps.ui.GpsActivity;
import com.zaze.demo.component.preference.MyPreferenceActivity;
import com.zaze.demo.component.provider.ui.ProviderActivity;
import com.zaze.demo.component.readpackage.ui.ReadPackageActivity;
import com.zaze.demo.component.rxandroid.RxAndroidActivity;
import com.zaze.demo.component.task.ui.TaskActivity;
import com.zaze.demo.component.time.TimeActivity;
import com.zaze.demo.component.toolbar.ToolBarDemoActivity;
import com.zaze.demo.component.xbus.ui.EventBusActivity;
import com.zaze.demo.model.EntityModel;
import com.zaze.demo.model.entity.AnimationEntity;
import com.zaze.demo.model.entity.TableEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-16 - 11:20
 */
public class EntityModelImpl implements EntityModel {

    @Override
    public List<TableEntity> getTableList() {
        List<TableEntity> list = new ArrayList<>();
//        list.add(new TableEntity(1, "Animation", AnimationActivity.class));
        list.add(new TableEntity(2, "toolbar", ToolBarDemoActivity.class));
        list.add(new TableEntity(3, "RxAndroid", RxAndroidActivity.class));
        list.add(new TableEntity(4, "Device Info", DeviceActivity.class));
        list.add(new TableEntity(5, "Time", TimeActivity.class));
        list.add(new TableEntity(6, "GPS", GpsActivity.class));
        list.add(new TableEntity(7, "EventBus", EventBusActivity.class));
        list.add(new TableEntity(8, "Task", TaskActivity.class));
        list.add(new TableEntity(9, "ContentProvider", ProviderActivity.class));
        list.add(new TableEntity(10, "ReadPackage", ReadPackageActivity.class));
        list.add(new TableEntity(11, "MyPreferenceActivity", MyPreferenceActivity.class));

//        SortUtil.sortList(list, "getType", "desc");

        return list;
    }

    @Override
    public List<AnimationEntity> getAnimationList() {
        List<AnimationEntity> list = new ArrayList<>();
        list.add(new AnimationEntity("View Property Animator", null));
        return list;
    }
}
