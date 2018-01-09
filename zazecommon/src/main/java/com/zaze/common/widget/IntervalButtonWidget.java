package com.zaze.common.widget;

import android.widget.Button;

import com.zaze.utils.ZStringUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Description : 倒计时button
 *
 * @author : ZAZE
 * @version : 2017-11-16 - 14:07
 */
public class IntervalButtonWidget {
    private Button button;
    private String defaultText;
    private Observable<Long> observable;
    private Disposable disposable;
    private int loopTime = 10;

    public void setLoopTime(int loopTime) {
        this.loopTime = loopTime;
    }

    public IntervalButtonWidget(Button button, String defaultText) {
        this.button = button;
        this.defaultText = defaultText;
        observable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(loopTime)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return loopTime - aLong;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    public void start() {
        observable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                updateButton(defaultText, false);
            }

            @Override
            public void onNext(Long aLong) {
                updateButton("" + aLong, false);
            }

            @Override
            public void onError(Throwable e) {
                updateButton(defaultText, true);
            }

            @Override
            public void onComplete() {
                updateButton(defaultText, true);
            }
        });
    }

    private void updateButton(String message, boolean enable) {
        if (button != null) {
            button.setText(ZStringUtil.parseString(message));
            button.setEnabled(enable);
        }
    }

    public void stop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}

