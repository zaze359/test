package com.zaze.component.device.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.zaze.R;
import com.zaze.aarrepo.commons.base.adapter.BaseUltimateAdapter;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.model.entity.DeviceStatus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-22 - 14:37
 */
public class DeviceAdapter extends BaseUltimateAdapter<DeviceStatus, DeviceAdapter.DeviceHolder> {

    public DeviceAdapter(Context context, List<DeviceStatus> data) {
        super(context, data);
    }

    @Override
    public DeviceHolder getViewHolder(View view, boolean b) {
        return new DeviceHolder(view, b);
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.list_item_device;
    }

    @Override
    public void onBindViewHolder(DeviceHolder deviceHolder, DeviceStatus deviceStatus, int i) {
        deviceHolder.itemDeviceTagTv.setText(StringUtil.parseString(deviceStatus.getTag()));
        deviceHolder.itemDeviceContentTv.setText(StringUtil.parseString(deviceStatus.getContent()));
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class DeviceHolder extends UltimateRecyclerviewViewHolder {
        @Bind(R.id.item_device_tag_tv)
        TextView itemDeviceTagTv;
        @Bind(R.id.item_device_content_tv)
        TextView itemDeviceContentTv;

        DeviceHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
