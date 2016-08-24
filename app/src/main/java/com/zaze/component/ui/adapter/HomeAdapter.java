package com.zaze.component.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaze.R;
import com.zz.library.commons.adapter.ZListAdapter;
import com.zz.library.commons.widget.PinnedSectionListView;
import com.zz.library.util.ViewUtil;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-05-24 - 18:45
 */
public class HomeAdapter extends ZListAdapter<String, HomeAdapter.StringItemHolder>
        implements PinnedSectionListView.PinnedSectionListAdapter {

    public HomeAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public void setViewData(String value, StringItemHolder itemHolder, int position, View convertView, ViewGroup parent) {
        itemHolder.textView.setText(value);
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.item_layout_home;
    }

    @Override
    public StringItemHolder createViewHolder(View convertView) {
        return new StringItemHolder(convertView);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).length();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == 1;
    }

    public class StringItemHolder {
        public TextView textView;

        public StringItemHolder(View view) {
            textView = ViewUtil.findView(view, R.id.desc_tv);
        }
    }
}
