package com.zaze.demo.component.fileexplorer.adapter;

import android.view.View;
import android.widget.TextView;

import com.zaze.demo.third.BaseUltimateRecyclerViewHolder;
import com.zaze.demo.R;

import org.jetbrains.annotations.NotNull;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-07-12 - 13:27
 */
public class FileItemHolder extends BaseUltimateRecyclerViewHolder {

    @Bind(R.id.item_file_name_tv)
    TextView itemFileNameTv;
    @Bind(R.id.item_file_icon_tv)
    TextView itemFileIconTv;

    public FileItemHolder(@NotNull View itemView, boolean isItem) {
        super(itemView, isItem);
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void initView(@NotNull View itemView) {

    }
}
