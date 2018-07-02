package com.zaze.demo.component.wifi.adapter;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaze.common.adapter.BaseRecyclerAdapter;
import com.zaze.demo.R;
import com.zaze.utils.ZNetUtil;
import com.zaze.utils.ZOnClickHelper;

import java.util.Collection;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-25 - 19:45
 */
public class WifiAdapter extends BaseRecyclerAdapter<ScanResult, WifiAdapter.WifiViewHolder> {

    private WifiInfo connInfo;
    private NetworkInfo networkInfo;

    public WifiAdapter(Context context, Collection<ScanResult> data) {
        super(context, data);
    }


    @Override
    public int getViewLayoutId() {
        return R.layout.wifi_item;
    }

    @Override
    public WifiViewHolder createViewHolder(View convertView) {
        return new WifiViewHolder(convertView);
    }

    @Override
    protected void setDataList(Collection<ScanResult> data, boolean isNotify) {
        networkInfo = ZNetUtil.getWifiInfo(getContext());
        connInfo = ZNetUtil.getConnectionInfo(getContext());
        super.setDataList(data, isNotify);
    }

    @Override
    public void onBindView(WifiViewHolder holder, final ScanResult value, int position) {
        String desc = null;
        if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            if (isConnect(value)) {
                desc = "已连接";
                holder.itemWifiDescTv.setTextColor(getColor(R.color.green));
            }
        }
        if (TextUtils.isEmpty(desc)) {
            desc = ZNetUtil.formatWifiDesc(value);
            if (desc.isEmpty()) {
                desc = "未受保护的网络";
            } else {
                desc = "通过 " + desc + " 进行保护";
            }
            holder.itemWifiDescTv.setTextColor(getColor(R.color.black));
        }
        holder.itemWifiDescTv.setText(desc);
        holder.itemWifiInfoNameTv.setText(value.SSID);
        ZOnClickHelper.setOnClickListener(holder.itemView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnect(value)) {
                    // 已连接，显示连接状态对话框
                } else {
                    // 未连接显示连接输入对话框
                }
            }
        });
    }

    private boolean isConnect(ScanResult scanResult) {
        return connInfo != null && ZNetUtil.isSSIDEquals(connInfo.getSSID(), scanResult.SSID);
    }


    class WifiViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_wifi_name_tv)
        TextView itemWifiInfoNameTv;
        @Bind(R.id.item_wifi_desc_tv)
        TextView itemWifiDescTv;
        @Bind(R.id.item_wifi_level_img)
        ImageView itemWifiLevelImg;

        public WifiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
