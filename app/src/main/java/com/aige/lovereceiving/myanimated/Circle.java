package com.aige.lovereceiving.myanimated;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Circle {
    private PointF center;
    private float radius;

    public Circle() {
        center = new PointF();
    }
    //设置圆球半径
    public void setRadius(float radius) {
        this.radius = radius;
    }
    //设置中心点
    public void setCenter(float x,float y) {
        center.set(x,y);
    }
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(center.x,center.y,radius,paint);
    }
}
