package com.zaze.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaze.R;
import com.zaze.commons.adapter.ZAdapter;
import com.zaze.util.ViewUtil;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-05-24 - 18:45
 */
public class HomeAdapter extends ZAdapter<String, HomeAdapter.StringItemHolder> {

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

    public class StringItemHolder {
        public TextView textView;

        public StringItemHolder(View view) {
            textView = ViewUtil.findView(view, R.id.desc_tv);
        }
    }
}
