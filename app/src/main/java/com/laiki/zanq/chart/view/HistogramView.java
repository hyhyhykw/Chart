package com.laiki.zanq.chart.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.laiki.zanq.chart.R;
import com.laiki.zanq.chart.utils.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Create at 2017-6-11 14:00
 * <p>
 * 自定义View ，通过传入一个Map集合画出直方图
 *
 * @author HY
 * @see View
 */
@SuppressWarnings("unused")
public class HistogramView extends View {

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

    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.HistogramView, defStyleAttr, 0);
        textColor = ta.getColor(R.styleable.HistogramView_textColor,
                DEFAULT_TEXT_COLOR);
        int pillColor = ta.getColor(R.styleable.HistogramView_pillColor,
                PILLAR_COLOR);
        int lineColor = ta.getColor(R.styleable.HistogramView_lineColor,
                PILLAR_COLOR);
        pillWidth = ta.getDimensionPixelSize(
                R.styleable.HistogramView_pillWidth, dp2px(9));
        dividerWidth = ta.getDimensionPixelSize(
                R.styleable.HistogramView_dividerWidth, dp2px(1));
        ta.recycle();

        setBackgroundColor(BG_COLOR);
        // 直方图的画笔
        pillarPaint = new Paint();
        pillarPaint.setColor(pillColor);
        pillarPaint.setAntiAlias(true);
        pillarPaint.setStyle(Style.FILL);

        // 线条的画笔
        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(dividerWidth);

        // 文字的画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(sp2px(14));

        mRect = new Rect();
        mRectF = new RectF();
        mKeyList = new ArrayList<>();

        defaultMap();
    }

    private void defaultMap() {
        costList.put("1", 125);
        costList.put("2", 236);
        costList.put("3", 354);
        costList.put("4", 273);
        costList.put("5", 100);
        costList.put("6", 432);
    }


    public void onDestroy() {
        if (null != mRect) {
            mRect.setEmpty();
            mRect = null;
        }
        if (null != mRectF) {
            mRectF.setEmpty();
            mRectF = null;
        }
        if (null != mKeyList) {
            mKeyList.clear();
            mKeyList = null;
        }
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setPillWidth(int pillWidth) {
        this.pillWidth = pillWidth;
    }

    public void setContentTop(int contentTop) {
        this.contentTop = contentTop;
    }

    // 设置数据集合
    public void setCostList(Map<String, Integer> costList) {
        this.costList = costList;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 如果布局里面设置的是固定值,这里取布局里面的固定值;如果设置的是match_parent,则取父布局的大小
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            // 如果布局里面没有设置固定值,这里取布局的宽度
            width = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            // 如果布局里面没有设置固定值,这里取布局的高度的2/5
            height = heightSize * 2 / 5;
        }
        setMeasuredDimension(width, height);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 获取视图里面的坐标
        // 在开始画图的地方对坐标进行重新赋值
        int top = getTop() - getTitleBarHeight();
        int left = getLeft() + dp2px(35);
        int bottom = getBottom() - dp2px(30);
        int right = getRight() - dp2px(35);

        // 纵坐标数量，
        int lineNum = getLineNum();

        // 画图位置的高度
        int viewHeight = bottom - top;

        float textWidth = 0;//文字的宽度
        // 重复绘图
        for (int i = 0; i < lineNum; i++) {

            // 横向的分割线
            /*
              起始和结束x坐标不变 画线时，每次减少 (所在条目/总条目)个单位的距离坐标
             */
            float startY = bottom * (1 - i * 1.0f / lineNum);
            canvas.drawLine(left, startY, right, startY, linePaint);
            // 纵坐标上的文字
            String text = String.valueOf((i + 1) * 100);
            // 测量文字的宽度
            textWidth = textPaint.measureText(text);
//            sizes.add(measureText);

            // 包裹文字的最小矩形，用于确定文字的位置
            textPaint.getTextBounds(text, 0, text.length(), mRect);

            /*
              纵坐标文字，x不变，一直是固定的 横坐标每次需要将分割线的尺寸减去
             */
            int sHeight = viewHeight / lineNum;

            canvas.drawText(text,
                    left + mRect.left,
                    startY - (sHeight - mRect.height()) / 2 - mRect.bottom - dividerWidth,
                    textPaint);
        }

        // 直方图矩形左侧坐标
        int pillarLeft = (int) (left + dp2px(35) + textWidth);

        // Logger.e(String.valueOf(pillarLeft));

        // 获取去除纵坐标文字的视图宽度
        int viewWidth = right - pillarLeft;

        // 获取数据中的所有键，通过键进行排序
        Set<String> keySet = costList.keySet();
        if (!mKeyList.isEmpty()) mKeyList.clear();
        mKeyList.addAll(keySet);
        Collections.sort(mKeyList);

        // 横坐标文字的y坐标
        int keyTextTop = bottom + dp2px(10);
        // 横坐标数量
        int size = costList.size();
        // 绘制横坐标文字以及直方图
        for (int i = 0; i < size; i++) {
            // 横坐标文字
            String keyText = format(mKeyList.get(i));

            textPaint.getTextBounds(keyText, 0, keyText.length(), mRect);
            int tWidth = (int) textPaint.measureText(keyText);

            canvas.drawText(keyText, pillarLeft + mRect.left
                            - (tWidth - pillWidth) / 2 + viewWidth * (i * 1.0f / size),
                    keyTextTop - mRect.top, textPaint);

            // 如果值为0，跳过
            if (costList.get(mKeyList.get(i)) == 0) {
                continue;
            }

            // 直方图的位置及尺寸
            float pLeft = pillarLeft + viewWidth * (i * 1.0f / size);
            float pTop = viewHeight
                    * (1 - costList.get(mKeyList.get(i)) * 1.0f / lineNum / 100);
            float pRight = pLeft + pillWidth * 1.0f;
            mRectF.set(pLeft, pTop, pRight, bottom);
            canvas.drawRect(mRectF, pillarPaint);

        }

    }

    /**
     * 将数字格式化为两位数形式的字符串
     *
     * @param numStr 需要转换的数字
     * @return 格式化好的字符串
     */
    private String format(String numStr) {
        int num = Integer.parseInt(numStr);
        if (num <= 9) {
            return "0" + num;
        } else {
            return "" + num;
        }
    }

    /**
     * 获取线条数
     *
     * @return 线条数
     */
    private int getLineNum() {
        Set<Entry<String, Integer>> entrySet = costList.entrySet();

        List<Entry<String, Integer>> list = new ArrayList<>();
        list.addAll(entrySet);
        int large = list.get(0).getValue();
        for (int i = 1; i < list.size(); i++) {
            Entry<String, Integer> entry = list.get(i);
            large = large > entry.getValue() ? large : entry.getValue();
        }

        if (large % 100 == 0) {
            return large / 100;
        } else {
            return ((large / 100) + 1);
        }
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

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue px值
     * @return dp值
     */
    public int px2dp(float pxValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或转换为，保证尺寸大小不变
     *
     * @param dipValue dp值
     * @return px值
     */
    public int dp2px(float dipValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue px值
     * @return sp值
     */
    public int px2sp(float pxValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue sp值
     * @return px值
     */
    public int sp2px(float spValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
