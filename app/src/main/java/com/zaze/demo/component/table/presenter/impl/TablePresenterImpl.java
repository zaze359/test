package com.zaze.demo.component.table.presenter.impl;


import com.zaze.demo.component.table.presenter.TablePresenter;
import com.zaze.demo.component.table.view.ToolView;
import com.zaze.demo.model.ModelFactory;
import com.zaze.demo.model.EntityModel;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-16 - 11:25
 */
public class TablePresenterImpl implements TablePresenter {
    private ToolView view;
    private EntityModel entityModel;

    public TablePresenterImpl(ToolView view) {
        this.view = view;
        entityModel = ModelFactory.getEntityModel();
    }

    @Override
    public void getToolBox() {
        view.showAppList(entityModel.getTableList());
    }
}
