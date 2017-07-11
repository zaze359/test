package com.zaze.demo.component.fileexplorer.ui;


import android.os.Bundle;

import com.zaze.aarrepo.commons.base.ZBaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.fileexplorer.presenter.FileExplorerPresenter;
import com.zaze.demo.component.fileexplorer.presenter.impl.FileExplorerPresenterImpl;
import com.zaze.demo.component.fileexplorer.view.FileExplorerView;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-05-05 04:42 1.0
 */
public class FileExplorerActivity extends ZBaseActivity implements FileExplorerView {
    private FileExplorerPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);
        presenter = new FileExplorerPresenterImpl(this);
//        getHeadWidget().setIcon()
    }

}
