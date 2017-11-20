package com.zaze.demo.model.entity;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-25 - 09:53
 */
public class BaseEntity {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
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
}
