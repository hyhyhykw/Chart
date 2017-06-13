package com.laiki.zanq.chart.view;

import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created time : 2017/6/13 11:35.
 *
 * @author HY
 */

public abstract class HistogramViewAdapter extends BaseChartAdapter {
    private Map<String, Integer> data = new HashMap<>();

    private List<Integer> values = new ArrayList<>();
    private List<String> keyList = new ArrayList<>();

    private Rect rect = new Rect();
    private RectF rectf = new RectF();

    public final void setData(Map<String, Integer> data) {
        this.data = data;
    }

    public final Map<String, Integer> getData() {
        return this.data;
    }

    public final int getLineNum() {
        if (data.isEmpty()) return 0;
        if (!values.isEmpty()) {
            values.clear();
            values.addAll(data.values());
            Collections.sort(values);
        }
        int value = values.get(values.size() - 1);
        return value / 100 + 1;
    }

    public final int getPillCount() {
        return data.size();
    }

    public String getXText(int position) {
        if (data.isEmpty()) return "00";
        if (!keyList.isEmpty()) {
            keyList.clear();
            keyList.addAll(data.keySet());
            Collections.sort(keyList);
        }
        return keyList.get(position);
    }

    public String getYText(int position) {
        return String.valueOf((position + 1) * 100);
    }


}
