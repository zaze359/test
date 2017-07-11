package com.zaze.demo.component.readpackage.view;

import com.zaze.aarrepo.commons.base.ZBaseView;
import com.zaze.demo.model.entity.PackageEntity;

import java.util.List;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-04-17 05:15 1.0
 */
public interface ReadPackageView extends ZBaseView {


    void showPackageList(List<PackageEntity> list);

}
