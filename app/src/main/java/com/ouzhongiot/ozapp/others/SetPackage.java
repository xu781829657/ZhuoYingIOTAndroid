package com.ouzhongiot.ozapp.others;

import android.util.Log;

/**
 * Created by liu on 2016/6/8.
 */
public class SetPackage {

//    HMxxxx*#
    public static byte[] GetHeartpackage(String userSn){
        byte[] b = new byte[8];
        int userSn0 = Integer.parseInt(userSn);
        String a = Integer.toHexString(userSn0);
        if (a.length()==7){
            a = "0"+a;
        }
        b[0] = 'H';
        b[1] = 'M';
        b[2] = (byte)Integer.parseInt(a.substring(0, 2), 16);
        b[3] = (byte)Integer.parseInt(a.substring(2, 4), 16);
        b[4] = (byte)Integer.parseInt(a.substring(4, 6), 16);
        b[5] = (byte)Integer.parseInt(a.substring(6, 8), 16);
        b[6] = '*';
        b[7] = '#';
        return b;
    }
    public static byte[] GetGoldFanAOrder(String devSn,String devType,String ordertype,byte order){
        byte[] b = new byte[15];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = 'F';
        b[3] = 'F';
        b[4] =  (byte)Integer.parseInt(devType.substring(0, 2), 16);
        b[5] =  (byte)Integer.parseInt(devType.substring(2, 4), 16);
        b[6] =  (byte)Integer.parseInt(devSn.substring(0, 2), 16);
        b[7] =  (byte)Integer.parseInt(devSn.substring(2, 4), 16);
        b[8] =  (byte)Integer.parseInt(devSn.substring(4, 6), 16);
        b[9] =  (byte)Integer.parseInt(devSn.substring(6, 8), 16);
        b[10] =  (byte)Integer.parseInt(devSn.substring(8, 10), 16);
        b[11] =  (byte)Integer.parseInt(devSn.substring(10, 12), 16);
        b[12] = ordertype.getBytes()[0];
        b[13] = order;
        b[14] = '#';
        return b;
    }
    public static byte[] GetConected(String devSn,String userSn,String devType){
        Log.wtf("这个是建立连接","发送了！！！");
        byte[] b = new byte[16];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = devType.getBytes()[0];
        b[3] = devType.getBytes()[1];
        b[4] =  (byte)Integer.parseInt(devSn.substring(0, 2), 16);
        b[5] =  (byte)Integer.parseInt(devSn.substring(2, 4), 16);
        b[6] =  (byte)Integer.parseInt(devSn.substring(4, 6), 16);
        b[7] =  (byte)Integer.parseInt(devSn.substring(6, 8), 16);
        b[8] =  (byte)Integer.parseInt(devSn.substring(8, 10), 16);
        b[9] =  (byte)Integer.parseInt(devSn.substring(10, 12), 16);
        int userSn0 = Integer.parseInt(userSn);
        String a = Integer.toHexString(userSn0);
        if (a.length()==7){
            a = "0"+a;
        }
        b[10] = (byte)Integer.parseInt(a.substring(0, 2), 16);
        b[11] = (byte)Integer.parseInt(a.substring(2, 4), 16);
        b[12] = (byte)Integer.parseInt(a.substring(4, 6), 16);
        b[13] = (byte)Integer.parseInt(a.substring(6, 8), 16);
        b[14] = 'N';
        b[15] = '#';
        return b;
    }

    public static byte[] GetDryerOrderC1(String devSn,String devType,byte[] order){
        byte[] b = new byte[31];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = 'F';
        b[3] = 'F';
        b[4] = 'A';
        b[5] =  (byte)Integer.parseInt(devType.substring(0, 2), 16);
        b[6] =  (byte)Integer.parseInt(devType.substring(2, 4), 16);
        b[7] =  (byte)Integer.parseInt(devSn.substring(0, 2), 16);
        b[8] =  (byte)Integer.parseInt(devSn.substring(2, 4), 16);
        b[9] =  (byte)Integer.parseInt(devSn.substring(4, 6), 16);
        b[10] =  (byte)Integer.parseInt(devSn.substring(6, 8), 16);
        b[11] =  (byte)Integer.parseInt(devSn.substring(8, 10), 16);
        b[12] =  (byte)Integer.parseInt(devSn.substring(10, 12), 16);
        b[13] = 'W';
        for(int i = 0;i<16;i++)
        {
            b[14+i] = order[i];
        }
        b[30] = '#';
        return b;
    }
//    HMxxxxN#

    public static byte[] GetAddConnected(String userSn){
        byte[] b = new byte[8];
        int userSn0 = Integer.parseInt(userSn);
        String a = Integer.toHexString(userSn0);
        if (a.length()==7){
            a = "0"+a;
        }
        b[0] = 'H';
        b[1] = 'M';
        b[2] = (byte)Integer.parseInt(a.substring(0, 2), 16);
        b[3] = (byte)Integer.parseInt(a.substring(2, 4), 16);
        b[4] = (byte)Integer.parseInt(a.substring(4, 6), 16);
        b[5] = (byte)Integer.parseInt(a.substring(6, 8), 16);
        b[6] = 'N';
        b[7] = '#';
        return b;
    }

    public static byte[] GetMachineQuit(String devSn,String userSn,String devType){
        byte[] b = new byte[16];
        b[0] = 'H';
        b[1] = 'M';
        int userSn0 = Integer.parseInt(userSn);
        String a = Integer.toHexString(userSn0);
        if (a.length()==7){
            a = "0"+a;
        }
        b[2] = (byte)Integer.parseInt(a.substring(0, 2), 16);
        b[3] = (byte)Integer.parseInt(a.substring(2, 4), 16);
        b[4] = (byte)Integer.parseInt(a.substring(4, 6), 16);
        b[5] = (byte)Integer.parseInt(a.substring(6, 8), 16);
        b[6] = devType.getBytes()[0];
        b[7] = devType.getBytes()[1];
        b[8] =  (byte)Integer.parseInt(devSn.substring(0, 2), 16);
        b[9] =  (byte)Integer.parseInt(devSn.substring(2, 4), 16);
        b[10] =  (byte)Integer.parseInt(devSn.substring(4, 6), 16);
        b[11] =  (byte)Integer.parseInt(devSn.substring(6, 8), 16);
        b[12] =  (byte)Integer.parseInt(devSn.substring(8, 10), 16);
        b[13] =  (byte)Integer.parseInt(devSn.substring(10, 12), 16);
        b[14] = 'Q';
        b[15] = '#';
        return b;
    }


    //    HMxxxxttffffffN#
    public static byte[] GetMachineConnected(String devSn,String userSn,String devType){
        byte[] b = new byte[16];
        b[0] = 'H';
        b[1] = 'M';
        int userSn0 = Integer.parseInt(userSn);
        String a = Integer.toHexString(userSn0);
        if (a.length()==7){
            a = "0"+a;
        }
        b[2] = (byte)Integer.parseInt(a.substring(0, 2), 16);
        b[3] = (byte)Integer.parseInt(a.substring(2, 4), 16);
        b[4] = (byte)Integer.parseInt(a.substring(4, 6), 16);
        b[5] = (byte)Integer.parseInt(a.substring(6, 8), 16);
        b[6] = devType.getBytes()[0];
        b[7] = devType.getBytes()[1];
        b[8] =  (byte)Integer.parseInt(devSn.substring(0, 2), 16);
        b[9] =  (byte)Integer.parseInt(devSn.substring(2, 4), 16);
        b[10] =  (byte)Integer.parseInt(devSn.substring(4, 6), 16);
        b[11] =  (byte)Integer.parseInt(devSn.substring(6, 8), 16);
        b[12] =  (byte)Integer.parseInt(devSn.substring(8, 10), 16);
        b[13] =  (byte)Integer.parseInt(devSn.substring(10, 12), 16);
        b[14] = 'N';
        b[15] = '#';
        return b;
    }

    public static byte[] GetHtmlOrder(String devSn, String devType, byte[] order) {
        byte[] b = new byte[31];
        b[0] = 'H';
        b[1] = 'M';
        b[2] = 'F';
        b[3] = 'F';
        b[4] = 'A';
        b[5] =  (byte)Integer.parseInt(devType.substring(0, 2), 16);
        b[6] =  (byte)Integer.parseInt(devType.substring(2, 4), 16);
        b[7] =  (byte)Integer.parseInt(devSn.substring(0, 2), 16);
        b[8] =  (byte)Integer.parseInt(devSn.substring(2, 4), 16);
        b[9] =  (byte)Integer.parseInt(devSn.substring(4, 6), 16);
        b[10] =  (byte)Integer.parseInt(devSn.substring(6, 8), 16);
        b[11] =  (byte)Integer.parseInt(devSn.substring(8, 10), 16);
        b[12] =  (byte)Integer.parseInt(devSn.substring(10, 12), 16);
        b[13] = 'W';
        for (int i = 0; i < order.length; i++)
        {
            b[14 + i] = order[i];
            int iii = order[i] & 0x000000FF;

        }
        b[13 + order.length + 1] = '#';
        return b;
    }
}
