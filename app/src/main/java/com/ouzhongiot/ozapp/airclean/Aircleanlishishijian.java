package com.ouzhongiot.ozapp.airclean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/6/25.
 */
public class Aircleanlishishijian {

    /**
     * data : [{"id":1,"devId":1,"useTime":6300000000,"useDate":"2016-06-18"},{"id":2,"devId":1,"useTime":560000000,"useDate":"2016-06-19"},{"id":3,"devId":1,"useTime":365245236,"useDate":"2016-06-20"},{"id":4,"devId":1,"useTime":20123032,"useDate":"2016-06-21"},{"id":5,"devId":1,"useTime":46465654,"useDate":"2016-06-22"},{"id":6,"devId":1,"useTime":32653202,"useDate":"2016-06-23"},{"id":7,"devId":1,"useTime":23021525,"useDate":"2016-06-24"}]
     * success : true
     * MessageNotificationActivity : null
     * state : 0
     */

    private boolean success;
    private Object message;
    private int state;
    /**
     * id : 1
     * devId : 1
     * useTime : 6300000000
     * useDate : 2016-06-18
     */

    private List<DataBean> data;

    public static Aircleanlishishijian objectFromData(String str) {

        return new Gson().fromJson(str, Aircleanlishishijian.class);
    }

    public static Aircleanlishishijian objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), Aircleanlishishijian.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Aircleanlishishijian> arrayAircleanlishishijianFromData(String str) {

        Type listType = new TypeToken<ArrayList<Aircleanlishishijian>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<Aircleanlishishijian> arrayAircleanlishishijianFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Aircleanlishishijian>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private int devId;
        private long useTime;
        private String useDate;

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

        public long getUseTime() {
            return useTime;
        }

        public void setUseTime(long useTime) {
            this.useTime = useTime;
        }

        public String getUseDate() {
            return useDate;
        }

        public void setUseDate(String useDate) {
            this.useDate = useDate;
        }
    }
}
