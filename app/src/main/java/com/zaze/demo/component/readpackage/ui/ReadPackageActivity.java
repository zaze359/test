package com.zaze.demo.component.readpackage.ui;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.zaze.aarrepo.commons.base.ZBaseActivity;
import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.utils.FileUtil;
import com.zaze.aarrepo.utils.helper.OnClickHelper;
import com.zaze.aarrepo.utils.helper.UltimateRecyclerViewHelper;
import com.zaze.demo.R;
import com.zaze.demo.component.readpackage.adapter.ReadPackageAdapter;
import com.zaze.demo.component.readpackage.presenter.ReadPackagePresenter;
import com.zaze.demo.component.readpackage.presenter.impl.ReadPackagePresenterImpl;
import com.zaze.demo.component.readpackage.view.ReadPackageView;
import com.zaze.demo.model.entity.PackageEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-04-17 05:15 1.0
 */
public class ReadPackageActivity extends ZBaseActivity implements ReadPackageView {
    @Bind(R.id.package_recycle_view)
    UltimateRecyclerView packageRecycleView;
    @Bind(R.id.package_extract_btn)
    Button packageExtractBtn;
    private ReadPackagePresenter presenter;
    private ReadPackageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_package);
        ButterKnife.bind(this);
        presenter = new ReadPackagePresenterImpl(this);
//        presenter.getAllApkFile("/sdcard/");
        presenter.getAllSystemApp();
        OnClickHelper.setOnClickListener(packageExtractBtn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keepApp = adapter.getPackageListStr();
                ZLog.i("package", keepApp);
                FileUtil.write2SDCardFile("/zaze/keepApp.txt", keepApp);
            }
        });
    }

    @Override
    public void showPackageList(List<PackageEntity> list) {
        if (adapter == null) {
            adapter = new ReadPackageAdapter(this, list);
            packageRecycleView.setLayoutManager(new LinearLayoutManager(this));
            UltimateRecyclerViewHelper.init(packageRecycleView);
            packageRecycleView.setAdapter(adapter);
        } else {
            adapter.setDataList(list);
        }

    }
}
