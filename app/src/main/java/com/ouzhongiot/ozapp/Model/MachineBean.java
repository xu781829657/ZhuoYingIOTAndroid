package com.ouzhongiot.ozapp.Model;

/**
 * @date 创建时间: 2017/5/11
 * @author hxf
 * @Description 一个设备
 */

import java.io.Serializable;

/**
 * "id": 26,
 * "typeSn": "4131",          类型编号
 * "typeName": "智能冷风扇",   类型名称
 * "typeNumber": null,        厂家出厂编号
 * "imageId": null,
 * "imageUrl": "http://112.124.48.212/image/device/4131.jpg",   图片地址
 * "logoUrl": null,
 * "brand": null,   品牌
 * "protocol": "HMCOLDFANA",   Smartlink协议
 * "bindUrl": "http://114.55.5.92:8080/smarthome/fan/bindDevice",   设备绑定地址
 * "indexUrl": "http://114.55.5.92:8080/smarthome/app/4100/4131",    HTML控制页地址
 * "level": 2,
 * "slType": 1   Smartlink进入方式，1：定时3秒，2：开关3秒，3：wifi3秒
 */
public class MachineBean implements Serializable {
    private String id;
    private String typeSn;
    private String typeName;
    private String typeNumber;
    private String imageId;
    private String imageUrl;
    private String logoUrl;
    private String brand;
    private String protocol;
    private String bindUrl;
    private String indexUrl;
    private String level;
    private String slType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSlType() {
        return slType;
    }

    public void setSlType(String slType) {
        this.slType = slType;
    }
}
