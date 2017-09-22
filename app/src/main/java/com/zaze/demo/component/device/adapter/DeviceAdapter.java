package com.zaze.demo.component.device.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaze.common.adapter.third.ZUltimateRecycleAdapter;
import com.zaze.common.adapter.third.ZUltimateRecyclerViewHolder;
import com.zaze.demo.R;
import com.zaze.demo.model.entity.DeviceStatus;
import com.zaze.utils.ZStringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-22 - 14:37
 */
public class DeviceAdapter extends ZUltimateRecycleAdapter<DeviceStatus, DeviceAdapter.DeviceHolder> {

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
    public void onBindView(DeviceHolder deviceHolder, DeviceStatus deviceStatus, int i) {
        deviceHolder.itemDeviceTagTv.setText(ZStringUtil.parseString(deviceStatus.getTag()));
        deviceHolder.itemDeviceContentTv.setText(ZStringUtil.parseString(deviceStatus.getContent()));
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

    class DeviceHolder extends ZUltimateRecyclerViewHolder {
        @Bind(R.id.item_device_tag_tv)
        TextView itemDeviceTagTv;
        @Bind(R.id.item_device_content_tv)
        TextView itemDeviceContentTv;

        public DeviceHolder(View itemView, boolean isItem) {
            super(itemView, isItem);
        }

        @Override
        protected void initView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
