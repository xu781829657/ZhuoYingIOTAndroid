package com.ouzhongiot.ozapp.airclean;

/**
 * Created by liu on 2016/6/22.
 */
public class AircleanOrder {



    public static byte[] GetAircleanOrder(String devSn,String devType,String ordertype,byte order){
        byte[] b =new byte[15];
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
}
