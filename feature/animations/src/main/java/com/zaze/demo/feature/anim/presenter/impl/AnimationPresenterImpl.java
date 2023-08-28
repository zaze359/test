package com.zaze.demo.feature.anim.presenter.impl;

import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.demo.feature.anim.AnimationEntity;
import com.zaze.demo.feature.anim.presenter.AnimationPresenter;
import com.zaze.demo.feature.anim.ui.SharedElementActivity;
import com.zaze.demo.feature.anim.ui.TransitionActivity;
import com.zaze.demo.feature.anim.view.AnimationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-25 - 09:45
 */
public class AnimationPresenterImpl extends BaseMvpPresenter<AnimationView> implements AnimationPresenter {

    @Override
    public void getAnimationList() {
        List<AnimationEntity> list = new ArrayList<>();
//        list.add(buildAnimationEntity("Object Animator", ObjectA))
        list.add(buildAnimationEntity("TransitionActivity", TransitionActivity.class, AnimationEntity.Type.SCENE_TRANSITION));
        list.add(buildAnimationEntity("SharedElementActivity", SharedElementActivity.class, AnimationEntity.Type.SHARED_ELEMENT));
//        list.add(buildAnimationEntity("Object Animator", SharedElementActivity.class, AnimationEntity.Type.SCENE_TRANSITION));
        getView().showAnimationList(list);
    }

    private AnimationEntity buildAnimationEntity(String name, Class clazz, int type) {
        return new AnimationEntity(name, clazz, type);
    }
}
