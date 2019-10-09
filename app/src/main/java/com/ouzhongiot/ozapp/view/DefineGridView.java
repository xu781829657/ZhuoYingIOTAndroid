package com.ouzhongiot.ozapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * @Description 自定义GridView
 * @author hxf
 * @date 2016-6-26
 */
public class DefineGridView extends GridView {
    public DefineGridView(Context context) {
        super(context);
    }

    public DefineGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefineGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 禁止滚动
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
