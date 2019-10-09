package com.ouzhongiot.ozapp.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/5/26.
 */
public class Coldfanashuju {
    /**
     * id : 5
     * devId : 6
     * totalTime : 34863358
     * totalC : 886028
     * waterStateTime : 886028
     * iceCrystalTime : 886028
     * filterDust : 0
     * filterScreenNeat : 0
     */

    private DataBean data;
    /**
     * data : {"id":5,"devId":6,"totalTime":34863358,"totalC":886028,"waterStateTime":886028,"iceCrystalTime":886028,"filterDust":0,"filterScreenNeat":0}
     * success : true
     * MessageNotificationActivity : null
     * state : 0
     */

    private boolean success;
    private Object message;
    private int state;

    public static Coldfanashuju objectFromData(String str) {

        return new Gson().fromJson(str, Coldfanashuju.class);
    }

    public static Coldfanashuju objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), Coldfanashuju.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Coldfanashuju> arrayColdfanashujuFromData(String str) {

        Type listType = new TypeToken<ArrayList<Coldfanashuju>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<Coldfanashuju> arrayColdfanashujuFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Coldfanashuju>>() {
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
        private int totalTime;
        private int totalC;
        private int waterStateTime;
        private int iceCrystalTime;
        private int filterDust;
        private int filterScreenNeat;

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

        public int getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(int totalTime) {
            this.totalTime = totalTime;
        }

        public int getTotalC() {
            return totalC;
        }

        public void setTotalC(int totalC) {
            this.totalC = totalC;
        }

        public int getWaterStateTime() {
            return waterStateTime;
        }

        public void setWaterStateTime(int waterStateTime) {
            this.waterStateTime = waterStateTime;
        }

        public int getIceCrystalTime() {
            return iceCrystalTime;
        }

        public void setIceCrystalTime(int iceCrystalTime) {
            this.iceCrystalTime = iceCrystalTime;
        }

        public int getFilterDust() {
            return filterDust;
        }

        public void setFilterDust(int filterDust) {
            this.filterDust = filterDust;
        }

        public int getFilterScreenNeat() {
            return filterScreenNeat;
        }

        public void setFilterScreenNeat(int filterScreenNeat) {
            this.filterScreenNeat = filterScreenNeat;
        }
    }
}
