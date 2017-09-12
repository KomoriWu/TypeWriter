package com.komoriwu.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
    public static final int UPDATE_DELAY = 10;
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
        int speed = 100;
        boolean isEnableTypeWrite = true;
        boolean isEnableTtf = true;
        if (attrs != null) {
            TypedArray typedArray = context.getTheme().
                    obtainStyledAttributes(attrs, R.styleable.TypeWrite, defStyleAttr, 0);
            int n = typedArray.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.TypeWrite_setSpeed) {
                    speed = typedArray.getInteger(attr, 100);

                } else if (attr == R.styleable.TypeWrite_isEnableTypeWrite) {
                    isEnableTypeWrite = typedArray.getBoolean(attr, true);

                } else if (attr == R.styleable.TypeWrite_isEnableTtf) {
                    isEnableTtf = typedArray.getBoolean(attr, true);

                }
            }
            typedArray.recycle();
        }
        if (isEnableTtf) {
            String fontName = "Heavy.ttf";
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/" + fontName), defStyleAttr);
        }

        if (isEnableTypeWrite) {
            Flowable.interval(UPDATE_DELAY, speed, TimeUnit.MILLISECONDS)
                    .take(mLength + 1)
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

}
