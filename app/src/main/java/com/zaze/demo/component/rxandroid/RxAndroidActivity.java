package com.zaze.demo.component.rxandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import kotlin.jvm.functions.Function2;

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
//        test2();
//        test3();
        test6();
    }

    StringBuilder builder = new StringBuilder();

    /**
     *
     */
    private void test1() {
        Observable<String> observable = Observable.just(
                "RxAndroid Test Observable.just A",
                "RxAndroid Test Observable.just B",
                "RxAndroid Test Observable.just C"

        );
        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                builder.append(s).append("\n");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                updateTestText(builder.toString());
            }
        });
    }
    // ----------------------------------------------------------------------

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
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        updateTestText(s);
                    }
                });

    }
    // ----------------------------------------------------------------------

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
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        updateTestText(s);
                    }
                });
    }
    // ----------------------------------------------------------------------

    public void test4() {
        PublishSubject.create();
    }
    // ----------------------------------------------------------------------

    /**
     * map()
     */
    public void test5() {
        Single.just(4).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "map() : " + String.valueOf(integer);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String value) throws Exception {
                updateTestText(value);
            }
        });
    }

    private Function2<String, String, String> mMergeStringFunc = new Function2<String, String, String>() {
        @Override
        public String invoke(String s, String s2) {
            // 空格连接字符串
            return String.format("%s %s", s, s2);
        }
    };

    public void test6() {
//        Flowable.fromCallable(new Callable<List<String>>() {
//            @Override
//            public List<String> call() throws Exception {
//                return Arrays.asList("W", "X", "S", "I", "L", "U");
//            }
//        })
        Flowable.create(new FlowableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(FlowableEmitter<List<String>> e) throws Exception {
                updateTestText("create");
                e.onNext(Arrays.asList("W", "X", "S", "I", "L", "U"));
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
//                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<List<String>, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(List<String> strings) throws Exception {
                        return Flowable.fromIterable(strings);
                    }
                })
                .observeOn(Schedulers.io())
//                .doOnSubscribe(new Consumer<Subscription>() {
//                    @Override
//                    public void accept(Subscription subscription) throws Exception {
//                        ZLog.i(ZTag.TAG_DEBUG, "doOnSubscribe");
//                        updateTestText("doOnSubscribe");
////                        subscription.cancel();
//                    }
//                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        ZLog.i(ZTag.TAG_DEBUG, "apply : " + s);
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return s;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
//                .map(new Function<String, String>() {
//                    @Override
//                    public String apply(String s) throws Exception {
//                        updateTestText("apply : " + s);
//                        return s;
//                    }
//                })
//                .reduce(mMergeStringFunc)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        ZLog.i(ZTag.TAG_DEBUG, "doFinally");
                        updateTestText("doFinally");
                    }
                })
                .subscribe(new Subscriber<String>() {
                    private Subscription subscription;

                    @Override
                    public void onSubscribe(Subscription s) {
                        ZLog.i(ZTag.TAG_DEBUG, "onStart");
                        subscription = s;
                        // 请求几个执行几个
                        subscription.request(1);
//                        s.request(100);
                    }

                    @Override
                    public void onNext(String s) {
                        ZLog.i(ZTag.TAG_DEBUG, "onNext : " + s);
                        updateTestText(s);
                        subscription.request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        ZLog.i(ZTag.TAG_DEBUG, "onError : " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        ZLog.i(ZTag.TAG_DEBUG, "onComplete");
                    }

                });

    }

    // ----------------------------------------------------------------------

}
