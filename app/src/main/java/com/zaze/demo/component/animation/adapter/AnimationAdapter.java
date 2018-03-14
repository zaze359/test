package com.zaze.demo.component.animation.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zaze.common.adapter.BaseRecyclerAdapter;
import com.zaze.demo.R;
import com.zaze.demo.component.animation.ui.SharedElementActivity;
import com.zaze.demo.model.entity.AnimationEntity;
import com.zaze.utils.ZActivityUtil;
import com.zaze.utils.ZStringUtil;

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
        return R.layout.animation_recycle_item;
    }

    @Override
    public AnimationHolder createViewHolder(View convertView) {
        return new AnimationHolder(convertView);
    }

    @Override
    public void onBindView(final AnimationHolder holder, final AnimationEntity value, final int position) {
        holder.animationTitle.setText(ZStringUtil.parseString(value.getName()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    Activity activity = (Activity) getContext();
                    switch (position) {
                        case AnimationEntity.Type.SCENE_TRANSITION:
                            ZActivityUtil.makeSceneTransitionAnimation(activity, value.getTargetClass());
                            break;
                        case AnimationEntity.Type.SCALE_UP:
                            ZActivityUtil.makeScaleUpAnimation(activity, value.getTargetClass(), holder.itemView, 100, 100, 100, 100);
                            break;
                        case AnimationEntity.Type.SHARED_ELEMENT:
                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, new Pair<>(holder.itemView, "holder.itemView"));
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(SharedElementActivity.EXTRA_ENTITY, value);
                            ZActivityUtil.startActivityForAnim(activity, value.getTargetClass(), optionsCompat, bundle);
                            break;
                        default:
                            ZActivityUtil.makeSceneTransitionAnimation(activity, value.getTargetClass());
                            break;
                    }
                }
            }
        });
    }

    class AnimationHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.animation_title)
        TextView animationTitle;

        AnimationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // --------------------------------------------------


}
