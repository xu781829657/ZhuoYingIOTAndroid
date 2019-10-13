package com.ouzhongiot.ozapp.tools;

import android.app.Activity;
import android.content.Context;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;

public class ViewUtils {

    /**
     * 获取控件宽度（适用于LinearLayout,View）
     *
     * @param view
     * @return
     */
    public static int getWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        try {
            view.measure(w, h);
        } catch (Exception e) {
            return 0;
        }
        return (view.getMeasuredWidth());
    }

    /**
     * 获取控件高度（适用于LinearLayout,View）
     *
     * @param view
     * @return
     */
    public static int getHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        try {
            view.measure(w, h);
        } catch (Exception e) {
            return 0;
        }
        return (view.getMeasuredHeight());
    }

    /****************************
     * 获取屏幕宽高
     ***************************/
    private static DisplayMetrics displayMetrics = null;

    /**
     * 获取屏幕高
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        if (null == displayMetrics) {
            displayMetrics = new DisplayMetrics();
        }
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenHeight() {
        if (null == displayMetrics) {
            displayMetrics = OZApplication.getInstance().getResources()
                    .getDisplayMetrics();
        }
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕宽
     *
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        if (null == displayMetrics) {
            displayMetrics = new DisplayMetrics();
        }
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenWidth() {
        if (null == displayMetrics) {
            displayMetrics = OZApplication.getInstance().getResources()
                    .getDisplayMetrics();
        }
        return displayMetrics.widthPixels;
    }
    public static float getScreenDensity() {
        if (null == displayMetrics) {
            displayMetrics = OZApplication.getInstance().getResources()
                    .getDisplayMetrics();
        }
        return displayMetrics.density;
    }

    /**************************** 以上是获取屏幕宽高 ***************************/
    /**
     * 手机天线高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = OZApplication.getInstance().getResources()
                    .getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 重置view高度
     *
     * @param view
     */
    public static void resetStatusTitle(View view) {
        int statusHeight = getStatusBarHeight();
        view.setPadding(0, statusHeight, 0, 0);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        if (null == displayMetrics) {
            displayMetrics = OZApplication.getInstance().getResources()
                    .getDisplayMetrics();
        }
        final float scale = displayMetrics.density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将dp类型的尺寸转换成px类型的尺寸
     */
    public static int dip2px(Context context, int dpSize) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        return (int) ((float) dpSize * metrics.density + 0.5);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        if (null == displayMetrics) {
            displayMetrics = OZApplication.getInstance().getResources()
                    .getDisplayMetrics();
        }
        final float scale = displayMetrics.density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 测量TextView 显示Text所需宽度
     *
     * @param textView
     * @param text
     * @return 宽度（PX）
     */
    public static int measureTextWidth(TextView textView, String text) {
        int paddingLeft = textView.getPaddingLeft();
        int paddingRight = textView.getPaddingRight();
        TextPaint paint = textView.getPaint();

        float textLength = paint.measureText(text);
        return (int) textLength + paddingLeft + paddingRight;
    }
}
