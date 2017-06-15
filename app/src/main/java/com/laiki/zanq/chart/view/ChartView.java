package com.laiki.zanq.chart.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.laiki.zanq.chart.utils.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created time : 2017/6/13 17:11.
 *
 * @author HY
 */

@SuppressWarnings("unused")
public abstract class ChartView extends View {

    protected int width;
    protected int height;
    protected Map<String, Float> mMap = new HashMap<>();
    private Context mContext;

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        defaultMap();
    }

    private void defaultMap() {
        mMap.put("1", 125f);
        mMap.put("2", 236f);
        mMap.put("3", 354f);
        mMap.put("4", 273f);
        mMap.put("5", 100f);
        mMap.put("6", 432f);
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
        measureComplete();
    }

    /**
     * 测量完成之后执行的方法
     */
    protected void measureComplete() {
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue px值
     * @return dp值
     */
    protected final int px2dp(float pxValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或转换为，保证尺寸大小不变
     *
     * @param dipValue dp值
     * @return px值
     */
    protected final int dp2px(float dipValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue px值
     * @return sp值
     */
    protected final int px2sp(float pxValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue sp值
     * @return px值
     */
    protected final int sp2px(float spValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public void setMap(Map<String, Float> map) {
        mMap = map;
        postInvalidate();
    }

    public Map<String, Float> getMap() {
        return mMap;
    }

    /**
     * 图表相互转换
     *
     * @param clazz 需要转换的对象
     * @param <T>   装换的类型
     * @return 转换后的图表
     */
    @Nullable
    protected <T extends ChartView> T convertTo(Class<T> clazz) {
        Constructor<T> con = null;
        T t = null;
        try {
            con = clazz.getConstructor(Context.class);
            t = con.newInstance(mContext);
            t.setMap(getMap());
        } catch (NoSuchMethodException e) {
            Logger.e("The constructor:" + clazz.getName() + "( " + Context.class.getName() + " )" + " is not exist!", e);
        } catch (IllegalAccessException e) {
            Logger.e("Access constructor" + con.getName() + " failed,do you set permissions for it?", e);
        } catch (InstantiationException e) {
            Logger.e(e.getMessage());
        } catch (InvocationTargetException e) {
            Logger.e(e.getMessage(), e);
        }
        return t;
    }

}
