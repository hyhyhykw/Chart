package com.laiki.zanq.chart.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.laiki.zanq.chart.R;
import com.laiki.zanq.chart.utils.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created time : 2017/6/13 11:33.
 *
 * @author HY
 */

public class PieChartView extends ChartView {
    private float arcDiameter;

    private int contentTop;// 顶部的高度
    private Paint textPaint;
    private Paint arcPaint;

    private RectF arcRectF = new RectF();
    private RectF tempRectF = new RectF();

    private boolean isShowText;
    private float startAngle;

    private Place mPlace = Place.CENTER;
    private float sum;

    private enum Place {
        LEFT, RIGHT, TOP, BOTTOM, CENTER
    }

    private List<String> keyList = new ArrayList<>();
    private static final int NOT_SET_DIA = -1;

    private static final int TOP = -1;
    private static final int BOTTOM = -2;
    private static final int CENTER = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;


    private int[] colors = new int[]{
            0xffd667cd,
            0xffb50606,
            0xff8A2BE2,
            0xffffbb33,
            0xff66cc33,
            0xff1122cc,
            0xffff8c00,
            0xff00b810,
            0xff33ccff
    };

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.PieChartView, defStyleAttr, 0);
        int place = ta.getInt(R.styleable.PieChartView_place, CENTER);
        switch (place) {
            case TOP:
                mPlace = Place.TOP;
                break;
            case BOTTOM:
                mPlace = Place.BOTTOM;
                break;
            case LEFT:
                mPlace = Place.LEFT;
                break;
            case RIGHT:
                mPlace = Place.RIGHT;
                break;
            case CENTER:
                mPlace = Place.CENTER;
                break;
        }

        isShowText = ta.getBoolean(R.styleable.PieChartView_showText, true);
        arcDiameter = ta.getDimensionPixelSize(R.styleable.PieChartView_arcDiameter, NOT_SET_DIA);
        startAngle = ta.getFloat(R.styleable.PieChartView_startAngle, -90);
        ta.recycle();
        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
//        setBackgroundColor(Color.BLACK);
        sum = calculateSum();
    }


    public void setContentTop(int contentTop) {
        this.contentTop = contentTop;
        invalidate();
    }

    @Override
    protected void measureComplete() {
        super.measureComplete();
        int min = Math.min(width, height);

        //未设置直径时，给一个默认值
        if (arcDiameter == NOT_SET_DIA) {
            arcDiameter = min - dp2px(20);
        }
        //重新确定位置，防止绘图错误
        if (height != width)
            if (min == height) {
                if (mPlace == Place.TOP || mPlace == Place.BOTTOM) {
                    mPlace = Place.CENTER;
                }
            } else if (min == width) {
                if (mPlace == Place.LEFT || mPlace == Place.RIGHT) {
                    mPlace = Place.CENTER;
                }
            }

        float arcSize = (width - arcDiameter) / 2;
        float arcSizeW = (height - arcDiameter) / 2;

        Logger.e(String.valueOf(arcDiameter));
        switch (mPlace) {
            case TOP:
                arcRectF.set(arcSize, 0, arcSize + arcDiameter, arcDiameter);
                break;
            case BOTTOM:
                arcRectF.set(arcSize, height - arcDiameter, arcSize + arcDiameter, height);
                break;
            case LEFT:
                arcRectF.set(0, arcSizeW, arcDiameter, arcSizeW + arcDiameter);
                break;
            case RIGHT:
                arcRectF.set(width - arcDiameter, arcSizeW, width, arcSizeW + arcDiameter);
                break;
            case CENTER:
                arcRectF.set(arcSize, arcSizeW, arcSize + arcDiameter, arcSizeW + arcDiameter);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startAngle = this.startAngle;
        tempRectF.set(arcRectF);

        float centerX = tempRectF.right - tempRectF.width() / 2;
        float centerY = tempRectF.bottom - tempRectF.height() / 2;

        if (!keyList.isEmpty()) keyList.clear();
        keyList.addAll(getMap().keySet());
        Collections.sort(keyList);

        for (int i = 0; i < keyList.size(); i++) {
            float occPer = getOccupyPercent(keyList.get(i));
            float sweepAngle = occPer * 360;
            Logger.e("startAngle=" + startAngle);
            Logger.e("sweepAngle=" + sweepAngle);
            if (i > colors.length - 1) {
                arcPaint.setColor(colors[(i % 9 + 2) % 9]);
            } else {
                arcPaint.setColor(colors[i]);
            }

            canvas.drawArc(tempRectF, startAngle, sweepAngle, true, arcPaint);

//            if (isShowText) {
//                String text = format(occPer);
////                canvas.save();
////                canvas.rotate(sweepAngle / 2 + startAngle);
////                canvas.restore();
//
//            }
            startAngle = startAngle + sweepAngle;
        }
    }


    private String format(float f) {
        StringBuilder sbl = new StringBuilder();
        sbl.append(f * 100).append("000");
        return sbl.substring(0, 3) + "%";
    }

    private float getOccupyPercent(String key) {
        return getMap().get(key) / sum;
    }

    private float calculateSum() {
        sum = 0;
        for (float num : getMap().values()) {
            sum += num;
        }
        return sum;
    }

    /**
     * 获取状态栏高度
     */
    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int temp = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(temp);
        } catch (Exception e) {
            Logger.e("Exception", e);
        }
        return statusBarHeight;
    }

    /**
     * 获取标题栏的高度
     *
     * @return 标题栏的高度
     */
    private int getTitleBarHeight() {
        return contentTop - getStatusBarHeight();
    }

}
