package com.zaze.component.table.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.zaze.R;
import com.zaze.aarrepo.commons.base.ZBaseFragment;
import com.zaze.aarrepo.commons.base.adapter.OnItemClickListener;
import com.zaze.component.table.TableAdapter;
import com.zaze.component.table.presenter.TablePresenter;
import com.zaze.component.table.presenter.impl.TablePresenterImpl;
import com.zaze.component.table.view.ToolView;
import com.zaze.model.entity.TableEntity;
import com.zz.library.util.helper.UltimateRecyclerViewHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-03 - 10:36
 */
public class TableFragment extends ZBaseFragment implements ToolView {
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
    protected int getLayoutId() {
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

        presenter = new TablePresenterImpl(this);
        presenter.getToolBox();
        return rootView;
    }

    @Override
    public void showAppList(List<TableEntity> list) {
        if (adapter == null) {
            adapter = new TableAdapter(getActivity(), list);
            adapter.setOnItemClickListener(new OnItemClickListener<TableEntity>() {
                @Override
                public void onItemClick(View view, TableEntity value, int position) {
                    startActivity(new Intent(getActivity(), value.getClazz()));
                }
            });
            linearLayoutManager = new GridLayoutManager(getActivity(), 2);
            tableRecyclerView.setLayoutManager(linearLayoutManager);
            UltimateRecyclerViewHelper.init(tableRecyclerView);
            tableRecyclerView.setAdapter(adapter);

        } else {
            adapter.setDataList(list);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
