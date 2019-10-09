package com.ouzhongiot.ozapp.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/5/24.
 */
public class ColdfanAdingshi {
    /**
     * data : null
     * success : true
     * MessageNotificationActivity : null
     * state : 0
     */

    private Object data;
    private boolean success;
    private Object message;
    private int state;

    public static ColdfanAdingshi objectFromData(String str) {

        return new Gson().fromJson(str, ColdfanAdingshi.class);
    }

    public static ColdfanAdingshi objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), ColdfanAdingshi.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<ColdfanAdingshi> arrayColdfanAdingshiFromData(String str) {

        Type listType = new TypeToken<ArrayList<ColdfanAdingshi>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<ColdfanAdingshi> arrayColdfanAdingshiFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<ColdfanAdingshi>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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
}
