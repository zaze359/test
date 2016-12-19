package com.zz.library.commons.loading;

import android.content.Context;

import com.zaze.aarrepo.utils.LocalDisplay;
import com.zz.library.R;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;


/**
 * Description : 这个添加一个加载样式
 * date : 2016-03-28 - 15:39
 *
 * @author : zaze
 * @version : 1.0
 */
public class LoadingStyle {
    public static void materialStyle(Context context, PtrFrameLayout frame) {
        init(frame);
        // --------  style1
//        final StoreHouseHeader header = new StoreHouseHeader(context);
//        header.setPadding(0, LocalDisplay.dp2px(15), 0, 0);
//        header.initWithStringArray(R.array.storehouse);
//        frame.setHeaderView(header);
//        frame.addPtrUIHandler(header);
        // --------  style2
        if(frame instanceof PtrClassicFrameLayout) {
            ((PtrClassicFrameLayout)frame).setLastUpdateTimeRelateObject(context);
        }
        materialStyle(context, frame, 100);

        // --------  style3
//        frame.setDurationToClose(100);
//        frame.setPinContent(true);
        // ----------------------------
    }

    private static void init(PtrFrameLayout frame){
        frame.setDurationToClose(200);
        frame.setDurationToCloseHeader(1000);
        frame.setKeepHeaderWhenRefresh(true);
        frame.setPullToRefresh(false);
        frame.setRatioOfHeaderHeightToRefresh(1.2f);
        frame.setResistance(1.7f);
    }

    public static void materialStyle(Context context, final PtrFrameLayout frame, int delay) {
        init(frame);
        final MaterialHeader header = new MaterialHeader(context);
        int[] colors = context.getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
        header.setPtrFrameLayout(frame);
        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);
        frame.postDelayed(new Runnable() {
            @Override
            public void run() {
                frame.autoRefresh(false);
            }
        }, delay);
    }
}
