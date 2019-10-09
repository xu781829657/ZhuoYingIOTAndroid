package com.ouzhongiot.ozapp.others;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by liu on 2016/8/30.
 */
public class TimerTextView extends TextView implements Runnable{

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    private long mday, mhour, mmin, msecond;//天，小时，分钟，秒
    private boolean run=false; //是否启动了

    public void setTimes(long[] times) {
        mday = times[0];
        mhour = times[1];
        mmin = times[2];
        msecond = times[3];

    }

    /**
     * 倒计时计算
     */
    private void ComputeTime() {
        msecond--;
        if (msecond < 0) {
            mmin--;
            msecond = 59;
            if (mmin < 0) {
                mmin = 59;
                mhour--;
                if (mhour < 0) {
                    // 倒计时结束，一天有24个小时
                    mhour = 23;
                    mday--;

                }
            }

        }

    }

    public boolean isRun() {
        return run;
    }

    public void beginRun() {
        this.run = true;
        run();
    }

    public void stopRun(){
        this.run = false;
    }


    @Override
    public void run() {
        //标示已经启动
        if(run){
            ComputeTime();
            String hour = mhour+"";
            String min = mmin+"";
            String second = msecond+"";
            if (mhour<10)
            {
                hour = "0"+mhour;
            }
            if (mmin<10)
            {
                min = "0"+mmin;
            }
            if (msecond<10)
            {
                second = "0"+msecond;
            }
            String strTime=  hour+":"+ min+":"+second;
            if(mhour==0&&mmin==0&&msecond==0){
                this.setText("衣物已烘干");

            }else {
                this.setText(strTime);
                postDelayed(this, 1000);
            }

        }else {
            removeCallbacks(this);
        }
    }

}