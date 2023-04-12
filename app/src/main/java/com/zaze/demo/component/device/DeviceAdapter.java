package com.zaze.demo.component.device;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zaze.common.adapter.BaseRecyclerAdapter;
import com.zaze.core.model.data.DeviceStatus;
import com.zaze.demo.R;
import com.zaze.utils.ZStringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-22 - 14:37
 */
class DeviceAdapter extends BaseRecyclerAdapter<DeviceStatus, DeviceAdapter.DeviceHolder> {

    DeviceAdapter(Context context, List<DeviceStatus> data) {
        super(context, data);
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.device_info_item;
    }

    @Override
    public DeviceHolder createViewHolder(@NotNull View convertView) {
        return new DeviceHolder(convertView);
    }

    @Override
    public void onBindView(@NotNull DeviceHolder deviceHolder, @NotNull DeviceStatus deviceStatus, int i) {
        deviceHolder.itemDeviceTagTv.setText(ZStringUtil.parseString(deviceStatus.getTag()));
        deviceHolder.itemDeviceContentTv.setText(ZStringUtil.parseString(deviceStatus.getContent()));
    }

    class DeviceHolder extends RecyclerView.ViewHolder {
        TextView itemDeviceTagTv;
        TextView itemDeviceContentTv;

        public DeviceHolder(@NonNull View itemView) {
            super(itemView);
            itemDeviceTagTv = findView(itemView, R.id.itemDeviceTagTv);
            itemDeviceContentTv = findView(itemView, R.id.itemDeviceContentTv);
        }
    }
}
