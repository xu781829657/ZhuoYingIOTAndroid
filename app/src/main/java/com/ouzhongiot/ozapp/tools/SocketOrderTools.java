package com.ouzhongiot.ozapp.tools;

/**
 * @author hxf
 * @date 创建时间: 2016/11/4
 * @Description socket指令工具类
 */

public class SocketOrderTools {
    //HMxxxxN#
    public static byte[] addUserOrder(String sn) {
        //登录接口返回的sn是10进制的数字
        int sn10 = Integer.parseInt(sn);
        //先把10进制的sn10转成16进制字符串
        String sn16 = Integer.toHexString(sn10);
        if (sn16.length() == 7) {
            sn16 = "0" + sn16;
        }
        byte[] b = new byte[8];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = (byte) Integer.parseInt(sn16.substring(0, 2), 16);
        b[3] = (byte) Integer.parseInt(sn16.substring(2, 4), 16);
        b[4] = (byte) Integer.parseInt(sn16.substring(4, 6), 16);
        b[5] = (byte) Integer.parseInt(sn16.substring(6, 8), 16);
        b[6] = 'N';
        b[7] = '#';
        return b;

    }

    //HMxxxxttffffffN#
    public static byte[] addDeviceOrder(String userSn, String deviceTypeSn, String deviceSn) {
        byte[] b = new byte[16];
        b[0] = 'H';
        b[1] = 'M';
        int sn10 = Integer.parseInt(userSn);
        String sn16 = Integer.toHexString(sn10);
        if (sn16.length() == 7) {
            sn16 = "0" + sn16;
        }
        b[2] = (byte) Integer.parseInt(sn16.substring(0, 2), 16);
        b[3] = (byte) Integer.parseInt(sn16.substring(2, 4), 16);
        b[4] = (byte) Integer.parseInt(sn16.substring(4, 6), 16);
        b[5] = (byte) Integer.parseInt(sn16.substring(6, 8), 16);
        b[6] = (byte) Integer.parseInt(deviceTypeSn.substring(0, 2), 16);
        b[7] = (byte) Integer.parseInt(deviceTypeSn.substring(2, 4), 16);
        b[8] = (byte) Integer.parseInt(deviceSn.substring(0, 2), 16);
        b[9] = (byte) Integer.parseInt(deviceSn.substring(2, 4), 16);
        b[10] = (byte) Integer.parseInt(deviceSn.substring(4, 6), 16);
        b[11] = (byte) Integer.parseInt(deviceSn.substring(6, 8), 16);
        b[12] = (byte) Integer.parseInt(deviceSn.substring(8, 10), 16);
        b[13] = (byte) Integer.parseInt(deviceSn.substring(10, 12), 16);
        b[14] = 'N';
        b[15] = '#';
        return b;

    }

    /**
     * HMFFAttffffffw00000000#
     * w：命令返回值的长度（后面跟的0000的长度），协议上是24位  24的16进制是18
     */
    public static byte[] appToServiceOrder(String deviceTypeSn, String deviceSn, int tab, byte order, String time) {
        String[] times = time.split(":");
        String hours = Integer.toHexString(Integer.parseInt(times[0]));
        String mins = Integer.toHexString(Integer.parseInt(times[1]));
        if (hours.length() < 2) {
            hours = "0" + hours;
        }
        if (mins.length() < 2) {
            mins = "0" + mins;
        }

        byte[] b = new byte[39];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = 'F';
        b[3] = 'F';
        b[4] = 'A';
        b[5] = (byte) Integer.parseInt(deviceTypeSn.substring(0, 2), 16);
        b[6] = (byte) Integer.parseInt(deviceTypeSn.substring(2, 4), 16);
        b[7] = (byte) Integer.parseInt(deviceSn.substring(0, 2), 16);
        b[8] = (byte) Integer.parseInt(deviceSn.substring(2, 4), 16);
        b[9] = (byte) Integer.parseInt(deviceSn.substring(4, 6), 16);
        b[10] = (byte) Integer.parseInt(deviceSn.substring(6, 8), 16);
        b[11] = (byte) Integer.parseInt(deviceSn.substring(8, 10), 16);
        b[12] = (byte) Integer.parseInt(deviceSn.substring(10, 12), 16);
        String w24 = Integer.toHexString(24);//18
        b[13] = (byte) Integer.parseInt(w24, 16);
        for (int i = 14; i < 38; i++) {
            if (i == tab) {
                b[i] = order;
            } else {
                b[i] = (byte) 0x00;
            }
            if (i == 28) {
                b[i] = (byte) Integer.parseInt(hours.substring(0, 2), 16);
            }
            if (i == 29) {
                b[i] = (byte) Integer.parseInt(mins.substring(0, 2), 16);
            }

        }
        b[38] = '#';
        if (LogTools.debug){
            LogTools.e("App->服务器发送的命令->"+b.toString());
        }
        return b;

    }

    public static byte[] pm25OpenOrder(String deviceTypeSn, String deviceSn, String time) {
        String[] times = time.split(":");
        String hours = Integer.toHexString(Integer.parseInt(times[0]));
        String mins = Integer.toHexString(Integer.parseInt(times[1]));
        if (hours.length() < 2) {
            hours = "0" + hours;
        }
        if (mins.length() < 2) {
            mins = "0" + mins;
        }

        byte[] b = new byte[39];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = 'F';
        b[3] = 'F';
        b[4] = 'A';
        b[5] = (byte) Integer.parseInt(deviceTypeSn.substring(0, 2), 16);
        b[6] = (byte) Integer.parseInt(deviceTypeSn.substring(2, 4), 16);
        b[7] = (byte) Integer.parseInt(deviceSn.substring(0, 2), 16);
        b[8] = (byte) Integer.parseInt(deviceSn.substring(2, 4), 16);
        b[9] = (byte) Integer.parseInt(deviceSn.substring(4, 6), 16);
        b[10] = (byte) Integer.parseInt(deviceSn.substring(6, 8), 16);
        b[11] = (byte) Integer.parseInt(deviceSn.substring(8, 10), 16);
        b[12] = (byte) Integer.parseInt(deviceSn.substring(10, 12), 16);
        String w24 = Integer.toHexString(24);//18
        b[13] = (byte) Integer.parseInt(w24, 16);
        for (int i = 14; i < 38; i++) {
            b[i] = (byte) 0x00;
            if (i == 14) {
                b[i] = (byte) 0x01;
            }
            if (i == 28) {
                b[i] = (byte) Integer.parseInt(hours.substring(0, 2), 16);
            }
            if (i == 29) {
                b[i] = (byte) Integer.parseInt(mins.substring(0, 2), 16);
            }
            if (i == 31) {
                b[31] = (byte) 0x02;
            }

        }
        b[38] = '#';
        if (LogTools.debug) {
            LogTools.e("App->服务器发送的命令->" + b.toString());
        }
        return b;

    }

    //新风空气净化器：睡眠模式（风速1档  自动关  负离子关）
    public static byte[] xinfengSleepModelOrder(String deviceTypeSn, String deviceSn, String time) {
        String[] times = time.split(":");
        String hours = Integer.toHexString(Integer.parseInt(times[0]));
        String mins = Integer.toHexString(Integer.parseInt(times[1]));
        if (hours.length() < 2) {
            hours = "0" + hours;
        }
        if (mins.length() < 2) {
            mins = "0" + mins;
        }
        byte[] b = new byte[39];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = 'F';
        b[3] = 'F';
        b[4] = 'A';
        b[5] = (byte) Integer.parseInt(deviceTypeSn.substring(0, 2), 16);
        b[6] = (byte) Integer.parseInt(deviceTypeSn.substring(2, 4), 16);
        b[7] = (byte) Integer.parseInt(deviceSn.substring(0, 2), 16);
        b[8] = (byte) Integer.parseInt(deviceSn.substring(2, 4), 16);
        b[9] = (byte) Integer.parseInt(deviceSn.substring(4, 6), 16);
        b[10] = (byte) Integer.parseInt(deviceSn.substring(6, 8), 16);
        b[11] = (byte) Integer.parseInt(deviceSn.substring(8, 10), 16);
        b[12] = (byte) Integer.parseInt(deviceSn.substring(10, 12), 16);
        String w24 = Integer.toHexString(24);//18
        b[13] = (byte) Integer.parseInt(w24, 16);
        b[14] = (byte) 0x00;
        b[15] = (byte) 0x02;
        b[16] = (byte) 0x00;
        b[17] = (byte) 0x01;
        b[18] = (byte) 0x00;
        b[19] = (byte) 0x00;
        b[20] = (byte) 0x01;
        for (int i = 21; i < 38; i++) {
            b[i] = (byte) 0x00;
            if (i == 28) {
                b[i] = (byte) Integer.parseInt(hours.substring(0, 2), 16);
            }
            if (i == 29) {
                b[i] = (byte) Integer.parseInt(mins.substring(0, 2), 16);
            }
        }
        b[38] = '#';
        return b;
    }

    //新风空气净化器 节能模式：负离子关 灯光关闭 手动模式 风速为1
    public static byte[] xinfengNaturalModelOrder(String deviceTypeSn, String deviceSn, String time) {
        String[] times = time.split(":");
        String hours = Integer.toHexString(Integer.parseInt(times[0]));
        String mins = Integer.toHexString(Integer.parseInt(times[1]));
        if (hours.length() < 2) {
            hours = "0" + hours;
        }
        if (mins.length() < 2) {
            mins = "0" + mins;
        }
        byte[] b = new byte[39];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = 'F';
        b[3] = 'F';
        b[4] = 'A';
        b[5] = (byte) Integer.parseInt(deviceTypeSn.substring(0, 2), 16);
        b[6] = (byte) Integer.parseInt(deviceTypeSn.substring(2, 4), 16);
        b[7] = (byte) Integer.parseInt(deviceSn.substring(0, 2), 16);
        b[8] = (byte) Integer.parseInt(deviceSn.substring(2, 4), 16);
        b[9] = (byte) Integer.parseInt(deviceSn.substring(4, 6), 16);
        b[10] = (byte) Integer.parseInt(deviceSn.substring(6, 8), 16);
        b[11] = (byte) Integer.parseInt(deviceSn.substring(8, 10), 16);
        b[12] = (byte) Integer.parseInt(deviceSn.substring(10, 12), 16);
        String w24 = Integer.toHexString(24);//18
        b[13] = (byte) Integer.parseInt(w24, 16);
        b[14] = (byte) 0x00;
        b[15] = (byte) 0x02;
        //关闭灯光
        b[16] = (byte) 0x01;
        //负离子关
        b[17] = (byte) 0x02;
        for (int i = 18; i < 38; i++) {
            b[i] = (byte) 0x00;
            if (i == 20) {
                //风速为1
                b[i] = (byte) 0x01;
            }
            if (i == 28) {
                b[i] = (byte) Integer.parseInt(hours.substring(0, 2), 16);
            }
            if (i == 29) {
                b[i] = (byte) Integer.parseInt(mins.substring(0, 2), 16);
            }
        }
        b[38] = '#';
        return b;
    }

    //新风空气净化器：高效模式（自动关  负离子关  风速4档）
    public static byte[] xinfengEffModelOrder(String deviceTypeSn, String deviceSn, String time) {
        String[] times = time.split(":");
        String hours = Integer.toHexString(Integer.parseInt(times[0]));
        String mins = Integer.toHexString(Integer.parseInt(times[1]));
        if (hours.length() < 2) {
            hours = "0" + hours;
        }
        if (mins.length() < 2) {
            mins = "0" + mins;
        }
        byte[] b = new byte[39];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = 'F';
        b[3] = 'F';
        b[4] = 'A';
        b[5] = (byte) Integer.parseInt(deviceTypeSn.substring(0, 2), 16);
        b[6] = (byte) Integer.parseInt(deviceTypeSn.substring(2, 4), 16);
        b[7] = (byte) Integer.parseInt(deviceSn.substring(0, 2), 16);
        b[8] = (byte) Integer.parseInt(deviceSn.substring(2, 4), 16);
        b[9] = (byte) Integer.parseInt(deviceSn.substring(4, 6), 16);
        b[10] = (byte) Integer.parseInt(deviceSn.substring(6, 8), 16);
        b[11] = (byte) Integer.parseInt(deviceSn.substring(8, 10), 16);
        b[12] = (byte) Integer.parseInt(deviceSn.substring(10, 12), 16);
        String w24 = Integer.toHexString(24);//18
        b[13] = (byte) Integer.parseInt(w24, 16);
        b[14] = (byte) 0x00;
        b[15] = (byte) 0x02;
        b[16] = (byte) 0x00;
        b[17] = (byte) 0x02;
        b[18] = (byte) 0x00;
        b[19] = (byte) 0x00;
        b[20] = (byte) 0x04;
        for (int i = 21; i < 38; i++) {
            b[i] = (byte) 0x00;
            if (i == 28) {
                b[i] = (byte) Integer.parseInt(hours.substring(0, 2), 16);
            }
            if (i == 29) {
                b[i] = (byte) Integer.parseInt(mins.substring(0, 2), 16);
            }

        }
        b[38] = '#';
        return b;
    }

    //新风空气净化器：舒适模式（负离子开  风速2档）
    public static byte[] xinfengComModelOrder(String deviceTypeSn, String deviceSn,String time) {
        String[] times = time.split(":");
        String hours = Integer.toHexString(Integer.parseInt(times[0]));
        String mins = Integer.toHexString(Integer.parseInt(times[1]));
        if (hours.length() < 2) {
            hours = "0" + hours;
        }
        if (mins.length() < 2) {
            mins = "0" + mins;
        }
        byte[] b = new byte[39];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = 'F';
        b[3] = 'F';
        b[4] = 'A';
        b[5] = (byte) Integer.parseInt(deviceTypeSn.substring(0, 2), 16);
        b[6] = (byte) Integer.parseInt(deviceTypeSn.substring(2, 4), 16);
        b[7] = (byte) Integer.parseInt(deviceSn.substring(0, 2), 16);
        b[8] = (byte) Integer.parseInt(deviceSn.substring(2, 4), 16);
        b[9] = (byte) Integer.parseInt(deviceSn.substring(4, 6), 16);
        b[10] = (byte) Integer.parseInt(deviceSn.substring(6, 8), 16);
        b[11] = (byte) Integer.parseInt(deviceSn.substring(8, 10), 16);
        b[12] = (byte) Integer.parseInt(deviceSn.substring(10, 12), 16);
        String w24 = Integer.toHexString(24);//18
        b[13] = (byte) Integer.parseInt(w24, 16);
        b[14] = (byte) 0x00;
        b[15] = (byte) 0x02;
        b[16] = (byte) 0x00;
        b[17] = (byte) 0x01;
        b[18] = (byte) 0x00;
        b[19] = (byte) 0x00;
        b[20] = (byte) 0x02;
        for (int i = 21; i < 38; i++) {
            b[i] = (byte) 0x00;
            if (i == 28) {
                b[i] = (byte) Integer.parseInt(hours.substring(0, 2), 16);
            }
            if (i == 29) {
                b[i] = (byte) Integer.parseInt(mins.substring(0, 2), 16);
            }
        }
        b[38] = '#';
        return b;

    }

    //新风空气净化器：设置定时（小时，分钟）
    public static byte[] xinfengSetTimeOrder(String deviceTypeSn, String deviceSn, byte hour, byte minutes,String time) {
        String[] times = time.split(":");
        String hours = Integer.toHexString(Integer.parseInt(times[0]));
        String mins = Integer.toHexString(Integer.parseInt(times[1]));
        if (hours.length() < 2) {
            hours = "0" + hours;
        }
        if (mins.length() < 2) {
            mins = "0" + mins;
        }
        byte[] b = new byte[39];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = 'F';
        b[3] = 'F';
        b[4] = 'A';
        b[5] = (byte) Integer.parseInt(deviceTypeSn.substring(0, 2), 16);
        b[6] = (byte) Integer.parseInt(deviceTypeSn.substring(2, 4), 16);
        b[7] = (byte) Integer.parseInt(deviceSn.substring(0, 2), 16);
        b[8] = (byte) Integer.parseInt(deviceSn.substring(2, 4), 16);
        b[9] = (byte) Integer.parseInt(deviceSn.substring(4, 6), 16);
        b[10] = (byte) Integer.parseInt(deviceSn.substring(6, 8), 16);
        b[11] = (byte) Integer.parseInt(deviceSn.substring(8, 10), 16);
        b[12] = (byte) Integer.parseInt(deviceSn.substring(10, 12), 16);
        String w24 = Integer.toHexString(24);//18
        b[13] = (byte) Integer.parseInt(w24, 16);
        for (int i = 14; i < 23; i++) {
            b[i] = (byte) 0x00;
        }

        b[23] = hour;
        b[24] = minutes;
        for (int i = 25; i < 38; i++) {
            b[i] = (byte) 0x00;
            if (i == 28) {
                b[i] = (byte) Integer.parseInt(hours.substring(0, 2), 16);
            }
            if (i == 29) {
                b[i] = (byte) Integer.parseInt(mins.substring(0, 2), 16);
            }
        }
        b[38] = '#';
        return b;

    }


    //html嵌入页发送指令
    public static byte[] htmlOrder(String deviceTypeSn, String deviceSn, byte[] order) {
        byte[] b = new byte[14 + order.length + 1];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = 'F';
        b[3] = 'F';
        b[4] = 'A';
        b[5] = (byte) Integer.parseInt(deviceTypeSn.substring(0, 2), 16);
        b[6] = (byte) Integer.parseInt(deviceTypeSn.substring(2, 4), 16);
        b[7] = (byte) Integer.parseInt(deviceSn.substring(0, 2), 16);
        b[8] = (byte) Integer.parseInt(deviceSn.substring(2, 4), 16);
        b[9] = (byte) Integer.parseInt(deviceSn.substring(4, 6), 16);
        b[10] = (byte) Integer.parseInt(deviceSn.substring(6, 8), 16);
        b[11] = (byte) Integer.parseInt(deviceSn.substring(8, 10), 16);
        b[12] = (byte) Integer.parseInt(deviceSn.substring(10, 12), 16);

        String w = Integer.toHexString(order.length);
        b[13] = (byte) Integer.parseInt(w, 16);
        for (int i = 0; i < order.length; i++) {
            b[14 + i] = order[i];
        }
        b[13 + order.length + 1] = '#';
        return b;

    }

    //退出当前设备指令
    public static byte[] quitDevOrder(String userSn, String deviceTypeSn, String deviceSn) {
        byte[] b = new byte[16];
        b[0] = 'H';
        b[1] = 'M';
        //10进制先转成16进制
        String userSn16 = Integer.toHexString(Integer.parseInt(userSn));
        if (userSn16.length() == 7) {
            userSn16 = "0" + userSn16;
        }
        b[2] = (byte) Integer.parseInt(userSn16.substring(0,2),16);
        b[3] = (byte) Integer.parseInt(userSn16.substring(2,4),16);
        b[4] = (byte) Integer.parseInt(userSn16.substring(4,6),16);
        b[5] = (byte) Integer.parseInt(userSn16.substring(6,8),16);
        b[6] = (byte) Integer.parseInt(deviceTypeSn.substring(0, 2), 16);
        b[7] = (byte) Integer.parseInt(deviceTypeSn.substring(2, 4), 16);
        b[8] = (byte) Integer.parseInt(deviceSn.substring(0, 2), 16);
        b[9] = (byte) Integer.parseInt(deviceSn.substring(2, 4), 16);
        b[10] = (byte) Integer.parseInt(deviceSn.substring(4, 6), 16);
        b[11] = (byte) Integer.parseInt(deviceSn.substring(6, 8), 16);
        b[12] = (byte) Integer.parseInt(deviceSn.substring(8, 10), 16);
        b[13] = (byte) Integer.parseInt(deviceSn.substring(10, 12), 16);
        b[14] = 'Q';
        b[15] = '#';
        return b;

    }

    /**
     * HMFFAttffffffw00000000#
     * w：命令返回值的长度（后面跟的0000的长度），协议上是24位  24的16进制是18
     */
    public static byte[] xinFengTime(String deviceTypeSn, String deviceSn, String time) {
        String[] times = time.split(":");
        String hours = Integer.toHexString(Integer.parseInt(times[0]));
        String mins = Integer.toHexString(Integer.parseInt(times[1]));
        if (hours.length() < 2) {
            hours = "0" + hours;
        }
        if (mins.length() < 2) {
            mins = "0" + mins;
        }

        byte[] b = new byte[39];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = 'F';
        b[3] = 'F';
        b[4] = 'A';
        b[5] = (byte) Integer.parseInt(deviceTypeSn.substring(0, 2), 16);
        b[6] = (byte) Integer.parseInt(deviceTypeSn.substring(2, 4), 16);
        b[7] = (byte) Integer.parseInt(deviceSn.substring(0, 2), 16);
        b[8] = (byte) Integer.parseInt(deviceSn.substring(2, 4), 16);
        b[9] = (byte) Integer.parseInt(deviceSn.substring(4, 6), 16);
        b[10] = (byte) Integer.parseInt(deviceSn.substring(6, 8), 16);
        b[11] = (byte) Integer.parseInt(deviceSn.substring(8, 10), 16);
        b[12] = (byte) Integer.parseInt(deviceSn.substring(10, 12), 16);
        String w24 = Integer.toHexString(24);//18
        b[13] = (byte) Integer.parseInt(w24, 16);
        for (int i = 14; i < 38; i++) {
            b[i] = (byte) 0x00;
            if (i == 28) {
                b[i] = (byte) Integer.parseInt(hours.substring(0, 2), 16);
            }
            if (i == 29) {
                b[i] = (byte) Integer.parseInt(mins.substring(0, 2), 16);
            }
        }
        b[38] = '#';
        if (LogTools.debug) {
            LogTools.e("App->服务器发送的命令->" + b.toString());
        }
        return b;

    }




}
