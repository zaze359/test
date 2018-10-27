package com.zaze.demo.component.okhttp.ui;


import android.os.Bundle;
import android.view.View;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.okhttp.OkHttpUtil;
import com.zaze.demo.component.okhttp.presenter.OkHttpPresenter;
import com.zaze.demo.component.okhttp.presenter.impl.OkHttpPresenterImpl;
import com.zaze.demo.component.okhttp.view.OkHttpView;
import com.zaze.utils.ZStringUtil;
import com.zaze.utils.log.ZLog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-06-15 01:25 1.0
 */
public class OkHttpActivity extends BaseActivity implements OkHttpView {
    private OkHttpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ok_http_activity);
        presenter = new OkHttpPresenterImpl(this);

        findViewById(R.id.http_test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = "http://zt.yunzuoye.net:84/apps/overt/"
                String json = "{\"M\":1,\"C\":\"1000\",\"S\":\"1\",\"sReceiverName\":\"aichenk\",\"sDevNumber\":\"112233\",\"sMacAddress\":\"DFS:DFSA\",\"iReceivePurpose\":0,\"sReceiverAccount\":\"aichenk\"}";
//                post("http://zt.yunzuoye.net:84/apps/overt/main/register-tablet-receiver", JsonUtil.objToJson(entity));
                post("http://common.yunzuoye.net/overt/main/register-tablet-receiver", json);
//                post("http://192.168.5.29:8083/api/v1/platform/login", "{\"userName\":\"xhyz13\",\"password\":\"E10ADC3949BA59ABBE56E057F20F883E\"}");
            }
        });
    }


    //        public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");

    void post(String url, String json) {
        String str = ZStringUtil.format("K=%s", json, 0);
        RequestBody body = RequestBody.create(FORM, str);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ZLog.i("okhttp", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ZLog.i("okhttp", response.body().string());
            }
        });
    }
}