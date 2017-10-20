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

    public BaseEntity() {
    }

    public BaseEntity(String name, Class targetClass) {
        this.name = name;
        this.targetClass = targetClass;
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
