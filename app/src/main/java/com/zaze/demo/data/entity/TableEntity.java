package com.zaze.demo.data.entity;


import com.zaze.core.model.data.BaseEntity;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-16 - 11:11
 */
public class TableEntity extends BaseEntity {
    public TableEntity(String name, Class targetClass, int type) {
        super(name, targetClass, type);
    }
}
