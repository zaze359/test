package com.zaze.model.impl;


import com.zaze.bean.TabEntity;
import com.zaze.component.ui.TestActivity;
import com.zaze.model.TabModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-16 - 11:20
 */
public class TabModelImpl implements TabModel {
    @Override
    public TabEntity getTab() {
        return null;
    }

    @Override
    public List<TabEntity> getTabList() {
        List<TabEntity> list = new ArrayList<>();
        list.add(new TabEntity(1, "a", TestActivity.class));
        list.add(new TabEntity(2, "b", TestActivity.class));
        return list;
    }
}
