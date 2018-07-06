package com.wei.rulerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RulerView extends View implements GestureDetector.OnGestureListener {

    private GestureDetector mGestureDetector;

    private Paint paint;

    private int maxNum;

    private int maxRulerWidth;

    private int scaleSpace; //刻度间距，单位为px
    private int scaleWidth; //刻度宽度，单位为px
    private int scaleHeight; //刻度高度，单位为px

    public RulerView(Context context) {
        super(context);

        initView();
    }

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initView();
    }

    private void initView() {
        if (paint == null) {
            paint = new Paint();
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
        }

        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(getContext(), this);
        }

        maxNum = 150;

        scaleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getContext().getResources().getDisplayMetrics());
        scaleWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getContext().getResources().getDisplayMetrics());
        scaleSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getContext().getResources().getDisplayMetrics());


        maxRulerWidth = (scaleSpace + scaleWidth) * maxNum;

    }

    private void drawRuler(Canvas canvas) {

        drawRulerTitle(canvas);

        drawRulerBody(canvas);

    }

    private void drawRulerBody(Canvas canvas) {
        int start_y = 200;
        int currentX = getWidth() / 2;
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int fontHeight = (int) Math.abs(fontMetrics.bottom - fontMetrics.top);

        for (int i = 0; i <= maxNum; i++) {
            Rect rect;
            if (i % 5 == 0 || i == maxNum) {
                rect = new Rect(currentX, start_y, currentX + scaleWidth, start_y + scaleHeight + 40);
                canvas.drawText(String.valueOf(i), rect.left + rect.width() / 2, rect.bottom + fontHeight, paint);
            } else {
                rect = new Rect(currentX, start_y, currentX + scaleWidth, start_y + scaleHeight);
            }

            canvas.drawRect(rect, paint);


            currentX += scaleSpace + scaleWidth;
        }
    }

    private void drawRulerTitle(Canvas canvas) {
        int start_y = 50;
        int currentX = getWidth() / 2;
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int fontHeight = (int) Math.abs(fontMetrics.bottom - fontMetrics.top);

        paint.setTextAlign(Paint.Align.CENTER);
        canvas.save();
        canvas.translate(getScrollX(), 0);
        canvas.drawText("10", currentX + scaleWidth / 2, start_y, paint);

        Rect rect = new Rect(currentX, start_y + fontHeight, currentX + scaleWidth, start_y + fontHeight + scaleHeight);
        canvas.drawRect(rect, paint);

        canvas.restore();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRuler(canvas);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("手势", "onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("手势", "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("手势", "onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("手势", "onScroll");
        scrollBy((int) distanceX, 0);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("手势", "onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("手势", "onFling");
        return false;
    }

    @Override
    public void scrollBy(int x, int y) {
        int temp = getScrollX() + x;
        Log.d("滚动数据", String.valueOf(temp));
        if (temp < maxRulerWidth && temp > 0) {
            super.scrollBy(x, y);
        } else{
            Log.d("最大值", String.valueOf(maxRulerWidth));
        }

    }
}