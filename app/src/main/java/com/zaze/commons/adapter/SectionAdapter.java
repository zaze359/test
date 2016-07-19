package com.zaze.commons.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zaze.commons.widget.PinnedSectionListView;

import java.util.Collection;

/**
 * Description :
 * @author : ZAZE
 * @version : 2016-06-03 - 17:07
 */
public class SectionAdapter<V, CH, GH> extends DataAdapter<V>
        implements ChildViewAdapter<V, CH>, PinnedSectionListView.PinnedSectionListAdapter {

    public SectionAdapter(Context context, Collection<V> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(isItemViewTypePinned(getItemViewType(position))) {
        } else {
        }
        return view;
    }

    @Override
    public void setViewData(V value, CH itemHolder, int position, View convertView, ViewGroup parent) {

    }

    @Override
    public int getViewLayoutId() {
        return 0;
    }

    @Override
    public CH createViewHolder(View convertView) {
        return null;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return false;
    }
}
