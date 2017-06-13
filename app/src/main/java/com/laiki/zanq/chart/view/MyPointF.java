package com.laiki.zanq.chart.view;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created time : 2017/6/13 15:37.
 *
 * @author HY
 */

public class MyPointF extends PointF {
    public MyPointF() {
    }

    public MyPointF(float x, float y) {
        super(x, y);
    }

    public MyPointF(Point p) {
        super(p);
    }

    public float getDistance(PointF pointF) {
        return (float) Math.hypot(pointF.x - this.x, pointF.y - this.y);
    }
}
