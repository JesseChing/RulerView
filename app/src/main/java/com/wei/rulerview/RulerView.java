package com.wei.rulerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

public class RulerView extends View implements GestureDetector.OnGestureListener {

    private GestureDetector mGestureDetector;

    private Paint paint;

    private int maxNum;

    private int maxRulerWidth; //最大宽度
    private int maxRulerHeight; //最大高度

    private int scaleSpace; //刻度间距，单位为px
    private int scaleWidth; //刻度宽度，单位为px
    private int scaleHeight; //刻度高度，单位为px

    private int scaleHeightDiffer; //显示刻度高度差

    private float textSize; //字体大小
    private int textColor; //字体颜色

    private int fontHeight;

    private int currentNumber;

    private Scroller mScroller;


    public RulerView(Context context) {
        super(context);

        initView(context, null);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView(context, attrs);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RulerView);
            scaleWidth = (int) typedArray.getDimension(R.styleable.RulerView_scaleWidth, 0);
            scaleHeight = (int) typedArray.getDimension(R.styleable.RulerView_scaleHeight, 0);
            scaleSpace = (int) typedArray.getDimension(R.styleable.RulerView_scaleSpace, 0);
            maxNum = typedArray.getInt(R.styleable.RulerView_maxNumber, 0);
            textSize = typedArray.getDimension(R.styleable.RulerView_textSize, 10);
            textColor = typedArray.getColor(R.styleable.RulerView_textColor, 0);
            typedArray.recycle();
        }

        if (paint == null) {
            paint = new Paint();
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLACK);
            paint.setTextSize(textSize);
        }

        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(getContext(), this);
        }

        if (mScroller == null) {
            mScroller = new Scroller(getContext());
        }

//        maxNum = 150;

//        scaleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getContext().getResources().getDisplayMetrics());
//        scaleWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getContext().getResources().getDisplayMetrics());
//        scaleSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getContext().getResources().getDisplayMetrics());


        maxRulerWidth = (scaleSpace + scaleWidth) * maxNum;

//        maxRulerHeight = ;

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        fontHeight = (int) Math.abs(fontMetrics.bottom - fontMetrics.top);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        int width = 0;
//        int height = 0;
//
//        int minWidth = getMinimumWidth();
//        int minHeight = getMinimumHeight();
//
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//
//        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(widthMeasureSpec, 300);
//        } else {
//            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
//        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void drawRuler(Canvas canvas) {

        drawRulerBody(canvas);

        drawRulerTitle(canvas);

    }

    private void drawRulerBody(Canvas canvas) {
        paint.setColor(Color.BLACK);

        int start_y = 150;
        int currentX = getWidth() / 2;
        paint.setTextAlign(Paint.Align.CENTER);


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
        paint.setColor(textColor);

        String numberStr = String.valueOf(currentNumber);


        int start_y = 0;
        int currentX = getWidth() / 2;
        paint.setTextAlign(Paint.Align.CENTER);

        paint.setTextAlign(Paint.Align.CENTER);
        canvas.save();
        canvas.translate(getScrollX(), 0);
        canvas.drawText(numberStr, currentX + scaleWidth / 2, start_y + fontHeight, paint);

//        Rect rect = new Rect(currentX, start_y + fontHeight, currentX + scaleWidth, start_y + fontHeight + scaleHeight + 300);
//        canvas.drawRect(rect, paint);

        /**
         * 绘制顶部的三角标
         */
        Path path = new Path();
        path.moveTo(currentX, start_y + fontHeight + 20);
        path.lineTo(currentX + scaleWidth, start_y + fontHeight + 20);
        path.lineTo(currentX + (scaleWidth / 2), start_y + fontHeight + 60);
        path.close();
        canvas.drawPath(path, paint);

        canvas.restore();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        drawRuler(canvas);
        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
//        Log.d("手势", "onDown");
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
            int x = mScroller.getCurrX();

            int width = scaleSpace + scaleWidth;
            int mod = x % width;
            int modDx = width - mod;

            if (mod > 0) {
                x = x + modDx;
            }
            currentNumber = x / width;
            scrollTo(x,0);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
//        Log.d("手势", "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
//        Log.d("手势", "onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        Log.d("手势", "onScroll");
        scrollBy((int) distanceX, 0);

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
//        Log.d("手势", "onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (Math.abs(velocityX) > 300) {

            mScroller.fling(getScrollX(), getScrollY(),(int)-velocityX,0,0,maxRulerWidth,0,0);
            Log.d("final X" , mScroller.getFinalX()+"");

            int x = mScroller.getFinalX();
            int width = scaleSpace + scaleWidth;
            int mod = Math.abs(x) % width;
            if (mod > 0) {
                if (velocityX<0) {
                    int modDx = width - mod;
                    x = x + modDx;
                } else {
                    x = x - mod;
                }
            }
            mScroller.setFinalX(x);

            invalidate();

            return true;
        }

        return false;
    }

    @Override
    public void scrollBy(int x, int y) {

        int dx = Math.abs(x);
        int width = scaleSpace + scaleWidth;
        int mod = dx % width;
        int modDx = width - mod;

        if (mod > 0) {
            if (x > 0) {
                dx = x + modDx;
            } else if (x < 0) {
                dx = x - modDx;
            }
        }

        int temp = getScrollX() + dx;

        if (temp <= maxRulerWidth && temp >= 0) {
            currentNumber = temp / (scaleSpace + scaleWidth);
            super.scrollBy(dx, y);
        } else {
            Log.d("最大值", String.valueOf(maxRulerWidth));
        }

    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            currentNumber = mScroller.getCurrX() / (scaleSpace + scaleWidth);
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
