package com.zaze.demo.component.wifi.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaze.common.adapter.BaseRecyclerAdapter;
import com.zaze.demo.R;
import com.zaze.utils.ZNetUtil;

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


    public WifiAdapter(Context context, Collection<ScanResult> data) {
        super(context, data);
    }


    @Override
    public int getViewLayoutId() {
        return R.layout.wifi_info_recycle_item;
    }

    @Override
    public WifiViewHolder createViewHolder(View convertView) {
        return new WifiViewHolder(convertView);
    }

    @Override
    public void onBindView(WifiViewHolder holder, ScanResult value, int position) {

        // Wifi 描述
        String desc = ZNetUtil.formatWifiDesc(value);
        if (desc.isEmpty()) {
            desc = "未受保护的网络";
        } else {
            desc = "通过 " + desc + " 进行保护";
        }
        connInfo = ZNetUtil.getConnectionInfo(getContext());
        holder.itemWifiDescTv.setText(desc);
        holder.itemWifiInfoNameTv.setText(value.SSID);
        holder.itemWifiInfoNameTv.setText(value.SSID);
    }

    class WifiViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_wifi_info_name_tv)
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
