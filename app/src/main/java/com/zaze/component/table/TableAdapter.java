package com.zaze.component.table;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.zaze.R;
import com.zaze.model.entity.TableEntity;
import com.zz.library.commons.adapter.ZUltimateAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-15 - 17:06
 */
public class TableAdapter extends ZUltimateAdapter<TableEntity, TableAdapter.AppItemHolder> {

    public TableAdapter(Context context, List<TableEntity> data) {
        super(context, data);
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.list_item_table;
    }

    @Override
    public void onBindViewHolder(AppItemHolder holder, TableEntity value, int position) {
        holder.itemToolName.setText(value.getName());
    }

    //
    @Override
    public AppItemHolder getViewHolder(View view) {
        return new AppItemHolder(view);
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

    public class AppItemHolder extends UltimateRecyclerviewViewHolder {
        @Bind(R.id.item_table_name)
        TextView itemToolName;

        public AppItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
