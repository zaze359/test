package com.zaze.demo.component.fileexplorer.ui;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.zaze.common.base.BaseActivity;
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

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static com.zaze.demo.util.AppCompatActivityExtKt.setupActionBar;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-05-05 04:42 1.0
 */
public class FileExplorerActivity extends BaseActivity implements FileExplorerView {

    RecyclerView fileExplorerRecycler;
    TextView fileExplorerReturnTv;
    private FileExplorerPresenter presenter;
    private FileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_explorer_activity);
        fileExplorerRecycler = findViewById(R.id.file_explorer_recycler);
        fileExplorerReturnTv = findViewById(R.id.file_explorer_return_tv);


        EventBus.getDefault().register(this);
        setupActionBar(this, R.id.file_explorer_toolbar, new Function1<ActionBar, Unit>() {
            @Override
            public Unit invoke(ActionBar actionBar) {
                actionBar.setTitle("文件管理");
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
                return null;
            }
        });
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public void showCurPath(String path) {
        fileExplorerReturnTv.setText(path);
    }

    @Override
    public void showFileList(@NotNull List<FileEntity> fileList) {
        if (adapter == null) {
            adapter = new FileAdapter(this, fileList);
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
