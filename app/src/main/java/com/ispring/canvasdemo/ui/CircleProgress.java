package com.ispring.canvasdemo.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by lhy on 2017/11/7.
 */

public class CircleProgress extends View {

    private Paint paint;
    private RectF progressRectf;
    private float progressint = 0;
    private Paint mTextPaint;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        paint = new Paint();


        progressRectf = new RectF();
        progressRectf.top = 10;
        progressRectf.left = 10;


        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(dp2px(getResources(), 16));
        mTextPaint.setColor(Color.RED);
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
        setMeasuredDimension(220, 220);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        progressRectf.right = 210;
        progressRectf.bottom = 210;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.parseColor("#f5d747"));
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2 - 10, paint);


        paint.setStyle(Paint.Style.FILL);


        if (progressint < 0.5) {
            BigDecimal mBigDecimal1 = new BigDecimal(Float.toString(1));
            BigDecimal mBigDecimal = new BigDecimal(Float.toString(2 * progressint));
            BigDecimal mBigDecima2 = mBigDecimal1.subtract(mBigDecimal);
            double doubleEnd = mBigDecima2.doubleValue();
            BigDecimal mBigDecimalAcos = new BigDecimal(Math.acos(doubleEnd));
            BigDecimal mBigDecimal314 = new BigDecimal(Math.PI);
            BigDecimal mBigDecima6 = mBigDecimalAcos.divide(mBigDecimal314, 5);
            float anglesFloat = mBigDecima6.multiply(BigDecimal.valueOf(180)).floatValue();

            // drawArc里的第二个角度是扫描角度
            canvas.drawArc(progressRectf, 90 - anglesFloat, 2 * anglesFloat, false, paint);
        } else if (progressint == 0.5) {
            canvas.drawArc(progressRectf, 0, 180, false, paint);
        } else if (progressint > 0.5) {
            BigDecimal mBigDecimal1 = new BigDecimal(Float.toString(1));
            BigDecimal mBigDecimal = new BigDecimal(Float.toString(2 * progressint));
            BigDecimal mBigDecima2 = mBigDecimal.subtract(mBigDecimal1);
            double doubleEnd = mBigDecima2.doubleValue();
            BigDecimal mBigDecimalAcos = new BigDecimal(Math.acos(doubleEnd));
            BigDecimal mBigDecimal314 = new BigDecimal(Math.PI);
            BigDecimal mBigDecima6 = mBigDecimalAcos.divide(mBigDecimal314, 5);
            float anglesFloat = mBigDecima6.multiply(BigDecimal.valueOf(180)).floatValue();

            // drawArc里的第二个角度是扫描角度
            canvas.drawArc(progressRectf, anglesFloat - 90, 360 - 2 * anglesFloat, false, paint);
        }


        mTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float baseY = getHeight() - (getHeight() - fontHeight) / 2 - fontMetrics.bottom;


        String st;
        if (progressint != 0) {
            float valueFloat = progressint * 100;
            DecimalFormat df = new DecimalFormat(".##");
            st = df.format(valueFloat);
        } else st = String.valueOf(progressint);
        canvas.drawText(st.concat("%"), getWidth() / 2, baseY, mTextPaint);

    }


    public void setProgressint(float progressint) {

        ValueAnimator mValueAnimator = ValueAnimator.ofFloat(0, progressint);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                CircleProgress.this.progressint = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mValueAnimator.setDuration(500);
        mValueAnimator.start();
    }


    public double div(double value1, double value2, int scale) throws IllegalAccessException {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.divide(b2, scale).doubleValue();
    }


    private float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }
}
