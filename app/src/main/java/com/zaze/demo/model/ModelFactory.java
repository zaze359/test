package com.zaze.demo.model;

import com.zaze.demo.model.impl.DeviceModelImpl;
import com.zaze.demo.model.impl.EntityModelImpl;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-17 - 09:02
 */
public class ModelFactory {
    public static EntityModel getEntityModel() {
        return new EntityModelImpl();
    }

    public static DeviceModel getDeviceModel() {
        return new DeviceModelImpl();
    }
}
