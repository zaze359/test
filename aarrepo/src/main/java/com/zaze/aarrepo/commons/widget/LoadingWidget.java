package com.zaze.aarrepo.commons.widget;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zaze.aarrepo.R;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.aarrepo.utils.ViewUtil;
import com.zaze.aarrepo.utils.helper.OnClickHelper;

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
        loadingMsgTv = ViewUtil.findView(view, R.id.loading_desc_tv);
        progressDialog.setContentView(view);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);// 点击dialog外不消失
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
        OnClickHelper.setOnClickListener(loadingMsgTv, onClickListener);
    }

    public void setText(String text) {
        loadingMsgTv.setText(StringUtil.parseString(text));
    }
}
