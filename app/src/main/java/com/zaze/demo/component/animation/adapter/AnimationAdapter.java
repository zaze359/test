package com.zaze.demo.component.animation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zaze.common.adapter.ZRecyclerAdapter;
import com.zaze.demo.R;
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
public class AnimationAdapter extends ZRecyclerAdapter<AnimationEntity, AnimationAdapter.AnimationHolder> {

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
        holder.animationTitle.setText(ZStringUtil.parseString(value.getName()));
        final AnimationEntity value1 = getItem(holder.getAdapterPosition());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ZAppUtil.INSTANCE.startApplication(getContext(), "com.xuehai.response_launcher_teacher");
                ZActivityUtil.startActivity(getContext(), value1.getClazz());
            }
        });
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
