package com.ouzhongiot.ozapp.Model;

/**
 * Created by liu on 2016/5/15.
 */
public class UDPsenddata {

    public static String Udpsenddata(String str) {
        String data = "";
        switch (str) {
            case "4131":
                data = "HMCOLDFANA";
                break;
            case "4231":
                data = "HMSMARTB1";
                break;
            default:
                break;
        }
        return data;
    }

}
