package com.ouzhongiot.ozapp.tools;

import android.content.Context;
import android.util.Log;

public class LogManager {

    public static final String TAG = "wxf";

    public static boolean isLogOpen = false;

    public static void initALog(Context argContext, boolean argIsLogOpen) {

        isLogOpen = argIsLogOpen;

        if (!isLogOpen) {
            return;
        }
    }

    
    public static void i(String msg) {
        if (isLogOpen && null != msg) {
            Log.i(TAG, msg);
        }
    }

    
    public static void i(String tag, String msg) {
        if (isLogOpen && null != msg) {
            Log.i(tag, msg);
        }
    }

    
    public static void d(String msg) {
        if (isLogOpen && null != msg) {
            Log.d(TAG, msg);
        }
    }

    
    public static void d(String tag, String msg) {
        if (isLogOpen && null != msg) {
            Log.d(tag, msg);
        }
    }

    
    public static void w(String msg) {
        if (isLogOpen && null != msg) {
            Log.w(TAG, msg);
        }
    }

    
    public static void w(String tag, String msg) {
        if (isLogOpen && null != msg) {
            Log.w(tag, msg);
        }
    }

    
    public static void e(String msg) {
        if (isLogOpen && null != msg) {
            Log.e(TAG, msg);
        }
    }

    
    public static void e(Throwable throwable) {
        if (isLogOpen && null != throwable) {
            Log.e(TAG, throwable.getMessage());
        }
    }

    
    public static void e(String tag, String msg) {
        if (isLogOpen && null != msg) {
            Log.e(tag, msg);
        }
    }

}
