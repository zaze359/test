package com.zaze.demo.component.animation.view;

import com.zaze.common.base.BaseView;
import com.zaze.core.model.data.AnimationEntity;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-24 - 14:57
 */
public interface AnimationView extends BaseView {
    /**
     * 显示列表
     *
     * @param list list
     */
    void showAnimationList(List<AnimationEntity> list);
}
