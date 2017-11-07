package com.ispring.canvasdemo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * 横向的scrollview
 * Created by Administrator on 2016/12/30.
 */

public class MyHorizontalScrollView extends HorizontalScrollView {


    /**
     * ScrollView正在向上滑动
     */
    public static final int SCROLL_UP = 0x01;

    /**
     * ScrollView正在向下滑动
     */
    public static final int SCROLL_DOWN = 0x10;

    /**
     * 最小的滑动距离
     */
    private static final int SCROLLLIMIT = 40;
    private ScrollListener mListener;
    boolean isTouchOrRunning;
    int detalX;
    float lastX;
    float beginx;
    float endx;
    int range = 720;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                beginx = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                isTouchOrRunning = true;
                Log.e(this.getClass() + "ACTION_MOVE", " getScrollX()=" + getScrollX() + "-- ev.getX()" + ev.getX());


                if (getScrollY() != 0) {
                    detalX = 0;
                    lastX = ev.getX();
                } else {
                    detalX = (int) (ev.getX() - lastX);
                    if (detalX > 0) {
                    }
                }

                break;

            case MotionEvent.ACTION_UP:  //抬起手势
                isTouchOrRunning = false;
                Log.e(this.getClass() + "ACTION_UP", " getScrollX()=" + getScrollX() + "-- ev.getX()" + ev.getX());
                endx = ev.getX();
                if (beginx != endx) {
                    if (beginx > endx) {
                        scrollTo(720, 0);
                        return true;
                    } else {
                        scrollTo(0, 0);
                        return true;
                    }
                }


                break;


            case MotionEvent.ACTION_CANCEL://超出布局
                endx = ev.getX();

                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setScrollListener(ScrollListener l) {
        this.mListener = l;
    }


}
