package com.aige.lovereceiving.myanimated;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class MyAnimated extends View implements ValueAnimator.AnimatorUpdateListener {

    private TextPaint mTextPaint;
    private int mTextSize = 14;
    private int mDotRadius = 0;
    private float mDotSpaceDegree = 0;
    private ValueAnimator mLoadAnimator = null;
    private boolean shouldReBuildAniamtor = false;
    private int headDotIndex = 0;
    private int color = 0xff00FFFF;
    private int maxShowDotNum = 9;
    private boolean decreseColor = false;

    public MyAnimated(Context context) {
        this(context, null);
    }

    public MyAnimated(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    //初始化一些东西
    public MyAnimated(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化画笔
        mTextPaint = initPaint();
        //原点半径
        mDotRadius = 8;
        //原点间距
        mDotSpaceDegree = (float) (Math.PI / 20);
        setClickable(true);//设置单击
        setFocusable(true);//设置焦点
        setFocusableInTouchMode(true);//在触摸模式下设置可对焦 //设置此项true，否则无法滑动
    }
    //当控件的父元素正要放置该控件时调用.父元素会问子控件一个问题，“你想要用多大地方啊？”，设置占用大小
    //这个方法计算实际的View大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //依据specMode的值，（MeasureSpec有3种模式分别是UNSPECIFIED, EXACTLY和AT_MOST）
        //如果是AT_MOST，specSize 代表的是最大可获得的空间；
        //如果是EXACTLY，specSize 代表的是精确的尺寸；
        //如果是UNSPECIFIED，对于控件尺寸来说，没有任何参考意义。

        //获取父组件的布局大小的模式，match_parent为EXACTLY、wrap_content为AT_MOST
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = getPaddingTop() + getPaddingBottom() + 320;
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = getPaddingTop() + getPaddingBottom() + 320;
        }
        //设置View实际的宽和高，该方法只能在onMeasure()中使用
        setMeasuredDimension(widthSize, heightSize);

    }
    //控件大小发生改变时调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        shouldReBuildAniamtor = true;
    }
    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) return;
        //绕中心点的位置
        int centerX = width / 2;
        int centerY = height / 2;
        //绕中心点半径
        int Radius = Math.min(width / 5, height / 5);
        if (Radius == 0) return;
        //保存当前所画图形状态
        int restoreId = canvas.save();
        //绘制在上一个原点的位置基础上计算位置并绘制
        canvas.translate(centerX, centerY);

        float dotDegree = (float) (Math.abs(Math.asin(mDotRadius * 1.0f / Radius)) * 2);

        int dotNum = (int) (2 * Math.PI / (dotDegree + mDotSpaceDegree));  //造成精度损失

        float fixSpaceDotDgree = (float) ((2 * Math.PI - dotNum * (dotDegree + mDotSpaceDegree)) / dotNum); //精度弥补

        int PRadius = Radius - mDotRadius;

        if (maxShowDotNum > dotNum / 2) {
            maxShowDotNum = dotNum / 3;
        }
        for (int i = headDotIndex; i > (headDotIndex - maxShowDotNum); i--) {
            int j = i;
            if (i < 0) {
                j = dotNum + i;
            }
            float netDotDegree = (mDotSpaceDegree + fixSpaceDotDgree + dotDegree) * (j - 1) + dotDegree / 2;
            int cx = (int) (PRadius * Math.cos(netDotDegree));
            int cy = (int) (PRadius * Math.sin(netDotDegree));
            canvas.drawCircle(cx, cy, mDotRadius, mTextPaint);//绘制
        }
        canvas.restoreToCount(restoreId);

        if (shouldReBuildAniamtor) {
            shouldReBuildAniamtor = false;
            startAnimation(dotNum);
        }
    }
    //动画监听器，监听每一帧做什么事
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        headDotIndex = (int) animation.getAnimatedValue();
        invalidate();//重绘，在此调用onDraw()方法绘制
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mLoadAnimator != null) {
            mLoadAnimator.cancel();
        }
        mLoadAnimator = null;
        shouldReBuildAniamtor = true;
    }
    //画笔颜色
    public void setColor(int color) {
        this.color = color;
        if (mTextPaint == null) {
            mTextPaint = initPaint();
        }
        mTextPaint.setColor(color);
    }
    private TextPaint initPaint() {
        // 实例化画笔并打开抗锯齿
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }
    private void startAnimation(int dotNum) {
        //cancel()停止当前帧动画，end()跳到最后一帧再停止动画
        if (mLoadAnimator != null) {
            mLoadAnimator.cancel();
        }
        ValueAnimator animator = ValueAnimator.ofInt(dotNum).setDuration(800);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);//重复次数，无限重复
        animator.setRepeatMode(ValueAnimator.RESTART);//重复模式，重头开始
        animator.addUpdateListener(this);
        mLoadAnimator = animator;
        mLoadAnimator.start();
    }

}