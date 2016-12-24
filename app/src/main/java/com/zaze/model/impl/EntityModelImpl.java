package com.zaze.model.impl;


import com.zaze.aarrepo.utils.SortUtil;
import com.zaze.component.animation.ui.AnimationActivity;
import com.zaze.component.device.DeviceActivity;
import com.zaze.component.rxandroid.RxAndroidActivity;
import com.zaze.component.time.TimeActivity;
import com.zaze.component.toolbar.ToolBarDemoActivity;
import com.zaze.model.EntityModel;
import com.zaze.model.entity.AnimationEntity;
import com.zaze.model.entity.TableEntity;

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
        list.add(new TableEntity(1, "Animation", AnimationActivity.class));
        list.add(new TableEntity(2, "toolbar", ToolBarDemoActivity.class));
        list.add(new TableEntity(3, "RxAndroid", RxAndroidActivity.class));
        list.add(new TableEntity(4, "Device Info", DeviceActivity.class));
        list.add(new TableEntity(5, "Time", TimeActivity.class));

        SortUtil.sortList(list, "getType", "desc");

        return list;
    }

    @Override
    public List<AnimationEntity> getAnimationList() {
        List<AnimationEntity> list = new ArrayList<>();
        list.add(new AnimationEntity("View Property Animator", null));
        return list;
    }
}
