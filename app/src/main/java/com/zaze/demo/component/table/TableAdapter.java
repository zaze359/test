package com.zaze.demo.component.table;


import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zaze.common.adapter.BaseRecyclerAdapter;
import com.zaze.common.util.ActivityUtil;
import com.zaze.demo.R;
import com.zaze.demo.model.entity.TableEntity;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-15 - 17:06
 */
public class TableAdapter extends BaseRecyclerAdapter<TableEntity, TableAdapter.AppItemHolder> {
    private Activity activity;

    public TableAdapter(Activity activity, List<TableEntity> data) {
        super(activity, data);
        this.activity = activity;
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.list_item_table;
    }

    @Override
    public AppItemHolder createViewHolder(View convertView) {
        return new AppItemHolder(convertView);
    }

    @Override
    public void onBindView(AppItemHolder holder, final TableEntity value, int position) {
        holder.itemToolName.setText(value.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startActivity(activity, value.getTargetClass());
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
