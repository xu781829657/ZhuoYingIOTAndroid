package com.ouzhongiot.ozapp.tools;

import android.content.Context;
import android.graphics.Typeface;

/**
 * @date 创建时间: 2017/2/5
 * @author hxf
 * @Description  使用字体图标工具类
 */

public class IconfontTools {
    public static Typeface getTypeface(Context context){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/iconfont.ttf");
        return typeface;
    }
}
