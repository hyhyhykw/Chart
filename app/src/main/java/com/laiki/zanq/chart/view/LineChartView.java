package com.laiki.zanq.chart.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.laiki.zanq.chart.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created time : 2017/6/13 11:33.
 *
 * @author HY
 */

public class LineChartView extends View {

    private boolean isShowPoint;
    private Map<String, Float> map = new HashMap<>();

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.LineChartView, defStyleAttr, 0);
        isShowPoint = ta.getBoolean(R.styleable.LineChartView_showPoint, true);
        ta.recycle();
        defaultMap();
    }

    private void defaultMap() {
        map.put("1", 125f);
        map.put("2", 236f);
        map.put("3", 354f);
        map.put("4", 273f);
        map.put("5", 100f);
        map.put("6", 432f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


}
