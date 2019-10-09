package com.ouzhongiot.ozapp.dryer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/8/25.
 */
public class DryerData {


    /**
     * id : 1
     * devId : 1
     * fSwitch : true
     * fShift : 1
     * fUV : true
     * fAnion : true
     * startTime : null
     */

    private DataBean data;
    /**
     * data : {"id":1,"devId":1,"fSwitch":true,"fShift":1,"fUV":true,"fAnion":true,"startTime":null}
     * success : true
     * MessageNotificationActivity : null
     * state : 0
     */

    private boolean success;
    private Object message;
    private int state;

    public static DryerData objectFromData(String str) {

        return new Gson().fromJson(str, DryerData.class);
    }

    public static DryerData objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), DryerData.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<DryerData> arrayDryerDataFromData(String str) {

        Type listType = new TypeToken<ArrayList<DryerData>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<DryerData> arrayDryerDataFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<DryerData>>() {
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
        private int fShift;
        private boolean fUV;
        private boolean fAnion;
        private Object startTime;

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

        public int getFShift() {
            return fShift;
        }

        public void setFShift(int fShift) {
            this.fShift = fShift;
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

        public Object getStartTime() {
            return startTime;
        }

        public void setStartTime(Object startTime) {
            this.startTime = startTime;
        }
    }
}
