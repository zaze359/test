package com.zaze.demo.component.readpackage.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaze.aarrepo.commons.base.ZBaseApplication;
import com.zaze.aarrepo.commons.base.adapter.BaseItemHolder;
import com.zaze.aarrepo.commons.base.adapter.BaseUltimateAdapter;
import com.zaze.aarrepo.utils.AppUtil;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.demo.R;
import com.zaze.demo.model.entity.PackageEntity;

import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-04-17 - 17:21
 */
public class ReadPackageAdapter extends BaseUltimateAdapter<PackageEntity, ReadPackageAdapter.PackageHolder> {

    public ReadPackageAdapter(Context context, Collection<PackageEntity> data) {
        super(context, data);
    }

    @Override
    public PackageHolder getViewHolder(View view, boolean isItem) {
        return new PackageHolder(view, isItem);
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.list_item_package;
    }

    @Override
    public void onBindViewHolder(PackageHolder holder, PackageEntity value, int position) {
        String packageName = StringUtil.parseString(value.getPackageName());
        holder.itemPackageTv.setText(StringUtil.format("%s : %s", AppUtil.getAppName(ZBaseApplication.getInstance(), packageName, ""), packageName));
        Drawable drawable = AppUtil.getAppIcon(ZBaseApplication.getInstance(), packageName);
        if (drawable == null) {
            drawable = getDrawable(R.drawable.ic_launcher);
        }
        holder.itemPackageIv.setImageDrawable(drawable);

    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
    }


    public String getPackageListStr() {
        List<PackageEntity> packageEntityList = getDataList();
        StringBuilder builder = new StringBuilder();
        for (PackageEntity entity : packageEntityList) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(entity.getPackageName());
        }
        return builder.toString();
    }

    class PackageHolder extends BaseItemHolder {
        @Bind(R.id.item_package_iv)
        ImageView itemPackageIv;
        @Bind(R.id.item_package_tv)
        TextView itemPackageTv;


        PackageHolder(View itemView, boolean isItem) {
            super(itemView, isItem);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void initView(View itemView) {

        }
    }
}
