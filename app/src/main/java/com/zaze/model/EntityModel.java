package com.zaze.model;


import com.zaze.model.entity.AnimationEntity;
import com.zaze.model.entity.TableEntity;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-16 - 11:20
 */
public interface EntityModel {

    List<TableEntity> getTableList();

    List<AnimationEntity> getAnimationList();

}
