package com.zaze.demo.component.animation.view;

import com.zaze.demo.model.entity.AnimationEntity;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-24 - 14:57
 */
public interface AnimationView {
    /**
     * 显示列表
     *
     * @param list list
     */
    void showAnimationList(List<AnimationEntity> list);
}
