package com.zaze.demo.component.animation.presenter.impl;

import com.zaze.demo.component.animation.presenter.AnimationPresenter;
import com.zaze.demo.component.animation.view.AnimationView;
import com.zaze.demo.model.entity.AnimationEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-25 - 09:45
 */
public class AnimationPresenterImpl implements AnimationPresenter {
    private AnimationView view;

    public AnimationPresenterImpl(AnimationView view) {
        this.view = view;
    }

    @Override
    public void getAnimationList() {
        List<AnimationEntity> list = new ArrayList<>();
        list.add(new AnimationEntity("View Property Animator", null));
        view.showAnimationList(list);

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
}
