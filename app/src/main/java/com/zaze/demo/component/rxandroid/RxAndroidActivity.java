package com.zaze.demo.component.rxandroid;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zaze.common.base.BaseActivity;
import com.zaze.common.thread.ThreadPlugins;
import com.zaze.demo.R;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-14 - 02:53
 */
public class RxAndroidActivity extends BaseActivity {

    TextView rxAndroidTestTv;

    private boolean isRunning;
    private Scheduler scheduler = Schedulers.io();
    private int count = 0;

    Observable<String> observable1 = Observable.just(
            "RxAndroid Test Observable.just A",
            "RxAndroid Test Observable.just B",
            "RxAndroid Test Observable.just C"
    );
    Observable<String> observable2 = Observable.fromCallable(new Callable<String>() {
        @Override
        public String call() throws Exception {
            Thread.sleep(1000L);
            return "RxAndroid Observable.fromCallable";
        }
    });
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rxandroid_activity);
        rxAndroidTestTv = findViewById(R.id.rx_android_test_tv);
        findViewById(R.id.rx_android_test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                test();
                test5();
            }
        });
//        Intent intent = new Intent(this, AppActivity.class);
//        startActivity(intent);
    }

    private void updateTestText(String text) {
        rxAndroidTestTv.setText(text);
    }

    private void test() {
        count = 0;
        Observable.fromArray(true, false, true).filter(new Predicate<Boolean>() {
            @Override
            public boolean test(Boolean aBoolean) throws Exception {
                return aBoolean;
            }
        }).map(new Function<Boolean, String>() {
            @Override
            public String apply(Boolean aBoolean) throws Exception {
                count++;
                ZLog.i(ZTag.TAG_DEBUG, "count : " + count);
//                throw new NullPointerException();
                return String.valueOf(count);
            }
        })

//                .onErrorReturn(new Function<Throwable, String>() {
//                    @Override
//                    public String apply(Throwable throwable) throws Exception {
//                        ZLog.i(ZTag.TAG_DEBUG, "onErrorReturn count : " + count);
//                        return String.valueOf(count);
//                    }
//                })

                .concatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        if (true) {
                            return observable1;
                        } else {
                            return observable2;
                        }
                    }
                }).subscribe(new Observer<String>() {
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

//        if (!isRunning) {
//            isRunning = true;
////            test1();
//            test2();
////        test3();
////            test6();
////        test7();
//        }
    }

    StringBuilder builder = new StringBuilder();

    /**
     *
     */
    private void test1() {
        observable1.doFinally(new Action() {
            @Override
            public void run() throws Exception {
                isRunning = false;
            }
        }).subscribe(new Observer<String>() {
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
        observable2
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        isRunning = false;
                    }
                })
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
        ZLog.i(ZTag.TAG_DEBUG, "start");
        Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                ZLog.i(ZTag.TAG_DEBUG, "start do");
                Thread.sleep(1000L);
                ZLog.i(ZTag.TAG_DEBUG, "end do");
                return "RxAndroid Test Single.just";
            }
        }).doFinally(new Action() {
            @Override
            public void run() throws Exception {
                isRunning = false;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                updateTestText(s);
            }
        });
        ZLog.i(ZTag.TAG_DEBUG, "end");
    }
    // ----------------------------------------------------------------------

    public void test4() {
        PublishSubject.create().doFinally(new Action() {
            @Override
            public void run() throws Exception {
                isRunning = false;
            }
        });

    }
    // ----------------------------------------------------------------------

    /**
     * map()
     */
    public void test5() {
        Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                ThreadPlugins.runInWorkThread(new Runnable() {
                    @Override
                    public void run() {
                        compositeDisposable.dispose();
                    }
                }, 1000L);
                SystemClock.sleep(3000L);
                throw new NullPointerException("123");
//                return 4;
            }
        }).subscribeOn(Schedulers.computation()).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "map() : " + String.valueOf(integer);
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(@NotNull String s) throws Exception {
                ZLog.i(ZTag.TAG, "s : " + s);
                SystemClock.sleep(3000L);
                ZLog.i(ZTag.TAG, "s2 : " + s);
                throw new NullPointerException("123");
            }
        }).doFinally(() -> isRunning = false).subscribe(new Observer<String>() {
            private Disposable disposable;

            @Override
            public void onSubscribe(@NotNull Disposable d) {
                disposable = d;
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(@NotNull String s) {
                ZLog.i(ZTag.TAG, "onNext : " + s);
                updateTestText(s);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                e.printStackTrace();
                compositeDisposable.remove(disposable);
            }

            @Override
            public void onComplete() {
                compositeDisposable.remove(disposable);
            }
        });
    }

    private BiFunction<String, String, String> mMergeStringFunc = new BiFunction<String, String, String>() {
        @Override
        public String apply(String s, String s2) throws Exception {
            // 空格连接字符串
            return String.format("%s %s", s, s2);
        }
    };

    public void test6() {
        Flowable.create(new FlowableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(FlowableEmitter<List<String>> e) throws Exception {
                e.onNext(Arrays.asList("W", "X", "S", "I", "L", "U"));
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(scheduler)
                .flatMap(new Function<List<String>, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(List<String> strings) throws Exception {
                        return Flowable.fromIterable(strings);
                    }
                })
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
//                .reduce(mMergeStringFunc)
                .toList()
                .map(new Function<List<String>, String>() {
                    @Override
                    public String apply(List<String> strings) throws Exception {
                        StringBuilder builder = new StringBuilder();
                        for (String str : strings) {
                            builder.append(str);
                        }
                        return builder.toString();
                    }
                })
                .toFlowable()
//                .map(new Function<String, String>() {
//                    @Override
//                    public String apply(String s) throws Exception {
//                        updateTestText("apply : " + s);
//                        return s;
//                    }
//                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        ZLog.i(ZTag.TAG_DEBUG, "doFinally1");
                        updateTestText("doFinally1");
                        isRunning = false;
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        ZLog.i(ZTag.TAG_DEBUG, "doFinally2");
                        updateTestText("doFinally2");
                        isRunning = false;
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
                        subscription.cancel();
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
                        ZLog.i(ZTag.TAG_DEBUG, "onError : " + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        ZLog.i(ZTag.TAG_DEBUG, "onComplete");
                    }

                });
    }

    public void test7() {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                updateTestText("create");
                e.onNext(Arrays.asList("W", "X", "S", "I", "L", "U"));
                e.onComplete();
            }
        }).flatMap(new Function<List<String>, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(List<String> strings) throws Exception {
                return Observable.fromIterable(strings);
            }
        }).map(new Function<String, String>() {
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
        }).reduce(mMergeStringFunc).doFinally(new Action() {
            @Override
            public void run() throws Exception {
                ZLog.i(ZTag.TAG_DEBUG, "doFinally");
                updateTestText("doFinally");
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        isRunning = false;
                    }
                }).subscribe(new MaybeObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(String s) {
                ZLog.i(ZTag.TAG_DEBUG, "onSuccess : " + s);
                updateTestText(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    // ----------------------------------------------------------------------

}
