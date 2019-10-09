package com.ouzhongiot.ozapp.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/11/24.
 */
public class MymachineData {

    /**
     * data : [{"typeName":"智能冷风扇","id":1,"typeNumber":null,"devTypeSn":"4131","imageUrl":"http://112.124.48.212/image/device/4131.jpg","brand":"菊花","devSn":"18fe34f56bcc","indexUrl":"http://112.124.48.212/app/4131/index"}]
     * success : true
     * MessageNotificationActivity : null
     * state : 0
     */

    private boolean success;
    private Object message;
    private int state;
    /**
     * typeName : 智能冷风扇
     * id : 1
     * typeNumber : null
     * devTypeSn : 4131
     * imageUrl : http://112.124.48.212/image/device/4131.jpg
     * brand : 菊花
     * devSn : 18fe34f56bcc
     * indexUrl : http://112.124.48.212/app/4131/index
     */

    private List<DataBean> data;

    public static MymachineData objectFromData(String str) {

        return new Gson().fromJson(str, MymachineData.class);
    }

    public static MymachineData objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), MymachineData.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<MymachineData> arrayMymachineDataFromData(String str) {

        Type listType = new TypeToken<ArrayList<MymachineData>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<MymachineData> arrayMymachineDataFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<MymachineData>>() {
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
        private int id;
        private Object typeNumber;
        private String devTypeSn;
        private String imageUrl;
        private String brand;
        private String devSn;
        private String indexUrl;
        private String definedName;



        private String logoUrl;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getTypeNumber() {
            return typeNumber;
        }

        public void setTypeNumber(Object typeNumber) {
            this.typeNumber = typeNumber;
        }

        public String getDevTypeSn() {
            return devTypeSn;
        }

        public void setDevTypeSn(String devTypeSn) {
            this.devTypeSn = devTypeSn;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getDevSn() {
            return devSn;
        }

        public void setDevSn(String devSn) {
            this.devSn = devSn;
        }

        public String getIndexUrl() {
            return indexUrl;
        }

        public void setIndexUrl(String indexUrl) {
            this.indexUrl = indexUrl;
        }
        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getDefinedName() {
            return definedName;
        }

        public void setDefinedName(String definedName) {
            this.definedName = definedName;
        }
    }
}
