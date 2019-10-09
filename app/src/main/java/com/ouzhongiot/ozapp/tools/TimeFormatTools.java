package com.ouzhongiot.ozapp.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * @author hxf
 * @date 创建时间: 2017/1/17
 * @Description 时间转化工具类
 */

public class TimeFormatTools {
    /**
     * 毫秒转化成小时分钟
     *
     * @param millisecond
     * @return
     */
    public static String getTimeFromMillisecond(Long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH时mm分");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date(millisecond);
        String timeStr = simpleDateFormat.format(date);
        return timeStr;
    }

}
