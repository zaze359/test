package com.zaze.component.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zaze.R;
import com.zaze.aarrepo.commons.base.BaseFragment;
import com.zaze.aarrepo.utils.ViewUtil;
import com.zaze.component.ui.adapter.HomeAdapter;
import com.zz.library.commons.loading.LoadingStyle;
import com.zz.library.util.ThreadManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by zaze on 16/4/26.
 */
public class HomeFragment extends BaseFragment {
    private PtrClassicFrameLayout mPtrFrameLayout;
    private LoadMoreListViewContainer loadMoreListViewContainer;

    private ListView mListView;
    private HomeAdapter adapter;
    private List<String> listDate = new ArrayList<>();
    private int page = 0;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

//    @Override
//    protected int getTheme() {
//        return R.style.BlueTheme;
//    }

    @Override
    protected void init(View view) {
        mPtrFrameLayout = ViewUtil.findView(view, R.id.ptr_frame_layout);
        loadMoreListViewContainer = ViewUtil.findView(view, R.id.load_more_list_view_container);
        mListView = ViewUtil.findView(view, R.id.list_view);
        LoadingStyle.materialStyle(getActivity(), mPtrFrameLayout);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 0;
                listDate.clear();
                loadNextPage();
                mPtrFrameLayout.refreshComplete();
            }
        });

        loadMoreListViewContainer.useDefaultHeader();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                loadNextPage();
                ThreadManager.getInstance().runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean hasMore = true;
                        boolean isEmpty = false;
                        if (page > 2) {
                            hasMore = false;
                        }
                        loadMoreListViewContainer.loadMoreFinish(hasMore, hasMore);
                    }
                }, 500);
            }
        });
    }

    private void loadNextPage() {
        int pageSize = 15;
        for (int i = 0; i < pageSize; i++) {
            listDate.add(String.format(Locale.getDefault(), "测试数据%d", i));
        }
        loadListView(listDate);
        page++;
    }

    private void loadListView(List<String> list) {
        if (adapter == null) {
            adapter = new HomeAdapter(getActivity(), list);
            mListView.setAdapter(adapter);
        } else {
            adapter.setDataList(list);
            adapter.notifyDataSetChanged();
        }
    }
}
