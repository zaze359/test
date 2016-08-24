package com.zaze.model.impl;


import com.zaze.model.entity.TableEntity;
import com.zaze.component.animation.AnimationActivity;
import com.zaze.model.TableModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-16 - 11:20
 */
public class TableModelImpl implements TableModel {
    @Override
    public TableEntity getTableEntity() {
        return null;
    }

    @Override
    public List<TableEntity> getTableList() {
        List<TableEntity> list = new ArrayList<>();
        list.add(new TableEntity(1, "Animation", AnimationActivity.class));
        list.add(new TableEntity(2, "b", AnimationActivity.class));
        return list;
    }
}
