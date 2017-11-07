package com.ispring.canvasdemo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.ispring.canvasdemo.R;

import java.util.Random;

/**
 * Created by lhy on 2017/4/1.
 */

public class ButtonRandomView extends View {

    private String buttonText = "123";
    private int buttonSize = 12;
    private int buttonColor;
    private Paint mPaint;
    private Rect rect;

    public ButtonRandomView(Context context) {
        this(context, null);
    }

    public ButtonRandomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonRandomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ButtonRandomView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ButtonRandomView_buttonText:
                    buttonText = a.getString(attr);
                    break;
                case R.styleable.ButtonRandomView_buttonColor:
                    // 默认颜色设置为黑色
                    buttonColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ButtonRandomView_buttonSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    buttonSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;

            }

        }
        a.recycle();

        mPaint = new Paint();

        mPaint.setTextSize(buttonSize);
        mPaint.setTextAlign(Paint.Align.CENTER);

        rect = new Rect();
/*        由调用者返回在边界(分配)最小的矩形 *包含所有的字符,隐含原点(0,0)*/
        mPaint.getTextBounds(buttonText, 0, buttonText.length(), rect);

        final Random random = new Random();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonText = String.valueOf(random.nextInt(1000));
                invalidate();
            }
        });

    }


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

            float textWidth = rect.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {

            float textHeight = rect.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);


        mPaint.setColor(buttonColor);

        mPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float baseY = getHeight() - (getHeight() - fontHeight) / 2 - fontMetrics.bottom;

        canvas.drawText(buttonText, getWidth() / 2, baseY, mPaint);


    }


}
