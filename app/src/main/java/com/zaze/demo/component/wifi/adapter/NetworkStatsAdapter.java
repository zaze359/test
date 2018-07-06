package com.zaze.demo.component.wifi.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaze.common.adapter.BaseRecyclerAdapter;
import com.zaze.demo.R;
import com.zaze.demo.component.wifi.presenter.NetworkStatsPresenter;
import com.zaze.demo.debug.AppShortcut;
import com.zaze.demo.debug.NetTrafficStats;
import com.zaze.utils.DescriptionUtil;
import com.zaze.utils.AppUtil;
import com.zaze.utils.ZStringUtil;

import java.util.Collection;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 16:48
 */
public class NetworkStatsAdapter extends BaseRecyclerAdapter<NetTrafficStats, NetworkStatsAdapter.NetworkStatsHolder> {
    private NetworkStatsPresenter presenter;

    public NetworkStatsAdapter(Context context, Collection<NetTrafficStats> data, NetworkStatsPresenter presenter) {
        super(context, data);
        this.presenter = presenter;
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.network_stats_item;
    }

    @Override
    public NetworkStatsHolder createViewHolder(View convertView) {
        return new NetworkStatsHolder(convertView);
    }

    @Override
    public void onBindView(NetworkStatsHolder holder, NetTrafficStats value, int position) {
        AppShortcut appShortcut = value.getAppShortcut();
        Drawable drawable = null;
        if (appShortcut != null) {
            String packageName = appShortcut.getPackageName();
            drawable = AppUtil.getAppIcon(getContext(), packageName);
            holder.itemNetworkStatsAppPackageTv.setText(packageName);
        }
        if (drawable != null) {
            holder.itemNetworkStatsAppIv.setImageDrawable(drawable);
        } else {
            holder.itemNetworkStatsAppIv.setImageResource(R.drawable.ic_launcher);
        }
        holder.itemNetworkStatsAppNameTv.setText(ZStringUtil.parseString(appShortcut.getName()));
        holder.itemNetworkStatsReceiverTv.setText("接收: " + DescriptionUtil.toByteUnit(value.getRxBytes()));
        holder.itemNetworkStatsSendTv.setText("发送: " + DescriptionUtil.toByteUnit(value.getTxBytes()));
    }

    class NetworkStatsHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_network_stats_app_iv)
        ImageView itemNetworkStatsAppIv;
        @Bind(R.id.item_network_stats_app_name_tv)
        TextView itemNetworkStatsAppNameTv;
        @Bind(R.id.item_network_stats_app_package_tv)
        TextView itemNetworkStatsAppPackageTv;
        @Bind(R.id.item_network_stats_send_tv)
        TextView itemNetworkStatsSendTv;
        @Bind(R.id.item_network_stats_receiver_tv)
        TextView itemNetworkStatsReceiverTv;

        public NetworkStatsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
