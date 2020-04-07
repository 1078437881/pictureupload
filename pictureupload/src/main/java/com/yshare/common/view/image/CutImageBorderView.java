package com.yshare.common.view.image;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class CutImageBorderView extends View {
    /**
     * 水平方向与View的边距
     */
    private int mHorizontalPadding;
    /**
     * 垂直方向与View的边距
     */
    private int mVerticalPadding;
    /**
     * 绘制的矩形的宽度
     */
    private int mWidth;
    /**
     * 边框的颜色，默认为白色
     */
    private int mBorderColor = Color.parseColor("#FFFFFF");
    /**
     * 边框的宽度 单位dp
     */
    private int mBorderWidth = 1;

    private Paint mPaint;

    public CutImageBorderView(Context context)
    {
        this(context, null);
    }

    public CutImageBorderView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CutImageBorderView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        mBorderWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
                        .getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        // 计算矩形区域的宽度
        mWidth = getWidth() - 2 * mHorizontalPadding;
        // 计算距离屏幕垂直边界 的边距
        mVerticalPadding = (getHeight() - mWidth) / 2;
        mPaint.setColor(Color.parseColor("#aa000000"));
        mPaint.setStyle(Paint.Style.FILL);
        // 绘制左边1
        canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
        // 绘制右边2
        canvas.drawRect(getWidth() - mHorizontalPadding, 0, getWidth(),
                getHeight(), mPaint);
        // 绘制上边3
        canvas.drawRect(mHorizontalPadding, 0, getWidth() - mHorizontalPadding,
                mVerticalPadding, mPaint);
        // 绘制下边4
        canvas.drawRect(mHorizontalPadding, getHeight() - mVerticalPadding,
                getWidth() - mHorizontalPadding, getHeight(), mPaint);


        RectF rect = new RectF(mHorizontalPadding, mVerticalPadding, getWidth()
                - mHorizontalPadding, getHeight() - mVerticalPadding);
        Path mPath = new Path();
        mPath.moveTo(mHorizontalPadding,mVerticalPadding);
        mPath.lineTo(mHorizontalPadding,getHeight()/2);
        mPath.arcTo(rect,180,90);
        mPath.lineTo(mHorizontalPadding,mVerticalPadding);
        canvas.drawPath(mPath,mPaint);

        mPath.reset();

        mPath.moveTo(getWidth() - mHorizontalPadding,mVerticalPadding);
        mPath.lineTo(getWidth()/2,mVerticalPadding);
        mPath.arcTo(rect,270,90);
        mPath.lineTo(getWidth() - mHorizontalPadding,mVerticalPadding);
        canvas.drawPath(mPath,mPaint);

        mPath.reset();

        mPath.moveTo(getWidth() - mHorizontalPadding,getHeight() - mVerticalPadding);
        mPath.lineTo(getWidth() - mHorizontalPadding,getHeight()/2);
        mPath.arcTo(rect,0,90);
        mPath.lineTo(getWidth() - mHorizontalPadding,getHeight() - mVerticalPadding);
        canvas.drawPath(mPath,mPaint);

        mPath.reset();

        mPath.moveTo(mHorizontalPadding,getHeight() - mVerticalPadding);
        mPath.lineTo(getWidth()/2,getHeight()-mVerticalPadding);
        mPath.arcTo(rect,90,90);
        mPath.lineTo(mHorizontalPadding,getHeight() - mVerticalPadding);
        canvas.drawPath(mPath,mPaint);

        // 绘制外边框
        mPaint.setColor(mBorderColor);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(getWidth()/2,getHeight()/2 ,getWidth()/2 - mHorizontalPadding,mPaint);

    }

    public void setHorizontalPadding(int mHorizontalPadding)
    {
        this.mHorizontalPadding = mHorizontalPadding;
    }
}