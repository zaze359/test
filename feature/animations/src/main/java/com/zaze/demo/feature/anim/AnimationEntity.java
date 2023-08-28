package com.zaze.demo.feature.anim;


import java.io.Serializable;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-25 - 09:50
 */
public class AnimationEntity implements Serializable {
    private String name;
    private Class targetClass;
    private int type;

    public AnimationEntity(){}

    public AnimationEntity(String name, Class targetClass, int type) {
        this.name = name;
        this.targetClass = targetClass;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class Type {
        public static final int SCENE_TRANSITION = 0;
        public static final int SCALE_UP = 1;
        public static final int SHARED_ELEMENT = 2;
    }
}
