package com.zaze.demo.component.table.presenter.impl;


import com.zaze.demo.component.table.presenter.TablePresenter;
import com.zaze.demo.component.table.view.ToolView;
import com.zaze.demo.model.EntityModel;
import com.zaze.demo.model.ModelFactory;
import com.zaze.demo.model.entity.TableEntity;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    public void refresh() {
        Observable.fromCallable(new Callable<List<TableEntity>>() {
            @Override
            public List<TableEntity> call() throws Exception {
                return entityModel.getTableList();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TableEntity>>() {
                    @Override
                    public void accept(List<TableEntity> tableEntities) throws Exception {
                        view.showAppList(tableEntities);
                    }
                });

    }
}
