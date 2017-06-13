package com.laiki.zanq.chart.view;

import android.database.DataSetObserver;

/**
 * Created time : 2017/6/13 11:30.
 *
 * @author HY
 */

public abstract class BaseChartAdapter {
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

    /**
     * 将数字格式化为两位数形式的字符串
     *
     * @param numStr 需要转换的数字
     * @return 格式化好的字符串
     */
    protected final String format(String numStr) {
        int num = Integer.parseInt(numStr);
        if (num <= 9) {
            return "0" + num;
        } else {
            return "" + num;
        }
    }

}
