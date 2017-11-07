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
import android.view.View;

/**
 * Created by lhy on 2017/11/6.
 */

public class ClockView extends View {
    private RectF rectf;
    private Paint paint;
    private int centerX;
    private int centerY;
    private ValueAnimator clickStart2;
    private int timeValue;


    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        rectf = new RectF();
        rectf.left = 10;
        rectf.top = 10;
        rectf.bottom = 310;
        rectf.right = 310;
        paint = new Paint();


        centerX = 160;
        centerY = 160;

        setMove();

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setMove() {
        clickStart2 = ValueAnimator.ofInt(0, 60);
        clickStart2.setDuration(60000);
        clickStart2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                timeValue = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(320, 320);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //外环
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#2e2e2e"));
        paint.setStrokeWidth(30);
        canvas.drawArc(rectf, 0, 360, false, paint);
        paint.setColor(Color.parseColor("#808080"));
        paint.setStrokeWidth(20);
        canvas.drawArc(rectf, 0, 360, false, paint);


        //画刻度线
        for (int i = 0; i < 60; i++) {

            if (i % 5 == 0) {
                paint.setStrokeWidth(9);
                paint.setColor(Color.parseColor("#f5d747"));
                canvas.drawLine(centerX, centerY - 120, centerX, centerY - 100, paint);
            } else {
                paint.setStrokeWidth(3);
                paint.setColor(Color.parseColor("#f5d747"));
                canvas.drawLine(centerX, centerY - 120, centerX, centerY - 110, paint);
            }

            canvas.rotate(6, centerX, centerY);
        }

        //圆心点
        paint.setColor(Color.parseColor("#f5d747"));
        paint.setStrokeWidth(20);
        canvas.drawCircle(centerX, centerY, 5, paint);


        //分针
        canvas.rotate(6 * timeValue, centerX, centerY);
        paint.setStrokeWidth(5);
        paint.setColor(Color.parseColor("#f5d747"));
        canvas.drawLine(centerX, centerY - 80, centerX, centerY, paint);
        //恢复画布
        canvas.rotate(360 - 6 * timeValue, centerX, centerY);


        //时针
        canvas.rotate(timeValue / 2, centerX, centerY);
        paint.setStrokeWidth(9);
        paint.setColor(Color.parseColor("#f5d747"));
        canvas.drawLine(centerX, centerY - 50, centerX, centerY, paint);

        //恢复画布
        canvas.rotate(360 - timeValue / 2, centerX, centerY);


        if (clickStart2 != null && !clickStart2.isStarted()) {
            clickStart2.start();
        }


    }


    /**
     * 取消
     */
    public void cancel() {
        if (clickStart2 != null) {
            clickStart2.removeAllUpdateListeners();
            clickStart2.cancel();
            clickStart2 = null;
        }
    }


    /**
     * 暂停
     */
    public void stop() {
        if (clickStart2 != null&&clickStart2.isRunning()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                clickStart2.pause();
            }
        }
    }
    /**
     * 恢复
     */
    public void restart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (clickStart2 != null&&clickStart2.isPaused()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    clickStart2.resume();
                }
            }
        }
    }




}
