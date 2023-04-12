package com.zaze.demo.component.animation.presenter.impl;

import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.core.model.data.AnimationEntity;
import com.zaze.demo.component.animation.presenter.AnimationPresenter;
import com.zaze.demo.component.animation.view.AnimationView;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-25 - 09:45
 */
public class AnimationPresenterImpl extends BaseMvpPresenter<AnimationView> implements AnimationPresenter {

    @Override
    public void getAnimationList() {
//        getView().showAnimationList(ModelFactory.getEntityModel().getAnimationList());
//        <string -array name="animation_titles">
//        <item></item>
//        <item>Object Animator</item>
//        <!--<item>Interpolators</item>-->
//        <!--<item>Circular Reveal</item>-->
//        <!--<item>Morph Transitions</item>-->
//        <!--<item>Shared Transitions</item>-->
//        <!--<item>Window Content Transitions</item>-->
//        <!--<item>Shared Element Transitions</item>-->
//        <!--<item>Animated Vector Drawables</item>-->
//        </string-array>
    }

    private AnimationEntity buildAnimationEntity(String name, Class clazz, int type) {
        return new AnimationEntity(name, clazz, type);
    }
}
