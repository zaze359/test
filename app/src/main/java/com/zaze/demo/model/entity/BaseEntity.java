package com.zaze.demo.model.entity;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-25 - 09:53
 */
public class BaseEntity extends BaseObservable implements Serializable {
    private String name;
    private Class targetClass;
    private int type;

    public BaseEntity() {
    }

    public BaseEntity(String name, Class targetClass, int type) {
        this.name = name;
        this.targetClass = targetClass;
        this.type = type;
    }

    @Bindable
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Bindable
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


}
