package com.zaze.component.rxandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.zaze.R;
import com.zaze.aarrepo.commons.base.BaseActivity;

import java.util.concurrent.Callable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

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

    private void updateTestText(String text) {
        rxAndroidTestTv.setText(text);
    }

    @OnClick(R.id.rx_android_test_btn)
    public void test() {
//        test1();
        test2();
        test3();
    }


    /**
     *
     */
    private void test1() {
        Observable<String> observable = Observable.just("RxAndroid Test Observable.just");
        observable.subscribe(new Observer<String>() {
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

    /**
     * Observable.fromCallable()方法可以拖延Observable获取数据的操作，这一点在数据需要在其他线程获取时尤其重要
     * subscribeOn()让我们在指定线程中运行获取数据的代码，只要不是UI线程就行
     * observeOn()让我们在合适的线程中接收Observable发送的数据，在这里是UI主线程
     */
    public void test2() {
        Observable<String> observable = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(3000L);
                return "RxAndroid Observable.fromCallable";
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

    /**
     * Single 的使用
     */
    public void test3() {
        Single<String> single = Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "RxAndroid Test Single.just";
            }
        });
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<String>() {
                    @Override
                    public void onSuccess(String value) {
                        updateTestText(value);
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });
    }

    public void test4() {
        PublishSubject.create();
    }

    /**
     * map()
     */
    public void test5() {
        Single.just(4).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return "map() : " + String.valueOf(integer);
            }
        }).subscribe(new SingleSubscriber<String>() {
            @Override
            public void onSuccess(String value) {
                updateTestText(value);
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }
}
