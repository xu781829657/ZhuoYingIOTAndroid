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
public class LishijiluBean {

    /**
     * id : 2
     * devId : 1
     * useTime : 20000000
     * useDate : 2016-08-29
     */

    private int id;
    private int devId;
    private int useTime;
    private String useDate;

    public static LishijiluBean objectFromData(String str) {

        return new Gson().fromJson(str, LishijiluBean.class);
    }

    public static LishijiluBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), LishijiluBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<LishijiluBean> arrayLishijiluBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<LishijiluBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<LishijiluBean> arrayLishijiluBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<LishijiluBean>>() {
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

    public int getUseTime() {
        return useTime;
    }

    public void setUseTime(int useTime) {
        this.useTime = useTime;
    }

    public String getUseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }
}
