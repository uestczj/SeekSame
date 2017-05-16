package com.example.caizejian.seeksamehobbies.utils;

import android.util.Log;

/**
 * Created by caizejian on 2017/3/29.
 */

public class LogUtils {
    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static boolean isDebug = true;

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void i(Object object, String msg) {
        if (isDebug) {
            Log.i(object.getClass().getSimpleName(), msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void e(Object object, String msg) {
        if (isDebug) {
            Log.e(object.getClass().getSimpleName(), msg);
        }
    }

}
