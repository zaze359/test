package com.zaze.demo.component.table.ui;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaze.common.base.BaseFragment;
import com.zaze.demo.R;
import com.zaze.demo.component.table.TableAdapter;
import com.zaze.demo.component.table.presenter.TablePresenter;
import com.zaze.demo.component.table.presenter.impl.TablePresenterImpl;
import com.zaze.demo.component.table.view.ToolView;
import com.zaze.demo.debug.DividerItemDecoration;
import com.zaze.demo.model.entity.TableEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-03 - 10:36
 */
public class TableFragment extends BaseFragment implements ToolView {
    @Bind(R.id.table_refresh_layout)
    SwipeRefreshLayout tableRefreshLayout;
    @Bind(R.id.table_recycler_view)
    RecyclerView tableRecyclerView;
    private TableAdapter adapter;
    private TablePresenter presenter;

    @Override
    protected boolean isNeedHead() {
        return false;
    }

    public static TableFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("TITLE", title);
        TableFragment fragment = new TableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_table;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        presenter = new TablePresenterImpl(this);
        tableRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
        tableRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                tableRefreshLayout.setRefreshing(true);
                presenter.refresh();
            }
        });
        return rootView;
    }

    @Override
    public void showAppList(List<TableEntity> list) {
        if (adapter == null) {
            adapter = new TableAdapter(getActivity(), list);
            tableRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            tableRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            tableRecyclerView.setAdapter(adapter);
        } else {
            adapter.setDataList(list);
        }
        tableRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
