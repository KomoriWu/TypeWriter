package com.komoriwu.typewriter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by KomoriWu
 * on 2017/9/12.
 */

public class TypeWriteTextView extends TextView {
    public static final int UPDATE_DELAY = 100;
    public static final int UPDATE_PERIOD = 100;
    private String mTextStr;
    private int mLength;
    private int mIndex;

    public TypeWriteTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TypeWriteTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TypeWriteTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mTextStr = (String) getText();
        mLength = mTextStr.length();

        Flowable.interval(UPDATE_DELAY, UPDATE_PERIOD, TimeUnit.MILLISECONDS)
                .take(mLength+1)
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        return mTextStr.substring(0, mIndex);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String str) throws Exception {
                        mIndex++;
                        setText(str);
                    }
                });
    }

}
