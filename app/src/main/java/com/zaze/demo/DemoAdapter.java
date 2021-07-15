package com.zaze.demo;


import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zaze.common.adapter.BaseRecyclerAdapter;
import com.zaze.common.util.ActivityUtil;
import com.zaze.demo.model.entity.TableEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-15 - 17:06
 */
public class DemoAdapter extends BaseRecyclerAdapter<TableEntity, DemoAdapter.AppItemHolder> {
    private Activity activity;

    public DemoAdapter(Activity activity, List<TableEntity> data) {
        super(activity, data);
        this.activity = activity;
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.list_item_table;
    }

    @Override
    public AppItemHolder createViewHolder(@NotNull View convertView) {
        return new AppItemHolder(convertView);
    }

    @Override
    public void onBindView(@NotNull AppItemHolder holder, @NotNull final TableEntity value, int position) {
        holder.itemToolName.setText(value.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startActivityForResult(activity, value.getTargetClass(), 0);
            }
        });
    }

    public class AppItemHolder extends RecyclerView.ViewHolder {
        TextView itemToolName;

        public AppItemHolder(View itemView) {
            super(itemView);
            itemToolName = itemView.findViewById(R.id.item_table_name);
        }
    }
}
