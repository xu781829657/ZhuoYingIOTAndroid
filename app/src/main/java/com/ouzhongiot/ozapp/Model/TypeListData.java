package com.ouzhongiot.ozapp.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/12/2.
 */
public class TypeListData {

    /**
     * data : [{"typeName":"空气净化器","imageUrl":"http://112.124.48.212/image/device/4231.jpg","typeSn":"4200"},{"typeName":"干衣机","imageUrl":"http://112.124.48.212/image/device/4331.jpg","typeSn":"4300"}]
     * success : true
     * MessageNotificationActivity : null
     * state : 0
     */

    private boolean success;
    private Object message;
    private int state;
    /**
     * typeName : 空气净化器
     * imageUrl : http://112.124.48.212/image/device/4231.jpg
     * typeSn : 4200
     */

    private List<DataBean> data;

    public static TypeListData objectFromData(String str) {

        return new Gson().fromJson(str, TypeListData.class);
    }

    public static TypeListData objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), TypeListData.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<TypeListData> arrayTypeListDataFromData(String str) {

        Type listType = new TypeToken<ArrayList<TypeListData>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<TypeListData> arrayTypeListDataFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<TypeListData>>() {
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
        private String typeName;
        private String imageUrl;
        private String typeSn;

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

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getTypeSn() {
            return typeSn;
        }

        public void setTypeSn(String typeSn) {
            this.typeSn = typeSn;
        }
    }
}
