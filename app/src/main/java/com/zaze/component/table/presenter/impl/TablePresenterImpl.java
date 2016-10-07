package com.zaze.component.table.presenter.impl;


import com.zaze.component.table.presenter.TablePresenter;
import com.zaze.component.table.view.ToolView;
import com.zaze.model.ModelFactory;
import com.zaze.model.EntityModel;

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
        entityModel = ModelFactory.getTabModel();
    }

    @Override
    public void getToolBox() {
        view.showAppList(entityModel.getTableList());
    }
}
