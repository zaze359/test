package com.zaze.bean;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-16 - 11:11
 */
public class TabEntity {
    private int type;
    private String name;
    private Class clazz;

    public TabEntity() {
    }

    public TabEntity(int type, String name, Class clazz) {
        this.type = type;
        this.name = name;
        this.clazz = clazz;
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

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
