package com.zaze.model.impl;


import com.zaze.model.entity.AnimationEntity;
import com.zaze.model.entity.TableEntity;
import com.zaze.component.animation.ui.AnimationActivity;
import com.zaze.model.EntityModel;

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
        list.add(new TableEntity(2, "b", AnimationActivity.class));
        return list;
    }

    @Override
    public List<AnimationEntity> getAnimationList() {
        List<AnimationEntity> list = new ArrayList<>();
        list.add(new AnimationEntity("View Property Animator", null));
        return list;
    }
}
