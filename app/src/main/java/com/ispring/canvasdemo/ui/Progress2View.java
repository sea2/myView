package com.ispring.canvasdemo.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by lhy on 2017/4/10.
 */

public class Progress2View extends View {
    private final RectF rect;
    private final Paint bigPaint;
    int progressInt = 0;

    public Progress2View(Context context) {
        this(context, null);
    }

    public Progress2View(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Progress2View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rect = new RectF();
        rect.top = 10;
        rect.left = 10;
        rect.right = 200;
        rect.bottom = 200;

         bigPaint = new Paint();


    }



    /**
     * MeasureSpec.EXACTLY   精确
     * MeasureSpec.UNSPECIFIED 自适应未指明
     * MeasureSpec.AT_MOST 子对象可以任意大-父布局内最大
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {

            float textWidth = rect.width() + 20;
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {

            float textHeight = rect.height() + 20;
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }



    @Override
    protected void onDraw(Canvas canvas) {


        bigPaint.setAntiAlias(true);
        bigPaint.setStyle(Paint.Style.STROKE);
        bigPaint.setStrokeWidth(20);
        bigPaint.setColor(Color.parseColor("#111111"));
        bigPaint.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawArc(rect, 135, 270, false, bigPaint);

        bigPaint.setStrokeWidth(15);
        bigPaint.setColor(Color.parseColor("#fcfcfc"));
        canvas.drawArc(rect, 135, progressInt, false, bigPaint);
    }

    public void setProgressInt(int progressInt) {
        open(progressInt);
    }





    public void open(int upRightInt) {
        ValueAnimator progressAnimator = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            progressAnimator = ValueAnimator.ofInt(0, upRightInt);
            progressAnimator.setDuration(1000);
            progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                        progressInt =  (int)animation.getAnimatedValue();
                        Log.e("123",String.valueOf(progressInt));
                        invalidate();
                }
            });
            progressAnimator.start();
        }

    }



}