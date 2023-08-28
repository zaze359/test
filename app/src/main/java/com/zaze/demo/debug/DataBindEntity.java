package com.zaze.demo.debug;



import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-25 - 09:53
 */
public class DataBindEntity extends BaseObservable implements Serializable {
    private String name;
    private int type;

    @Bindable
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
