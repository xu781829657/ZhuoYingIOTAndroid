package com.ouzhongiot.ozapp.airclean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/6/22.
 */
public class Aircleanstate {


    /**
     * id : 1
     * devId : 1
     * fSwitch : true
     * fAuto : false
     * fSleep : false
     * fUV : false
     * fAnion : false
     * fWind : 2
     * durTime : null
     * currentC : 0
     * pm25 : 248
     * cleanFilterScreen : 895
     * changeFilterScreen : 1402
     * startTime : 1475994606065
     * light : 1
     */

    private DataBean data;
    /**
     * data : {"id":1,"devId":1,"fSwitch":true,"fAuto":false,"fSleep":false,"fUV":false,"fAnion":false,"fWind":2,"durTime":null,"currentC":"0","pm25":248,"cleanFilterScreen":895,"changeFilterScreen":1402,"startTime":1475994606065,"light":1}
     * success : true
     * MessageNotificationActivity : null
     * state : 0
     */

    private boolean success;
    private Object message;
    private int state;

    public static Aircleanstate objectFromData(String str) {

        return new Gson().fromJson(str, Aircleanstate.class);
    }

    public static Aircleanstate objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), Aircleanstate.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Aircleanstate> arrayAircleanstateFromData(String str) {

        Type listType = new TypeToken<ArrayList<Aircleanstate>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<Aircleanstate> arrayAircleanstateFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Aircleanstate>>() {
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
        private int id;
        private int devId;
        private boolean fSwitch;
        private boolean fAuto;
        private boolean fSleep;
        private boolean fUV;
        private boolean fAnion;
        private int fWind;
        private Object durTime;
        private String currentC;
        private int pm25;
        private int cleanFilterScreen;
        private int changeFilterScreen;
        private long startTime;
        private int light;

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getDevId() {
            return devId;
        }

        public void setDevId(int devId) {
            this.devId = devId;
        }

        public boolean isFSwitch() {
            return fSwitch;
        }

        public void setFSwitch(boolean fSwitch) {
            this.fSwitch = fSwitch;
        }

        public boolean isFAuto() {
            return fAuto;
        }

        public void setFAuto(boolean fAuto) {
            this.fAuto = fAuto;
        }

        public boolean isFSleep() {
            return fSleep;
        }

        public void setFSleep(boolean fSleep) {
            this.fSleep = fSleep;
        }

        public boolean isFUV() {
            return fUV;
        }

        public void setFUV(boolean fUV) {
            this.fUV = fUV;
        }

        public boolean isFAnion() {
            return fAnion;
        }

        public void setFAnion(boolean fAnion) {
            this.fAnion = fAnion;
        }

        public int getFWind() {
            return fWind;
        }

        public void setFWind(int fWind) {
            this.fWind = fWind;
        }

        public Object getDurTime() {
            return durTime;
        }

        public void setDurTime(Object durTime) {
            this.durTime = durTime;
        }

        public String getCurrentC() {
            return currentC;
        }

        public void setCurrentC(String currentC) {
            this.currentC = currentC;
        }

        public int getPm25() {
            return pm25;
        }

        public void setPm25(int pm25) {
            this.pm25 = pm25;
        }

        public int getCleanFilterScreen() {
            return cleanFilterScreen;
        }

        public void setCleanFilterScreen(int cleanFilterScreen) {
            this.cleanFilterScreen = cleanFilterScreen;
        }

        public int getChangeFilterScreen() {
            return changeFilterScreen;
        }

        public void setChangeFilterScreen(int changeFilterScreen) {
            this.changeFilterScreen = changeFilterScreen;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public int getLight() {
            return light;
        }

        public void setLight(int light) {
            this.light = light;
        }
    }
}
