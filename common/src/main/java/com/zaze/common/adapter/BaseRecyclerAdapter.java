package com.zaze.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Description :
 * date : 2016-01-19 - 14:06
 *
 * @author : zaze
 * @version : 1.0
 */
@Deprecated
public abstract class BaseRecyclerAdapter<V, H extends RecyclerView.ViewHolder> extends DataRecyclerAdapter<V, H> implements ResourceAdapter {
    private final Context context;

    public BaseRecyclerAdapter(Context context, @Nullable Collection<? extends V> data) {
        super(data);
        this.context = context;
    }

    @NotNull
    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getViewLayoutId(), parent, false);
        return createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull H holder, int position) {
        V v = getItem(position);
        if (v != null) {
            onBindView(holder, v, position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Context getContext() {
        return context;
    }


    // --------------------------------------------------

    /**
     * get view layout id
     *
     * @return int
     */
    public abstract int getViewLayoutId();

    /**
     * 构建viewHolder
     *
     * @param convertView convertView
     * @return H
     */
    public abstract H createViewHolder(@NotNull View convertView);

    /**
     * view赋值
     *
     * @param holder   holder
     * @param value    value
     * @param position position
     */
    public abstract void onBindView(@NotNull H holder, @NotNull V value, int position);

    // --------------------------------------------------
    @Override
    public <T extends View> T findView(View parentView, int resId) {
        return parentView.findViewById(resId);
    }

    @Override
    public @ColorInt int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(context, resId);
    }

    @Override
    public String getString(int resId, Object... args) {
        return context.getString(resId, args);
    }

    @Override
    public Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(context, id);
    }

    @Override
    public Bitmap getBitmap(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }
}
