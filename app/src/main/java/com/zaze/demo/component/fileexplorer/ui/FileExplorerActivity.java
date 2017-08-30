package com.zaze.demo.component.fileexplorer.ui;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.zaze.common.adapter.third.UltimateRecyclerViewHelper;
import com.zaze.common.base.ZBaseActivity;
import com.zaze.common.widget.head.ZOrientation;
import com.zaze.demo.R;
import com.zaze.demo.component.fileexplorer.FileEvent;
import com.zaze.demo.component.fileexplorer.adapter.FileAdapter;
import com.zaze.demo.component.fileexplorer.adapter.FileEntity;
import com.zaze.demo.component.fileexplorer.presenter.FileExplorerPresenter;
import com.zaze.demo.component.fileexplorer.presenter.impl.FileExplorerPresenterImpl;
import com.zaze.demo.component.fileexplorer.view.FileExplorerView;
import com.zaze.utils.ZOnClickHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-05-05 04:42 1.0
 */
public class FileExplorerActivity extends ZBaseActivity implements FileExplorerView {

    @Bind(R.id.file_explorer_recycler)
    UltimateRecyclerView fileExplorerRecycler;
    @Bind(R.id.file_explorer_return_tv)
    TextView fileExplorerReturnTv;
    private FileExplorerPresenter presenter;

    private FileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getHeadWidget()
                .setText("File Explorer", ZOrientation.CENTER)
                .setBackClickListener(this);
        presenter = new FileExplorerPresenterImpl(this);
        presenter.loadFileList();
        ZOnClickHelper.setOnClickListener(fileExplorerReturnTv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.backToParent();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public void showFileList(@NotNull List<FileEntity> fileList) {
        if (adapter == null) {
            adapter = new FileAdapter(this, fileList);
            UltimateRecyclerViewHelper.init(fileExplorerRecycler);
            fileExplorerRecycler.setLayoutManager(new LinearLayoutManager(this));
            fileExplorerRecycler.setAdapter(adapter);
        } else {
            adapter.setDataList(fileList);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFileEvent(FileEvent fileEvent) {
        presenter.openFileOrDir(fileEvent);
    }
}
