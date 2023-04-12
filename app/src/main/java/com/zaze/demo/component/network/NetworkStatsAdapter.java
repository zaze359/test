package com.zaze.demo.component.network;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zaze.common.adapter.BaseRecyclerAdapter;
import com.zaze.demo.R;
import com.zaze.demo.debug.AppShortcut;
import com.zaze.demo.debug.NetTrafficStats;
import com.zaze.utils.AppUtil;
import com.zaze.utils.DescriptionUtil;
import com.zaze.utils.ZStringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-12-22 - 16:48
 */
public class NetworkStatsAdapter extends BaseRecyclerAdapter<NetTrafficStats, NetworkStatsAdapter.NetworkStatsHolder> {

    public NetworkStatsAdapter(Context context, Collection<NetTrafficStats> data) {
        super(context, data);
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.network_stats_item;
    }

    @Override
    public NetworkStatsHolder createViewHolder(@NotNull View convertView) {
        return new NetworkStatsHolder(convertView);
    }

    @Override
    public void onBindView(@NotNull NetworkStatsHolder holder, @NotNull NetTrafficStats value, int position) {
        AppShortcut appShortcut = value.getAppShortcut();
        Drawable drawable = null;
        if (appShortcut != null) {
            String packageName = appShortcut.getPackageName();
            drawable = AppUtil.getAppIcon(getContext(), packageName);
            holder.itemNetworkStatsAppPackageTv.setText(packageName);
            holder.itemNetworkStatsAppNameTv.setText(ZStringUtil.parseString(appShortcut.getAppName()));
        } else {
            holder.itemNetworkStatsAppNameTv.setText("");
        }
        if (drawable != null) {
            holder.itemNetworkStatsAppIv.setImageDrawable(drawable);
        } else {
            holder.itemNetworkStatsAppIv.setImageResource(R.drawable.ic_launcher);
        }
        holder.itemNetworkStatsReceiverTv.setText("接收: " + DescriptionUtil.toByteUnit(value.getRxBytes(), 1024));
        holder.itemNetworkStatsSendTv.setText("发送: " + DescriptionUtil.toByteUnit(value.getTxBytes(), 1024));
    }

    class NetworkStatsHolder extends RecyclerView.ViewHolder {
        ImageView itemNetworkStatsAppIv;
        TextView itemNetworkStatsAppNameTv;
        TextView itemNetworkStatsAppPackageTv;
        TextView itemNetworkStatsSendTv;
        TextView itemNetworkStatsReceiverTv;

        public NetworkStatsHolder(View itemView) {
            super(itemView);
            itemNetworkStatsAppIv = itemView.findViewById(R.id.item_network_stats_app_iv);
            itemNetworkStatsAppNameTv = itemView.findViewById(R.id.item_network_stats_app_name_tv);
            itemNetworkStatsAppPackageTv = itemView.findViewById(R.id.item_network_stats_app_package_tv);
            itemNetworkStatsSendTv = itemView.findViewById(R.id.item_network_stats_send_tv);
            itemNetworkStatsReceiverTv = itemView.findViewById(R.id.item_network_stats_receiver_tv);
        }
    }
}
