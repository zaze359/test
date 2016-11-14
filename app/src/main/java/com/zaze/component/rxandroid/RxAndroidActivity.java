package com.zaze.component.rxandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.zaze.R;
import com.zz.library.commons.base.BaseActivity;
import com.zz.library.commons.log.LogKit;

import java.util.concurrent.Callable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-14 - 02:53
 */
public class RxAndroidActivity extends BaseActivity {

    @Bind(R.id.rx_android_test_tv)
    TextView rxAndroidTestTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxandroid);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rx_android_test_btn)
    public void test() {
//        test1();
        test2();
    }

    private void updateTestText(String text) {
        rxAndroidTestTv.setText(text);
    }

    private void test1() {
        Observable<String> observable = Observable.just("RxAndroid Test1");
        observable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                LogKit.v("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogKit.v("onError");
            }

            @Override
            public void onNext(String s) {
                LogKit.v("onNext : %s", s);
                updateTestText(s);
            }
        });
    }

    public void test2() {
        Observable<String> observable = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000L);
                return "RxAndroid Test2";
            }
        });

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        updateTestText(s);
                    }
                });

    }


}
