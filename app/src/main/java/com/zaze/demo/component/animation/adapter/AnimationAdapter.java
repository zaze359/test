package com.zaze.demo.component.animation.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.zaze.common.adapter.BaseRecyclerAdapter;
import com.zaze.common.util.ActivityUtil;
import com.zaze.demo.R;
import com.zaze.demo.component.animation.ui.SharedElementActivity;
import com.zaze.demo.model.entity.AnimationEntity;
import com.zaze.utils.ZStringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    public AnimationHolder createViewHolder(@NotNull View convertView) {
        return new AnimationHolder(convertView);
    }

    @Override
    public void onBindView(@NotNull final AnimationHolder holder, final AnimationEntity value, final int position) {
        holder.animationTitle.setText(ZStringUtil.parseString(value.getName()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    Activity activity = (Activity) getContext();
                    switch (position) {
                        case AnimationEntity.Type.SCENE_TRANSITION:
                            ActivityUtil.makeSceneTransitionAnimation(activity, value.getTargetClass());
                            break;
                        case AnimationEntity.Type.SCALE_UP:
                            ActivityUtil.makeScaleUpAnimation(activity, value.getTargetClass(), holder.itemView, 100, 100, 100, 100);
                            break;
                        case AnimationEntity.Type.SHARED_ELEMENT:
                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, new Pair<>(holder.itemView, "holder.itemView"));
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(SharedElementActivity.EXTRA_ENTITY, value);
                            ActivityUtil.startActivityForAnim(activity, value.getTargetClass(), optionsCompat, bundle);
                            break;
                        default:
                            ActivityUtil.makeSceneTransitionAnimation(activity, value.getTargetClass());
                            break;
                    }
                }
            }
        });
    }

    class AnimationHolder extends RecyclerView.ViewHolder {
        TextView animationTitle;

        AnimationHolder(View itemView) {
            super(itemView);
            animationTitle = itemView.findViewById(R.id.animation_title);
        }
    }

    // --------------------------------------------------


}
