package com.zaze.common.widget;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zaze.common.R;
import com.zaze.utils.ZOnClickHelper;
import com.zaze.utils.ZStringUtil;
import com.zaze.utils.ZViewUtil;


/**
 * Description :
 * date : 2016-01-05 - 10:43
 *
 * @author : zaze
 * @version : 1.0
 */
public class LoadingWidget {
    private Dialog progressDialog;
    private TextView loadingMsgTv;

    public LoadingWidget(Context context) {
        progressDialog = new Dialog(context, R.style.MyDialog);
        View view = View.inflate(context, R.layout.layout_loading, null);
        loadingMsgTv = ZViewUtil.findView(view, R.id.loading_desc_tv);
        progressDialog.setContentView(view);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void showProgress() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void updateProgress(int progress) {
        if (progressDialog.isShowing()) {
//            progressDialog.setProgress(progress);
        }
    }

    public void dismissProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void setTextOnClick(View.OnClickListener onClickListener) {
        ZOnClickHelper.setOnClickListener(loadingMsgTv, onClickListener);
    }

    public void setText(String text) {
        loadingMsgTv.setText(ZStringUtil.parseString(text));
    }
}
