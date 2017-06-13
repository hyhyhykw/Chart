package com.laiki.zanq.chart.utils;

import android.util.Log;


/**
 * Created time : 2017/5/10 9:00.
 *
 * @author HY
 */
@SuppressWarnings({"all"})
public class Logger {

    private static final String TAG = Logger.class.getSimpleName();

    private static final boolean IS_DEBUG = true;

    private Logger() {
    }

    private static String addThreadInfo(final String msg) {
        final String threadName = Thread.currentThread().getName();
        final String shortName = threadName.startsWith("OkHttp") ? "OkHttp" : threadName;
        return "[" + shortName + "] " + msg;
    }

    public static void v(final String msg) {
        v(msg, null);
    }

    public static void v(final String msg, final Throwable tr) {
        if (IS_DEBUG) {
            Log.v(TAG, addThreadInfo(msg), tr);
        }
    }

    public static void d(final String msg) {
        d(msg, null);
    }

    public static void d(final String msg, final Throwable tr) {
        if (IS_DEBUG) {
            Log.d(TAG, addThreadInfo(msg), tr);
        }
    }

    public static void i(final String msg) {
        i(msg, null);
    }

    public static void i(final String msg, final Throwable tr) {
        if (IS_DEBUG) {
            Log.i(TAG, addThreadInfo(msg), tr);
        }
    }

    public static void w(final String msg) {
        w(msg, null);
    }

    public static void w(final String msg, final Throwable tr) {
        if (IS_DEBUG) {
            Log.w(TAG, addThreadInfo(msg), tr);
        }
    }

    public static void e(final String msg) {
        e(msg, null);
    }

    public static void e(final String msg, final Throwable tr) {
        if (IS_DEBUG) {
            Log.e(TAG, addThreadInfo(msg), tr);
        }
    }

}
