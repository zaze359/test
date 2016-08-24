package com.zaze.model;


import com.zaze.model.entity.TableEntity;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-16 - 11:20
 */
public interface TableModel {

    TableEntity getTableEntity();

    List<TableEntity> getTableList();

}
