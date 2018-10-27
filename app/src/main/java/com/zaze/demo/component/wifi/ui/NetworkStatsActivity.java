package com.zaze.demo.component.wifi.ui;

import android.os.Bundle;

import com.zaze.common.base.mvp.BaseMvpActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.wifi.adapter.NetworkStatsAdapter;
import com.zaze.demo.component.wifi.presenter.NetworkStatsPresenter;
import com.zaze.demo.component.wifi.presenter.impl.NetworkStatsPresenterImpl;
import com.zaze.demo.component.wifi.view.NetworkStatsView;
import com.zaze.demo.debug.NetTrafficStats;

import java.util.Collection;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-22 04:36 1.0
 */
public class NetworkStatsActivity extends BaseMvpActivity<NetworkStatsView, NetworkStatsPresenter> implements NetworkStatsView {

    private RecyclerView networkStatsRecycler;
    private SwipeRefreshLayout networkStatsRefreshLayout;
    private NetworkStatsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_stats_activity);
        networkStatsRecycler = findViewById(R.id.network_stats_recycler);
        networkStatsRefreshLayout = findViewById(R.id.network_stats_refresh_layout);
        networkStatsRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });
        networkStatsRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                networkStatsRefreshLayout.setRefreshing(true);
                presenter.onRefresh();
            }
        });
    }

    @Override
    protected NetworkStatsPresenter getPresenter() {
        return new NetworkStatsPresenterImpl(this);
    }

    @Override
    public void showNetworkStats(Collection<NetTrafficStats> netTrafficStats) {
        if (adapter == null) {
            adapter = new NetworkStatsAdapter(this, netTrafficStats, presenter);
            networkStatsRecycler.setLayoutManager(new LinearLayoutManager(this));
            networkStatsRecycler.setAdapter(adapter);
        } else {
            adapter.setDataList(netTrafficStats);
        }
    }

    @Override
    public void onRefreshCompleted() {
        networkStatsRefreshLayout.setRefreshing(false);
    }
}