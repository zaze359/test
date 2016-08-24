package com.zaze.component.animation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zz.library.commons.adapter.ZRecyclerAdapter;

import java.util.List;

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

    @Override
    public int getViewLayoutId() {
        return 0;
    }

    @Override
    public AnimationHolder createViewHolder(View convertView) {
        return null;
    }

    @Override
    public void onBindViewHolder(AnimationHolder holder, String value, int position) {

    }

    class AnimationHolder extends RecyclerView.ViewHolder {

        public AnimationHolder(View itemView) {
            super(itemView);
        }
    }
}
