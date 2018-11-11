package com.zaze.demo.component.readpackage.view;


import com.zaze.common.base.BaseView;
import com.zaze.demo.debug.AppShortcut;

import java.util.List;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-04-17 05:15 1.0
 */
public interface ReadPackageView extends BaseView {


    void showPackageList(List<AppShortcut> list);

}
