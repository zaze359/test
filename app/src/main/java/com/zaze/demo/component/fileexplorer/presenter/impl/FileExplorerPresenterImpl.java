package com.zaze.demo.component.fileexplorer.presenter.impl;

import com.zaze.demo.component.fileexplorer.presenter.FileExplorerPresenter;
import com.zaze.aarrepo.commons.base.ZBasePresenter;
import com.zaze.demo.component.fileexplorer.view.FileExplorerView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-05-05 04:42 1.0
 */
public class FileExplorerPresenterImpl extends ZBasePresenter<FileExplorerView> implements FileExplorerPresenter {

    public FileExplorerPresenterImpl(FileExplorerView view) {
        super(view);
    }

}
