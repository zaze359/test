package com.zaze.component.animation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zaze.R;
import com.zz.library.commons.adapter.ZRecyclerAdapter;
import com.zz.library.util.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-24 - 14:58
 */
public class AnimationAdapter extends ZRecyclerAdapter<String, AnimationAdapter.AnimationHolder> {

    public AnimationAdapter(Context context, List<String> data) {
        super(context, data);
    }

    public AnimationAdapter(Context context, String[] data) {
        super(context, data);
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.list_item_animation;
    }

    @Override
    public AnimationHolder createViewHolder(View convertView) {
        return new AnimationHolder(convertView);
    }

    @Override
    public void onBindViewHolder(AnimationHolder holder, String value, int position) {
        holder.animationTitle.setText(StringUtil.parseString(value));
    }

    class AnimationHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.animation_title)
        TextView animationTitle;

        public AnimationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
