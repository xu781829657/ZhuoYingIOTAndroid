package com.ouzhongiot.ozapp.Model;

/**
 * Created by liu on 2016/5/15.
 */
public class UDPreceivedata {

    public static String Udpreceivedata(String str) {
        String data = "";
        switch (str) {
            case "A":
                data = "41";  //  484d412a18fe34f56bcc23
                break;
            default:
                break;
        }
        return data;
    }
}
