package com.zaze.demo.component.fileexplorer.adapter;

import android.view.View;
import android.widget.TextView;

import com.zaze.demo.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-07-12 - 13:27
 */
public class FileItemHolder extends RecyclerView.ViewHolder {

    TextView itemFileNameTv;
    TextView itemFileIconTv;

    public FileItemHolder(@NonNull View itemView) {
        super(itemView);
        itemFileNameTv = itemView.findViewById(R.id.item_file_name_tv);
        itemFileIconTv = itemView.findViewById(R.id.item_file_icon_tv);
    }
}
