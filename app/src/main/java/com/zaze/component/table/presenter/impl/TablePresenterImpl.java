package com.zaze.component.table.presenter.impl;


import com.zaze.component.table.presenter.TablePresenter;
import com.zaze.component.table.view.ToolView;
import com.zaze.model.ModelFactory;
import com.zaze.model.TableModel;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-16 - 11:25
 */
public class TablePresenterImpl implements TablePresenter {
    private ToolView view;
    private TableModel tableModel;

    public TablePresenterImpl(ToolView view) {
        this.view = view;
        tableModel = ModelFactory.getTabModel();
    }

    @Override
    public void getToolBox() {
        view.showAppList(tableModel.getTableList());
    }
}
