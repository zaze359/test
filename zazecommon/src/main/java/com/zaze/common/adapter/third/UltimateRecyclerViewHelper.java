package com.zaze.common.adapter.third;


import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.zaze.common.R;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-09-20 - 23:17
 */
public class UltimateRecyclerViewHelper {
    private UltimateRecyclerView recyclerView;

    public static UltimateRecyclerViewHelper init(UltimateRecyclerView recyclerView) {
        return new UltimateRecyclerViewHelper(recyclerView);
    }

    private UltimateRecyclerViewHelper(UltimateRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setEmptyView(R.layout.layout_empty_view, UltimateRecyclerView.EMPTY_KEEP_HEADER_AND_LOARMORE);
    }

    /**
     * 加载更多
     *
     * @param onLoadMoreListener
     */
    public UltimateRecyclerViewHelper initLoadMore(final UltimateRecyclerView.OnLoadMoreListener onLoadMoreListener) {
        init(recyclerView);
        recyclerView.setLoadMoreView(R.layout.layout_load_more);
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(final int itemsCount, final int maxLastVisiblePosition) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.loadMore(itemsCount, maxLastVisiblePosition);
                        }
                    }
                }, 500);
            }
        });
        return this;
    }

    /**
     * 刷新
     */
    public UltimateRecyclerViewHelper initRefresh(final SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        init(recyclerView);
//        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
//        recyclerView.addItemDecoration(headersDecor);
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRefreshListener.onRefresh();
                    }
                }, 400);
            }
        });
        return this;
////        ultimateRecyclerView.setEmptyView(getResources().getIdentifier("empty_view","layout",getPackageName()));
//        ultimateRecyclerView.showEmptyView();
//        ultimateRecyclerView.setParallaxHeader(getLayoutInflater().inflate(R.layout.parallax_recyclerview_header, ultimateRecyclerView.mRecyclerView, false));
    }
}
