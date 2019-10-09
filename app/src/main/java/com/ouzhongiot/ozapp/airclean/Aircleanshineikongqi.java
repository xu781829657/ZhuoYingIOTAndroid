package com.ouzhongiot.ozapp.airclean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/9/2.
 */
public class Aircleanshineikongqi {

    /**
     * 10 : 233
     * 12 : 233
     * 14 : 234
     * 16 : 234
     * 18 : 235
     * 20 : 235
     * 22 : 234
     * 0 : 232
     * 2 : 233
     * 4 : 233
     * 6 : 233
     * 8 : 233
     */

    private DataBean data;
    /**
     * data : {"10":233,"12":233,"14":234,"16":234,"18":235,"20":235,"22":234,"0":232,"2":233,"4":233,"6":233,"8":233}
     * success : true
     * MessageNotificationActivity : null
     * state : 0
     */

    private boolean success;
    private Object message;
    private int state;

    public static Aircleanshineikongqi objectFromData(String str) {

        return new Gson().fromJson(str, Aircleanshineikongqi.class);
    }

    public static Aircleanshineikongqi objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), Aircleanshineikongqi.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Aircleanshineikongqi> arrayAircleanshineikongqiFromData(String str) {

        Type listType = new TypeToken<ArrayList<Aircleanshineikongqi>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<Aircleanshineikongqi> arrayAircleanshineikongqiFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Aircleanshineikongqi>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static class DataBean {
        @SerializedName("10")
        private int value10;
        @SerializedName("12")
        private int value12;
        @SerializedName("14")
        private int value14;
        @SerializedName("16")
        private int value16;
        @SerializedName("18")
        private int value18;
        @SerializedName("20")
        private int value20;
        @SerializedName("22")
        private int value22;
        @SerializedName("0")
        private int value0;
        @SerializedName("2")
        private int value2;
        @SerializedName("4")
        private int value4;
        @SerializedName("6")
        private int value6;
        @SerializedName("8")
        private int value8;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public static DataBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), DataBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<DataBean> arrayDataBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<DataBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<DataBean> arrayDataBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<DataBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public int getValue10() {
            return value10;
        }

        public void setValue10(int value10) {
            this.value10 = value10;
        }

        public int getValue12() {
            return value12;
        }

        public void setValue12(int value12) {
            this.value12 = value12;
        }

        public int getValue14() {
            return value14;
        }

        public void setValue14(int value14) {
            this.value14 = value14;
        }

        public int getValue16() {
            return value16;
        }

        public void setValue16(int value16) {
            this.value16 = value16;
        }

        public int getValue18() {
            return value18;
        }

        public void setValue18(int value18) {
            this.value18 = value18;
        }

        public int getValue20() {
            return value20;
        }

        public void setValue20(int value20) {
            this.value20 = value20;
        }

        public int getValue22() {
            return value22;
        }

        public void setValue22(int value22) {
            this.value22 = value22;
        }

        public int getValue0() {
            return value0;
        }

        public void setValue0(int value0) {
            this.value0 = value0;
        }

        public int getValue2() {
            return value2;
        }

        public void setValue2(int value2) {
            this.value2 = value2;
        }

        public int getValue4() {
            return value4;
        }

        public void setValue4(int value4) {
            this.value4 = value4;
        }

        public int getValue6() {
            return value6;
        }

        public void setValue6(int value6) {
            this.value6 = value6;
        }

        public int getValue8() {
            return value8;
        }

        public void setValue8(int value8) {
            this.value8 = value8;
        }
    }
}
