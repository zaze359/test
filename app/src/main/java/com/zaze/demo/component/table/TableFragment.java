package com.zaze.demo.component.table;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zaze.common.base.AbsFragment;
import com.zaze.common.base.ext.ViewModelFactoryKt;
import com.zaze.demo.R;
import com.zaze.demo.debug.DividerItemDecoration;
import com.zaze.demo.model.entity.TableEntity;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-03 - 10:36
 */
public class TableFragment extends AbsFragment {

    private SwipeRefreshLayout tableRefreshLayout;
    private RecyclerView tableRecyclerView;
    private TableAdapter adapter;
    private TableViewModel tableViewModel;

    public static TableFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("TITLE", title);
        TableFragment fragment = new TableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.table_fragment, container, false);
        tableRefreshLayout = rootView.findViewById(R.id.table_refresh_layout);
        tableRecyclerView = rootView.findViewById(R.id.table_recycler_view);

        tableViewModel = ViewModelFactoryKt.obtainViewModel(this, TableViewModel.class);
        tableViewModel.getData().observe(this, new Observer<List<TableEntity>>() {
            @Override
            public void onChanged(List<TableEntity> tableEntities) {
                showAppList(tableEntities);
            }
        });

        tableRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tableViewModel.refresh();
            }
        });
        tableRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                tableRefreshLayout.setRefreshing(true);
                tableViewModel.refresh();
            }
        });
        return rootView;
    }

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
}
