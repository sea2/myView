package com.ispring.canvasdemo.ui;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by lhy on 2017/11/6.
 */

public class ButtonProgress extends View {

    private int progressValue = 0;
    private int radiusValue = 0;
    private RectF progressRectf;
    private Paint progressPaint;
    private int centerX;
    private int centerY;
    private ValueAnimator clickStart;
    private ValueAnimator clickStart2;
    private Paint mPaint;


    public ButtonProgress(Context context) {
        this(context, null);
    }

    public ButtonProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void init() {
        clickStart = ValueAnimator.ofInt(0, 360);
        clickStart.setInterpolator(new DecelerateInterpolator());
        clickStart.setDuration(500);
        clickStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progressValue = (int) animation.getAnimatedValue();
                Log.e(this.getClass().getSimpleName(), "progressValue--" + progressValue);
                postInvalidate();
                if (progressValue == 360)
                    setMove();

            }
        });


        progressRectf = new RectF();
        progressRectf.top = 20;
        progressRectf.left = 20;

        progressPaint = new Paint();
        progressPaint.setColor(Color.parseColor("#789802"));
        progressPaint.setStrokeWidth(10);
        progressPaint.setStyle(Paint.Style.STROKE);

        //文字的画笔
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setTextSize( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30, getResources().getDisplayMetrics()));
        mPaint.setTextAlign(Paint.Align.CENTER);



        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progressValue = 0;
                Log.e(this.getClass().getSimpleName(), "setOnClickListener");
                if (clickStart != null) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    clickStart.start();
                }
            }
        });


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setMove() {
        clickStart2 = ValueAnimator.ofInt(getMeasuredHeight() / 2 - 30, 0);
        clickStart2.setDuration(1000);
        clickStart2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radiusValue = (int) animation.getAnimatedValue();
                Log.e(this.getClass().getSimpleName(), "radiusValue--" + radiusValue);
                postInvalidate();
            }
        });
        clickStart2.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(this.getClass().getSimpleName(), "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        progressRectf.bottom = getMeasuredHeight() - 20;
        progressRectf.right = getMeasuredWidth() - 20;
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
        radiusValue = getMeasuredWidth() / 2 - 30;
        Log.e(this.getClass().getSimpleName(), "onLayout");
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(this.getClass().getSimpleName(), "onDraw");
        if (progressValue < 360) {
            progressPaint.setStyle(Paint.Style.STROKE);
            if (progressValue == 0) {
                progressPaint.setColor(Color.parseColor("#eaeaea"));
                canvas.drawArc(progressRectf, 0, 360, false, progressPaint);
            } else {
                progressPaint.setColor(Color.parseColor("#f5d747"));
                canvas.drawArc(progressRectf, 0, progressValue, false, progressPaint);
            }
        } else if (progressValue == 360) {
            canvas.drawArc(progressRectf, 0, progressValue, false, progressPaint);
            progressPaint.setStyle(Paint.Style.FILL);
            progressPaint.setColor(Color.parseColor("#f5d747"));
            canvas.drawCircle(centerX, centerY, getMeasuredWidth() / 2 - 20, progressPaint);
            progressPaint.setColor(Color.parseColor("#ffffff"));
            canvas.drawCircle(centerX, centerY, radiusValue, progressPaint);

            if (radiusValue == 0) {
                mPaint.setTextAlign(Paint.Align.CENTER);
                Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
                float fontHeight = fontMetrics.bottom - fontMetrics.top;
                float baseY = getHeight() - (getHeight() - fontHeight) / 2 - fontMetrics.bottom;
                canvas.drawText("OK", getWidth() / 2, baseY, mPaint);
            }
        }


    }


}
