package com.ouzhongiot.ozapp.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/5/27.
 */
public class Lishijilu {
    /**
     * id : 16
     * devId : 6
     * useTime : 714
     * useDate : 2016-05-22
     */

    private int id;
    private int devId;
    private int useTime;
    private String useDate;

    public static Lishijilu objectFromData(String str) {

        return new Gson().fromJson(str, Lishijilu.class);
    }

    public static Lishijilu objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), Lishijilu.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Lishijilu> arrayLishijiluFromData(String str) {

        Type listType = new TypeToken<ArrayList<Lishijilu>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<Lishijilu> arrayLishijiluFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Lishijilu>>() {
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
