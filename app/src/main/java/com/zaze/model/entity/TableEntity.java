package com.zaze.model.entity;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-16 - 11:11
 */
public class TableEntity extends BaseEntity {
    private int type;

    public TableEntity(int type, String name, Class clazz) {
        super(name, clazz);
        this.type = type;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
