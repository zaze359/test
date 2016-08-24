package com.zaze.component.table;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.zaze.R;
import com.zaze.bean.TabEntity;
import com.zz.library.commons.widget.XHUltimateAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-15 - 17:06
 */
public class ToolAdapter extends XHUltimateAdapter<TabEntity, ToolAdapter.AppItemHolder> {

    public ToolAdapter(Context context, List<TabEntity> data) {
        super(context, data);
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.list_item_tool;
    }

    @Override
    public void onBindViewHolder(AppItemHolder holder, TabEntity value, int position) {
//        holder.itemToolIcon.setText(value.getName());
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
        @Bind(R.id.item_tool_icon)
        TextView itemToolIcon;
        @Bind(R.id.item_tool_name)
        TextView itemToolName;

        public AppItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
