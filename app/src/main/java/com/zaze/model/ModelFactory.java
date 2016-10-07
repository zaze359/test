package com.zaze.model;

import com.zaze.model.impl.EntityModelImpl;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-17 - 09:02
 */
public class ModelFactory {
    public static EntityModel getTabModel() {
        return new EntityModelImpl();
    }
}
