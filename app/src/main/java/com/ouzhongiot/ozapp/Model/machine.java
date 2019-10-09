package com.ouzhongiot.ozapp.Model;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/5/12.
 */
public class machine {

    /**
     * id : 26
     * typeSn : 4131
     * typeName : 智能冷风扇
     * typeNumber : 123
     * imageId : 1
     * imageUrl : http://112.124.48.212/image/device/4131.jpg
     * brand : 菊花
     * protocol : HMCOLDFANA
     * bindUrl : http://192.168.1.104:8080/smarthome/fan/bindDevice
     * indexUrl : 1112
     * level : 2
     */

    private int id;
    private String typeSn;
    private String typeName;
    private String typeNumber;
    private int imageId;
    private String imageUrl;
    private String brand;
    private String protocol;
    private String bindUrl;
    private String indexUrl;
    private int level;


    private int slType;

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    private String logoUrl;
    public static machine objectFromData(String str) {

        return new Gson().fromJson(str, machine.class);
    }

    public static machine objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), machine.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<machine> arraymachineFromData(String str) {

        Type listType = new TypeToken<ArrayList<machine>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<machine> arraymachineFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<machine>>() {
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

    public String getTypeSn() {
        return typeSn;
    }

    public void setTypeSn(String typeSn) {
        this.typeSn = typeSn;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(String typeNumber) {
        this.typeNumber = typeNumber;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getBindUrl() {
        return bindUrl;
    }

    public void setBindUrl(String bindUrl) {
        this.bindUrl = bindUrl;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    public int getSlType() {
        return slType;
    }

    public void setSlType(int slType) {
        this.slType = slType;
    }

}
