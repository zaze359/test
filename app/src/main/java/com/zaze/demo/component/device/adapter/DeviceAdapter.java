package com.zaze.demo.component.device.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zaze.common.adapter.BaseRecyclerAdapter;
import com.zaze.demo.R;
import com.zaze.demo.model.entity.DeviceStatus;
import com.zaze.utils.ZStringUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-22 - 14:37
 */
public class DeviceAdapter extends BaseRecyclerAdapter<DeviceStatus, DeviceAdapter.DeviceHolder> {

    public DeviceAdapter(Context context, List<DeviceStatus> data) {
        super(context, data);
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.list_item_device;
    }

    @Override
    public DeviceHolder createViewHolder(View convertView) {
        return new DeviceHolder(convertView);
    }

    @Override
    public void onBindView(DeviceHolder deviceHolder, DeviceStatus deviceStatus, int i) {
        deviceHolder.itemDeviceTagTv.setText(ZStringUtil.parseString(deviceStatus.getTag()));
        deviceHolder.itemDeviceContentTv.setText(ZStringUtil.parseString(deviceStatus.getContent()));
    }

    class DeviceHolder extends RecyclerView.ViewHolder {
        TextView itemDeviceTagTv;
        TextView itemDeviceContentTv;

        public DeviceHolder(@NonNull View itemView) {
            super(itemView);
            itemDeviceTagTv = findView(itemView, R.id.item_device_tag_tv);
            itemDeviceContentTv = findView(itemView, R.id.item_device_content_tv);
        }
    }
}
