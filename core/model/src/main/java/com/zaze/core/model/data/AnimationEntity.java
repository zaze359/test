package com.zaze.core.model.data;



/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-25 - 09:50
 */
public class AnimationEntity extends BaseEntity {
    public AnimationEntity() {
    }

    public AnimationEntity(String name, Class targetClass, int type) {
        super(name, targetClass, type);
    }


    public static class Type {
        public static final int SCENE_TRANSITION = 0;
        public static final int SCALE_UP = 1;
        public static final int SHARED_ELEMENT = 2;
    }
}
