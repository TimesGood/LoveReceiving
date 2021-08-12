package com.aige.lovereceiving.myanimated;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MinSoftLoadingView extends View {
    private int circleCount = 5;
    private Circle[] circles;
    private Paint paint;
    private int width;
    private int height;
    private PointF center;
    private float circleRadius;
    private float[] rotates;
    private float roateRadius;

    public MinSoftLoadingView(Context context) {
        this(context,null);
    }
    /**
     * 在xml布局文件中使用时自动调用
     * @param context
     */
    public MinSoftLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MinSoftLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        rotates = new float[circleCount];
    }

    public MinSoftLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        center = new PointF(width/2.0f,height/2.0f);
        //旋转扫描半径
        roateRadius = Math.min(width,height);
        //圆球最大的半径
        circleRadius = roateRadius/10.0f;
        initCircle();
    }

    private void initCircle() {
        circles = new Circle[circleCount];
        for(int i = 0;i < circleCount;i++) {
            circles[i] = new Circle();
            circles[i].setCenter(center.x, center.y-roateRadius/2+circleRadius);
            circles[i].setRadius(circleRadius-circleRadius*i/5);
        }
        //开始执行动画
        startAnimation();
    }
    private void startAnimation(){
        for(int i = 0;i<circleCount;i++){
            final int index = i;
            ValueAnimator animator = ValueAnimator.ofFloat(0,360);
            animator.setRepeatCount(ValueAnimator.INFINITE);//重复动画
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setDuration(2000);
            animator.setStartDelay(index*100);//每一个随后的延迟时间
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    rotates[index] = (float) animation.getAnimatedValue();
                    initCircle();
                }
            });
            animator.start();
        }
    }
    //绘制图形
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 0;i<circleCount;i++) {
            canvas.save();
            canvas.rotate(rotates[i],center.x,center.y);
            circles[i].draw(canvas,paint);
            canvas.restore();
        }
    }
}
