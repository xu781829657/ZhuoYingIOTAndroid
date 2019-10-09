package com.ouzhongiot.ozapp.others;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by liu on 2016/10/12.
 */
public class UnchuangtouLinerlayouot extends LinearLayout {


    public UnchuangtouLinerlayouot(Context context) {
        super(context);
    }

    public UnchuangtouLinerlayouot(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnchuangtouLinerlayouot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {

        return super.onInterceptHoverEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(this.getVisibility() == View.VISIBLE)
        {
            return true;
        }
        return super.onTouchEvent(event);
    }
}
