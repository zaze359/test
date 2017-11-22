package com.zaze.common.widget;

import android.database.Observable;
import android.widget.Button;

import com.zaze.utils.ZStringUtil;

import java.util.concurrent.TimeUnit;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-16 - 14:07
 */
public class IntervalButtonWidget {
    private Button button;
    private String defaultText;
    private Observable<Long> observable;
    private Subscriber<Long> subscriber;
    private int loopTime = 10;

    public void setLoopTime(int loopTime) {
        this.loopTime = loopTime;
    }

    public IntervalButtonWidget(Button button, String defaultText) {
        this.button = button;
        this.defaultText = defaultText;
        observable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(loopTime)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long o) {
                        return loopTime - o;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    public void start() {
        subscriber = new Subscriber<Long>() {
            @Override
            public void onStart() {
                super.onStart();
                updateButton(defaultText, false);
            }

            @Override
            public void onCompleted() {
                updateButton(defaultText, true);
            }

            @Override
            public void onError(Throwable e) {
                updateButton(defaultText, true);
            }

            @Override
            public void onNext(Long aLong) {
                updateButton("" + aLong, false);
            }
        };
        observable.subscribe(subscriber);
    }

    private void updateButton(String message, boolean enable) {
        if (button != null) {
            button.setText(ZStringUtil.parseString(message));
            button.setEnabled(enable);
        }
    }

    public void stop() {
        if (subscriber != null) {
            subscriber.unsubscribe();
        }
    }
}

