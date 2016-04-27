package com.zaze.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zaze.R;
import com.zaze.commons.BaseFragment;
import com.zaze.commons.adapter.ZAdapter;
import com.zaze.commons.loading.LoadingStyle;
import com.zaze.util.ThreadManager;
import com.zaze.util.ViewUtil;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
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
                        if(page > 2) {
                            hasMore = false;
                        }
                        loadMoreListViewContainer.loadMoreFinish(hasMore, hasMore);
                    }
                }, 500);
            }
        });
        return view;
    }

    private void loadNextPage() {
        int pageSize = 15;
        for (int i = 0; i < pageSize; i++) {
            listDate.add(String.format(Locale.getDefault(), "测试数据%d", pageSize * page + i));
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

    private class HomeAdapter extends ZAdapter<String, HomeAdapter.StringItemHolder> {

        public HomeAdapter(Context context, List<String> data) {
            super(context, data);
        }

        @Override
        public void setViewData(String value, StringItemHolder itemHolder, int position, View convertView, ViewGroup parent) {
            itemHolder.textView.setText(value);
        }

        @Override
        public int getViewLayoutId() {
            return R.layout.item_layout_home;
        }

        @Override
        public StringItemHolder createViewHolder(View convertView) {
            return new StringItemHolder(convertView);
        }

        public class StringItemHolder {
            public TextView textView;

            public StringItemHolder(View view) {
                textView = ViewUtil.findView(view, R.id.desc_tv);
            }
        }
    }
}
