package com.zaze.model.entity;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-25 - 09:53
 */
public class BaseEntity {
    private String name;
    private Class clazz;

    public BaseEntity(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
