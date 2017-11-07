package com.ispring.canvasdemo.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.ispring.canvasdemo.R;

/**
 * colorful arc progress bar
 * Created by shinelw on 12/4/15.
 */
public class ColorArcProgressBar extends View {

    private int mWidth;
    private int mHeight;

    //外弧直径
    private int diameter;

    //圆心
    private float centerX;
    private float centerY;

    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;

    private RectF bgRect;

    private ValueAnimator progressAnimator;

    private float startAngle = 135;
    private float sweepAngle = 270;
    private float currentAngle = 0;
    private float lastAngle;
    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private float maxValues = 60;
    private float curValues = 0;
    private float bgArcWidth = dipToPx(2);

    private float innerArcWidth = dipToPx(2);

    //进度宽度
    private float progressWidth = dipToPx(10);
    private float textSize = dipToPx(60);
    private float hintSize = dipToPx(15);
    private float curSpeedSize = dipToPx(13);
    private int aniSpeed = 1000;

    //刻度线长度
    private float longdegree = dipToPx(5);
    private float shortdegree = dipToPx(3);

    //内弧和外弧的距离
    private int innerPosition = dipToPx(25);
    //刻度线和圆弧的距离
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(20);

    //单位颜色
    private String hintColor = "#8ebfff";

    //刻度线颜色
    private String longDegreeColor = "#d2f2ff";
    private String shortDegreeColor = "#d2f2ff";

    //外弧背景
    private String bgArcColor = "#d2f2ff";
    private boolean isShowCurrentSpeed = true;
    private String hintString = "Km/h";
    private boolean isNeedTitle;
    private boolean isNeedUnit;
    private boolean isNeedDial;
    private boolean isNeedContent;
    private String titleString;


    // sweepAngle / maxValues 的值
    private float k;
    private RectF bgRectInner;
    private Paint innerArcPaint;

    public ColorArcProgressBar(Context context) {
        super(context, null);
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initCofig(context, attrs);
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     * 初始化布局配置
     *
     * @param context
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorArcProgressBar);
        int color1 = a.getColor(R.styleable.ColorArcProgressBar_front_color1, Color.GREEN);
        int color2 = a.getColor(R.styleable.ColorArcProgressBar_front_color2, color1);
        int color3 = a.getColor(R.styleable.ColorArcProgressBar_front_color3, color1);
        colors = new int[]{color1, color2, color3, color3};
        a.getResourceId(R.styleable.ColorArcProgressBar_image, R.drawable.icon_add_lines_paint_image);
        sweepAngle = a.getInteger(R.styleable.ColorArcProgressBar_total_engle, 270);
        bgArcWidth = a.getDimension(R.styleable.ColorArcProgressBar_back_width, dipToPx(2));
        progressWidth = a.getDimension(R.styleable.ColorArcProgressBar_front_width, dipToPx(10));
        isNeedTitle = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_title, false);
        isNeedContent = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_content, false);
        isNeedUnit = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_unit, false);
        isNeedDial = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_dial, false);
        hintString = a.getString(R.styleable.ColorArcProgressBar_string_unit);
        titleString = a.getString(R.styleable.ColorArcProgressBar_string_title);
        curValues = a.getFloat(R.styleable.ColorArcProgressBar_current_value, 0);
        maxValues = a.getFloat(R.styleable.ColorArcProgressBar_max_value, 60);
        setCurrentValues(curValues);
        setMaxValues(maxValues);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        int height = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        setMeasuredDimension(width, height);
    }

    private void initView() {


        diameter = 3 * getScreenWidth() / 5;
        //弧形的矩阵区域
        bgRect = new RectF();
        bgRect.top = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);

        //内弧形的矩阵区域
        bgRectInner = new RectF();
        bgRectInner.top = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE + innerPosition;
        bgRectInner.left = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE + innerPosition;
        bgRectInner.right = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE) - innerPosition;
        bgRectInner.bottom = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE) - innerPosition;


        //圆心
        centerX = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;
        centerY = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(Color.parseColor(bgArcColor));


        //外弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(Color.parseColor(bgArcColor));
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //内弧画笔
        innerArcPaint = new Paint();
        innerArcPaint.setAntiAlias(true);
        innerArcPaint.setStyle(Paint.Style.STROKE);
        innerArcPaint.setStrokeWidth(innerArcWidth);
        innerArcPaint.setColor(Color.parseColor("#ffffff"));
        innerArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(Color.WHITE);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //显示单位文字
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(Color.parseColor(hintColor));
        hintPaint.setTextAlign(Paint.Align.CENTER);

        //显示标题文字
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(Color.parseColor(hintColor));
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        //抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        if (isNeedDial) {
            //画刻度线
            for (int i = 0; i < 60; i++) {
                if (i > 21 && i < 39) {
                    canvas.rotate(6, centerX, centerY);
                    continue;
                }
                /* if (i % 5 == 0) {
                    degreePaint.setStrokeWidth(dipToPx(2.4f));
                    degreePaint.setColor(Color.parseColor(bgArcColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 + DEGREE_PROGRESS_DISTANCE, centerX, centerY - diameter / 2 - progressWidth / 2 + DEGREE_PROGRESS_DISTANCE - longdegree, degreePaint);
              } else { }*/
                degreePaint.setStrokeWidth(dipToPx(2.4f));
                degreePaint.setColor(Color.parseColor(shortDegreeColor));
                canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 + DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2, centerX, centerY - diameter / 2 - progressWidth / 2 + DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2 - shortdegree, degreePaint);


                canvas.rotate(6, centerX, centerY);
            }
        }


        //指针
        Bitmap mImage = ((BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.icon_add_lines_paint_image)).getBitmap();
        Rect rect = new Rect();
        rect.top = (int) centerX;
        rect.left = (int) (centerY - diameter / 2 - progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);
        rect.right = (int) (centerY - diameter / 2 - progressWidth / 2 + DEGREE_PROGRESS_DISTANCE) + dipToPx(18);
        rect.bottom = (int) centerX + dipToPx(18);

        // currentAngle

        float rotateFloat = -startAngle + 103 + currentAngle;
        Log.e("tag", rotateFloat + "----" + startAngle + "----" + currentAngle);
        canvas.rotate(rotateFloat, centerX, centerY);
        canvas.drawBitmap(mImage, null, rect, allArcPaint);

        canvas.rotate(360 - rotateFloat, centerX, centerY);



        //外弧
        canvas.drawArc(bgRect, startAngle + 10, sweepAngle - 20, false, allArcPaint);

        //内弧
        canvas.drawArc(bgRectInner, startAngle, sweepAngle, false, innerArcPaint);

        //设置渐变色
        SweepGradient sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(130, centerX, centerY);
        sweepGradient.setLocalMatrix(matrix);
        progressPaint.setShader(sweepGradient);

        //当前进度
        canvas.drawArc(bgRect, startAngle + 10, currentAngle, false, progressPaint);

        if (isNeedContent) {
            canvas.drawText(String.format("%.0f", curValues), centerX, centerY + textSize / 3, vTextPaint);
        }
        if (isNeedUnit) {
            canvas.drawText(hintString, centerX, centerY + 2 * textSize / 3, hintPaint);
        }
        if (isNeedTitle) {
            canvas.drawText(titleString, centerX, centerY - 2 * textSize / 3, curSpeedPaint);
        }


    }

    /**
     * 设置最大值
     *
     * @param maxValues
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        k = sweepAngle / maxValues;
    }

    /**
     * 设置当前值
     *
     * @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        this.curValues = currentValues;
        lastAngle = currentAngle;
        setAnimation(lastAngle, currentValues * k, aniSpeed);

    }

    /**
     * 设置整个圆弧宽度
     *
     * @param bgArcWidth
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**
     * 设置进度宽度
     *
     * @param progressWidth
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置速度文字大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置单位文字大小
     *
     * @param hintSize
     */
    public void setHintSize(int hintSize) {
        this.hintSize = hintSize;
    }

    /**
     * 设置单位文字
     *
     * @param hintString
     */
    public void setUnit(String hintString) {
        this.hintString = hintString;
        invalidate();
    }

    /**
     * 设置直径大小
     *
     * @param diameter
     */
    public void setDiameter(int diameter) {
        this.diameter = dipToPx(diameter);
    }

    /**
     * 为进度设置动画
     *
     * @param last
     * @param current
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle = (float) animation.getAnimatedValue();
                curValues = currentAngle / k;
                postInvalidate();
            }
        });
        progressAnimator.start();
    }


    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setIsShowCurrentSpeed(boolean isShowCurrentSpeed) {
        this.isShowCurrentSpeed = isShowCurrentSpeed;
    }


}
