package com.zaze.demo.component.table.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.zaze.aarrepo.commons.base.ZBaseFragment;
import com.zaze.aarrepo.commons.base.adapter.OnItemClickListener;
import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.utils.ZTag;
import com.zaze.aarrepo.utils.helper.UltimateRecyclerViewHelper;
import com.zaze.demo.R;
import com.zaze.demo.component.table.TableAdapter;
import com.zaze.demo.component.table.presenter.TablePresenter;
import com.zaze.demo.component.table.presenter.impl.TablePresenterImpl;
import com.zaze.demo.component.table.view.ToolView;
import com.zaze.demo.model.entity.TableEntity;

import java.util.ArrayList;
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
//            linearLayoutManager = new GridLayoutManager(getActivity(), 2);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            tableRecyclerView.setLayoutManager(linearLayoutManager);
            UltimateRecyclerViewHelper.init(tableRecyclerView).initLoadMore(new UltimateRecyclerView.OnLoadMoreListener() {
                @Override
                public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                    ZLog.i(ZTag.TAG_DEBUG, "loadMore : %s / %s", itemsCount, maxLastVisiblePosition);
                    adapter.loadMore(new ArrayList<TableEntity>());
                }
            }).initRefresh(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    presenter.getToolBox();
                }
            });
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
