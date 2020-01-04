package com.miyatu.mirror.util;

import android.util.Log;

public class LogUtils {
    private static final String TAG = "wangchao";
    private static boolean isDebug = true;

    public static void i(Object object) {
        if (isDebug)
            Log.i(TAG, getString(object));
    }

    public static void d(Object object) {
        if (isDebug)
            Log.d(TAG, getString(object));
    }

    public static void e(Object object) {
        if (isDebug)
            Log.e(TAG, getString(object));
    }

    public static void w(Object object) {
        if (isDebug)
            Log.w(TAG, getString(object));
    }

    private static String getString(Object object) {
        if (object != null && !object.equals("")) {
            return object.toString();
        }
        return "null";
    }
}
