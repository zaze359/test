package com.zaze.demo.component.animation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zaze.demo.R;
import com.zaze.aarrepo.commons.base.adapter.BaseRecyclerAdapter;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.demo.model.entity.AnimationEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-24 - 14:58
 */
public class AnimationAdapter extends BaseRecyclerAdapter<AnimationEntity, AnimationAdapter.AnimationHolder> {

    public AnimationAdapter(Context context, List<AnimationEntity> data) {
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
    public void onBindViewHolder(AnimationHolder holder, AnimationEntity value, int position) {
        holder.animationTitle.setText(StringUtil.parseString(value.getName()));
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