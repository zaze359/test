package com.zaze.demo.component.animation.presenter;

import com.zaze.common.base.BasePresenter;
import com.zaze.demo.component.animation.view.AnimationView;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-25 - 09:44
 */
public interface AnimationPresenter extends BasePresenter<AnimationView> {

    /**
     * animation列表
     */
    void getAnimationList();
}
