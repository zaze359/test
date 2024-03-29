package com.zaze.demo;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zaze.common.adapter.BaseRecyclerAdapter;
import com.zaze.common.util.ActivityUtil;
import com.zaze.demo.data.entity.TableEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Singleton;

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
                if (value.getTargetClass() != null) {
                    Intent intent = new Intent(activity, value.getTargetClass());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    ActivityUtil.startActivityForResult(activity, intent, 0);
                    ActivityUtil.startActivity(activity, intent);
                } else {
                    ARouter.getInstance().build(value.getRoute())
//                            .withLong("key", 1L)
                            .navigation();
                }
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
