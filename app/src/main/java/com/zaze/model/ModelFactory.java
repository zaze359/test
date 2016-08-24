package com.zaze.model;

import com.zaze.model.impl.TableModelImpl;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-17 - 09:02
 */
public class ModelFactory {
    public static TableModel getTabModel() {
        return new TableModelImpl();
    }
}
