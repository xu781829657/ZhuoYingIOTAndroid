package com.ouzhongiot.ozapp.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/5/20.
 */
public class Address {

    /**
     * id : 4
     * userId : 6
     * userSn : 100000006
     * addrProvince : 安徽省
     * addrCity : 枞阳县
     * addrCounty : 安庆市
     * addrStreet : 斤斤计较
     * addrDetail : null
     * postcode : null
     * receiverName : 斤斤计较
     * receiverPhone : 1556
     * receiverTelCode : null
     * receiverTelNum : null
     * receiverTelExt : null
     * state : 1
     */

    private int id;
    private int userId;
    private int userSn;
    private String addrProvince;
    private String addrCity;
    private String addrCounty;
    private String addrStreet;
    private Object addrDetail;
    private Object postcode;
    private String receiverName;
    private long receiverPhone;
    private Object receiverTelCode;
    private Object receiverTelNum;
    private Object receiverTelExt;
    private int state;

    public static Address objectFromData(String str) {

        return new Gson().fromJson(str, Address.class);
    }

    public static Address objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), Address.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Address> arrayAddressFromData(String str) {

        Type listType = new TypeToken<ArrayList<Address>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<Address> arrayAddressFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Address>>() {
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserSn() {
        return userSn;
    }

    public void setUserSn(int userSn) {
        this.userSn = userSn;
    }

    public String getAddrProvince() {
        return addrProvince;
    }

    public void setAddrProvince(String addrProvince) {
        this.addrProvince = addrProvince;
    }

    public String getAddrCity() {
        return addrCity;
    }

    public void setAddrCity(String addrCity) {
        this.addrCity = addrCity;
    }

    public String getAddrCounty() {
        return addrCounty;
    }

    public void setAddrCounty(String addrCounty) {
        this.addrCounty = addrCounty;
    }

    public String getAddrStreet() {
        return addrStreet;
    }

    public void setAddrStreet(String addrStreet) {
        this.addrStreet = addrStreet;
    }

    public Object getAddrDetail() {
        return addrDetail;
    }

    public void setAddrDetail(Object addrDetail) {
        this.addrDetail = addrDetail;
    }

    public Object getPostcode() {
        return postcode;
    }

    public void setPostcode(Object postcode) {
        this.postcode = postcode;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public long getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(long receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public Object getReceiverTelCode() {
        return receiverTelCode;
    }

    public void setReceiverTelCode(Object receiverTelCode) {
        this.receiverTelCode = receiverTelCode;
    }

    public Object getReceiverTelNum() {
        return receiverTelNum;
    }

    public void setReceiverTelNum(Object receiverTelNum) {
        this.receiverTelNum = receiverTelNum;
    }

    public Object getReceiverTelExt() {
        return receiverTelExt;
    }

    public void setReceiverTelExt(Object receiverTelExt) {
        this.receiverTelExt = receiverTelExt;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
