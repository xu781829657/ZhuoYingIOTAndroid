package com.ouzhongiot.ozapp.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/5/18.
 */
public class Danqianzhuangtai {

    /**
     * id : 9
     * devId : 6
     * fSwitch : true
     * fMode : 3
     * fWind : 2
     * fSwing : true
     * fUV : true
     * fLock : null
     * fState : true
     * startTime : 1463544035813
     * stateStartTime : 1463544041869
     * modeNormalStartTime : 0
     */

    private DataBean data;
    /**
     * data : {"id":9,"devId":6,"fSwitch":true,"fMode":3,"fWind":2,"fSwing":true,"fUV":true,"fLock":null,"fState":true,"startTime":1463544035813,"stateStartTime":1463544041869,"modeNormalStartTime":0}
     * success : true
     * MessageNotificationActivity : null
     * state : 0
     */

    private boolean success;
    private Object message;
    private int state;

    public static Danqianzhuangtai objectFromData(String str) {

        return new Gson().fromJson(str, Danqianzhuangtai.class);
    }

    public static Danqianzhuangtai objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), Danqianzhuangtai.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Danqianzhuangtai> arrayDanqianzhuangtaiFromData(String str) {

        Type listType = new TypeToken<ArrayList<Danqianzhuangtai>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<Danqianzhuangtai> arrayDanqianzhuangtaiFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Danqianzhuangtai>>() {
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
        private int fMode;
        private int fWind;
        private boolean fSwing;
        private boolean fUV;
        private Object fLock;
        private boolean fState;
        private long startTime;
        private long stateStartTime;
        private int modeNormalStartTime;

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

        public int getFMode() {
            return fMode;
        }

        public void setFMode(int fMode) {
            this.fMode = fMode;
        }

        public int getFWind() {
            return fWind;
        }

        public void setFWind(int fWind) {
            this.fWind = fWind;
        }

        public boolean isFSwing() {
            return fSwing;
        }

        public void setFSwing(boolean fSwing) {
            this.fSwing = fSwing;
        }

        public boolean isFUV() {
            return fUV;
        }

        public void setFUV(boolean fUV) {
            this.fUV = fUV;
        }

        public Object getFLock() {
            return fLock;
        }

        public void setFLock(Object fLock) {
            this.fLock = fLock;
        }

        public boolean isFState() {
            return fState;
        }

        public void setFState(boolean fState) {
            this.fState = fState;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getStateStartTime() {
            return stateStartTime;
        }

        public void setStateStartTime(long stateStartTime) {
            this.stateStartTime = stateStartTime;
        }

        public int getModeNormalStartTime() {
            return modeNormalStartTime;
        }

        public void setModeNormalStartTime(int modeNormalStartTime) {
            this.modeNormalStartTime = modeNormalStartTime;
        }
    }
}
