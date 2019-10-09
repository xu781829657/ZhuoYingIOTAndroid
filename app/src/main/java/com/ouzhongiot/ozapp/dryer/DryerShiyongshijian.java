package com.ouzhongiot.ozapp.dryer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/9/1.
 */
public class DryerShiyongshijian {


    /**
     * id : 1
     * devId : 1
     * totalTime : 1000000000
     */

    private DataBean data;
    /**
     * data : {"id":1,"devId":1,"totalTime":1000000000}
     * success : true
     * MessageNotificationActivity : null
     * state : 0
     */

    private boolean success;
    private Object message;
    private int state;

    public static DryerShiyongshijian objectFromData(String str) {

        return new Gson().fromJson(str, DryerShiyongshijian.class);
    }

    public static DryerShiyongshijian objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), DryerShiyongshijian.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<DryerShiyongshijian> arrayDryerShiyongshijianFromData(String str) {

        Type listType = new TypeToken<ArrayList<DryerShiyongshijian>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<DryerShiyongshijian> arrayDryerShiyongshijianFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<DryerShiyongshijian>>() {
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
        private long totalTime;

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

        public long getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(long totalTime) {
            this.totalTime = totalTime;
        }
    }
}
