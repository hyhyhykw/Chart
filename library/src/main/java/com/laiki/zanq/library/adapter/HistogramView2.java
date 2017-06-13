package com.laiki.zanq.library.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created time : 2017/6/13 12:49.
 *
 * @author HY
 */

public class HistogramView2 extends View {
    // 数据集合
    private Map<String, Integer> costList = new HashMap<>();
    // 直方图画笔
    private Paint pillarPaint;
    // 文字画笔
    private Paint textPaint;
    // 线 画笔
    private Paint linePaint;
    // 背景颜色
    private static final int BG_COLOR = 0xFFEEEEEE;
    // 背景颜色
    private static final int DEFAULT_TEXT_COLOR = 0xFF666666;
    // 直方图和线的颜色
    private static final int PILLAR_COLOR = Color.LTGRAY;
    // 文字的颜色
    private int textColor;
    int width;// 视图宽度
    int height;// 视图高度

    private int contentTop;// 顶部的高度

    private int pillWidth;// 直方图宽度
    private int dividerWidth;// 分割线尺寸

    //为防止资源占用，使用静态变量
    private static RectF mRectF;//直方图
    private static Rect mRect;//包裹文字的最小矩形
    private static List<String> mKeyList;//横坐标文字

    public HistogramView2(Context context) {
        this(context, null);
    }

    public HistogramView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    abstract class Adapter {
        private DataSetObserver mDataSetObserver;

        public void registerObserver(DataSetObserver observer) {
            mDataSetObserver = observer;
        }

        public void unregisterObserver() {
            mDataSetObserver = null;
        }

        public void notifyDataSetChanged() {
            if (mDataSetObserver != null)
                mDataSetObserver.onChanged();
        }

        public void notifyDataSetInvalidate() {
            if (mDataSetObserver != null)
                mDataSetObserver.onInvalidated();
        }

    }
}
