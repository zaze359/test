package com.zaze.model;


import com.zaze.bean.TabEntity;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-16 - 11:20
 */
public interface TabModel {

    TabEntity getTab();

    List<TabEntity> getTabList();

}
