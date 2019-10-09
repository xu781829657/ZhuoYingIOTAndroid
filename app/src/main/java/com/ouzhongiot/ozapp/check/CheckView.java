package com.ouzhongiot.ozapp.check;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

/**
 * Created by liu on 2016/4/24.
 */

public class CheckView extends View {
    Context mContext;
    int [] CheckNum = null;
    Paint mTempPaint = new Paint();
    // 验证码

    public CheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mTempPaint.setAntiAlias(true);
        mTempPaint.setTextSize(Config.TEXT_SIZE);
        mTempPaint.setStrokeWidth(3);
        CheckNum = CheckUtil.getCheckNum();
    }

    public void onDraw(Canvas canvas){
//        canvas.drawColor(getResources().getColor(R.color.green));

//        mTempPaint.setAntiAlias(true);// 设置画笔的锯齿效果



        final int height = getHeight();//获得CheckView控件的高度
        final int width = getWidth();//获得CheckView控件的宽度

        int dx = 20;

        Paint p = new Paint();
        Random random = new Random(555L);
        double c = Math.random()*16777216;

        int d = (int)c;
        String hex = Integer.toHexString(d);
        if(hex.length()!=6){
            hex="22BE64";
        }
        Log.wtf("颜色",hex);
        p.setColor(Color.parseColor("#"+hex));
        canvas.drawText("画圆角矩形:", 10, 260, p);
        RectF oval3 = new RectF(0,0, width, height);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 10, 10,p);//第二个参数是x半径，第三个参数是y半径

        for(int i = 0; i < 4; i ++){//绘制验证控件上的文本
            canvas.drawText("" + CheckNum[i],  dx, CheckUtil.getPositon(height), mTempPaint);
            dx += width/ 5;
        }
        int [] line;
        for(int i = 0; i < Config.LINE_NUM; i ++){//划线
            line = CheckUtil.getLine(height, width);
            canvas.drawLine(line[0], line[1], line[2], line[3], mTempPaint);
        }
        // 绘制小圆点
        int [] point;
        for(int i = 0; i < Config.POINT_NUM; i ++)    {//画点
            point=CheckUtil.getPoint(height, width);
            canvas.drawCircle(point[0], point[1], 1, mTempPaint);
        }
    }

    public void setCheckNum(int [] chenckNum) {//设置验证码
        CheckNum = chenckNum;
    }

    public int[] getCheckNum() {//获得验证码
        return CheckNum;
    }

    public void invaliChenkNum() {
        invalidate();
    }

}