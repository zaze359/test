package com.zaze.demo.data.entity;



/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-16 - 11:11
 */
public class TableEntity {
    private String name;
    private Class targetClass;
    private String route;
    private int type;

    public TableEntity(String name, Class targetClass, int type) {
        this.name = name;
        this.targetClass = targetClass;
        this.type = type;
    }

    public TableEntity(String name, String route, int type) {
        this.name = name;
        this.route = route;
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

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
