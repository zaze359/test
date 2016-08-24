package com.zaze.component.table.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.zaze.R;
import com.zaze.model.entity.TableEntity;
import com.zaze.component.table.TableAdapter;
import com.zaze.component.table.presenter.TablePresenter;
import com.zaze.component.table.presenter.impl.TablePresenterImpl;
import com.zaze.component.table.view.ToolView;
import com.zz.library.commons.BaseFragment;
import com.zz.library.commons.adapter.ZUltimateAdapter;

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
    @Bind(R.id.table_recycler_view)
    UltimateRecyclerView tableRecyclerView;

    private TableAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private TablePresenter presenter;


    public static TableFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("TITLE", title);
        TableFragment fragment = new TableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_table;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        Bundle arg = getArguments();
        // API 21
//        Transition explode = TransitionInflater.from(getActivity()).inflateTransition(R.transition.explode);
//        getActivity().getWindow().setEnterTransition(explode);

        linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        tableRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TableAdapter(getActivity(), null);
        adapter.setOnItemClickListener(new ZUltimateAdapter.OnItemClickListener<TableEntity>() {
            @Override
            public void onItemClick(View view, TableEntity value, int position) {
                startActivity(new Intent(getActivity(), value.getClazz()));
            }
        });
        presenter = new TablePresenterImpl(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getToolBox();
    }

    @Override
    public void showAppList(List<TableEntity> list) {
        adapter.setDataList(list);
        tableRecyclerView.setAdapter(adapter);
    }
//    startActivity(new Intent(getActivity(), CertificateActivity.class));

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
