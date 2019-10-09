package com.ouzhongiot.ozapp.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/5/13.
 */
public class logindata {

    /**
     * id : 2
     * sn : 100000002
     * phone : 15606787989
     * email :
     * nickname :
     * password : E10ADC3949BA59ABBE56E057F20F883E
     * sex : 2
     * birthdate :
     * headImageId : 37
     * headImageUrl : http://112.124.48.212/image/100000002/1477451263600.jpg
     */

    private DataBean data;
    /**
     * data : {"id":2,"sn":100000002,"phone":"15606787989","email":"","nickname":"","password":"E10ADC3949BA59ABBE56E057F20F883E","sex":2,"birthdate":"","headImageId":37,"headImageUrl":"http://112.124.48.212/image/100000002/1477451263600.jpg"}
     * success : true
     * MessageNotificationActivity : null
     * state : 0
     */

    private boolean success;
    private Object message;
    private int state;

    public static logindata objectFromData(String str) {

        return new Gson().fromJson(str, logindata.class);
    }

    public static logindata objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), logindata.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<logindata> arraylogindataFromData(String str) {

        Type listType = new TypeToken<ArrayList<logindata>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<logindata> arraylogindataFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<logindata>>() {
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
        private int sn;
        private String phone;
        private String email;
        private String nickname;
        private String password;
        private int sex;
        private String birthdate;
        private int headImageId;
        private String headImageUrl;

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

        public int getSn() {
            return sn;
        }

        public void setSn(int sn) {
            this.sn = sn;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(String birthdate) {
            this.birthdate = birthdate;
        }

        public int getHeadImageId() {
            return headImageId;
        }

        public void setHeadImageId(int headImageId) {
            this.headImageId = headImageId;
        }

        public String getHeadImageUrl() {
            return headImageUrl;
        }

        public void setHeadImageUrl(String headImageUrl) {
            this.headImageUrl = headImageUrl;
        }
    }
}

//    /**
//     * userAddress : [{"id":1,"userId":1,"userSn":100000001,"addrProvince":"陕西省","addrCity":"宝鸡市","addrCounty":"扶风县","addrStreet":null,"addrDetail":"粑粑","postcode":555555,"receiverName":"分泌物","receiverPhone":"12345678910","receiverTelCode":null,"receiverTelNum":null,"receiverTelExt":null,"state":1}]
//     * userDevice : [{"id":1,"userId":1,"userSn":100000001,"devId":1,"devSn":"18fe34f56bcc","devTypeId":null,"devTypeSn":"A"}]
//     * deviceType : {"id":1,"typeSn":"A","typeName":"冷风扇","imageId":null,"imageUrl":"http://114.55.5.92:8080/fan/image/A.jpg","brand":"恒马"}
//     * user : {"id":1,"sn":100000001,"phone":"15158888963","email":"Ttghuh@qq.com","nickname":"默默","password":"E10ADC3949BA59ABBE56E057F20F883E","zmoney":null,"sex":1,"birthdate":"2016-01-01","headImageId":10,"headImageUrl":"http://114.55.5.92:8080/fan/image/100000001/20160509094733.jpg","state":null}
//     */
//
//    private DataBean data;
//    /**
//     * data : {"userAddress":[{"id":1,"userId":1,"userSn":100000001,"addrProvince":"陕西省","addrCity":"宝鸡市","addrCounty":"扶风县","addrStreet":null,"addrDetail":"粑粑","postcode":555555,"receiverName":"分泌物","receiverPhone":"12345678910","receiverTelCode":null,"receiverTelNum":null,"receiverTelExt":null,"state":1}],"userDevice":[{"id":1,"userId":1,"userSn":100000001,"devId":1,"devSn":"18fe34f56bcc","devTypeId":null,"devTypeSn":"A"}],"deviceType":{"id":1,"typeSn":"A","typeName":"冷风扇","imageId":null,"imageUrl":"http://114.55.5.92:8080/fan/image/A.jpg","brand":"恒马"},"user":{"id":1,"sn":100000001,"phone":"15158888963","email":"Ttghuh@qq.com","nickname":"默默","password":"E10ADC3949BA59ABBE56E057F20F883E","zmoney":null,"sex":1,"birthdate":"2016-01-01","headImageId":10,"headImageUrl":"http://114.55.5.92:8080/fan/image/100000001/20160509094733.jpg","state":null}}
//     * success : true
//     * MessageNotificationActivity : null
//     * state : 0
//     */
//
//    private boolean success;
//    private Object MessageNotificationActivity;
//    private int state;
//
//    public static logindata objectFromData(String str) {
//
//        return new Gson().fromJson(str, logindata.class);
//    }
//
//    public static logindata objectFromData(String str, String key) {
//
//        try {
//            JSONObject jsonObject = new JSONObject(str);
//
//            return new Gson().fromJson(jsonObject.getString(str), logindata.class);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public static List<logindata> arraylogindataFromData(String str) {
//
//        Type listType = new TypeToken<ArrayList<logindata>>() {
//        }.getType();
//
//        return new Gson().fromJson(str, listType);
//    }
//
//    public static List<logindata> arraylogindataFromData(String str, String key) {
//
//        try {
//            JSONObject jsonObject = new JSONObject(str);
//            Type listType = new TypeToken<ArrayList<logindata>>() {
//            }.getType();
//
//            return new Gson().fromJson(jsonObject.getString(str), listType);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return new ArrayList();
//
//
//    }
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }
//
//    public boolean isSuccess() {
//        return success;
//    }
//
//    public void setSuccess(boolean success) {
//        this.success = success;
//    }
//
//    public Object getMessage() {
//        return MessageNotificationActivity;
//    }
//
//    public void setMessage(Object MessageNotificationActivity) {
//        this.MessageNotificationActivity = MessageNotificationActivity;
//    }
//
//    public int getState() {
//        return state;
//    }
//
//    public void setState(int state) {
//        this.state = state;
//    }
//
//    public static class DataBean {
//        /**
//         * id : 1
//         * typeSn : A
//         * typeName : 冷风扇
//         * imageId : null
//         * imageUrl : http://114.55.5.92:8080/fan/image/A.jpg
//         * brand : 恒马
//         */
//
//        private DeviceTypeBean deviceType;
//        /**
//         * id : 1
//         * sn : 100000001
//         * phone : 15158888963
//         * email : Ttghuh@qq.com
//         * nickname : 默默
//         * password : E10ADC3949BA59ABBE56E057F20F883E
//         * zmoney : null
//         * sex : 1
//         * birthdate : 2016-01-01
//         * headImageId : 10
//         * headImageUrl : http://114.55.5.92:8080/fan/image/100000001/20160509094733.jpg
//         * state : null
//         */
//
//        private UserBean user;
//        /**
//         * id : 1
//         * userId : 1
//         * userSn : 100000001
//         * addrProvince : 陕西省
//         * addrCity : 宝鸡市
//         * addrCounty : 扶风县
//         * addrStreet : null
//         * addrDetail : 粑粑
//         * postcode : 555555
//         * receiverName : 分泌物
//         * receiverPhone : 12345678910
//         * receiverTelCode : null
//         * receiverTelNum : null
//         * receiverTelExt : null
//         * state : 1
//         */
//
//        private List<UserAddressBean> userAddress;
//        /**
//         * id : 1
//         * userId : 1
//         * userSn : 100000001
//         * devId : 1
//         * devSn : 18fe34f56bcc
//         * devTypeId : null
//         * devTypeSn : A
//         */
//
//        private List<UserDeviceBean> userDevice;
//
//        public static DataBean objectFromData(String str) {
//
//            return new Gson().fromJson(str, DataBean.class);
//        }
//
//        public static DataBean objectFromData(String str, String key) {
//
//            try {
//                JSONObject jsonObject = new JSONObject(str);
//
//                return new Gson().fromJson(jsonObject.getString(str), DataBean.class);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        public static List<DataBean> arrayDataBeanFromData(String str) {
//
//            Type listType = new TypeToken<ArrayList<DataBean>>() {
//            }.getType();
//
//            return new Gson().fromJson(str, listType);
//        }
//
//        public static List<DataBean> arrayDataBeanFromData(String str, String key) {
//
//            try {
//                JSONObject jsonObject = new JSONObject(str);
//                Type listType = new TypeToken<ArrayList<DataBean>>() {
//                }.getType();
//
//                return new Gson().fromJson(jsonObject.getString(str), listType);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return new ArrayList();
//
//
//        }
//
//        public DeviceTypeBean getDeviceType() {
//            return deviceType;
//        }
//
//        public void setDeviceType(DeviceTypeBean deviceType) {
//            this.deviceType = deviceType;
//        }
//
//        public UserBean getUser() {
//            return user;
//        }
//
//        public void setUser(UserBean user) {
//            this.user = user;
//        }
//
//        public List<UserAddressBean> getUserAddress() {
//            return userAddress;
//        }
//
//        public void setUserAddress(List<UserAddressBean> userAddress) {
//            this.userAddress = userAddress;
//        }
//
//        public List<UserDeviceBean> getUserDevice() {
//            return userDevice;
//        }
//
//        public void setUserDevice(List<UserDeviceBean> userDevice) {
//            this.userDevice = userDevice;
//        }
//
//        public static class DeviceTypeBean {
//            private int id;
//            private String typeSn;
//            private String typeName;
//            private Object imageId;
//            private String imageUrl;
//            private String brand;
//
//            public static DeviceTypeBean objectFromData(String str) {
//
//                return new Gson().fromJson(str, DeviceTypeBean.class);
//            }
//
//            public static DeviceTypeBean objectFromData(String str, String key) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(str);
//
//                    return new Gson().fromJson(jsonObject.getString(str), DeviceTypeBean.class);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//
//            public static List<DeviceTypeBean> arrayDeviceTypeBeanFromData(String str) {
//
//                Type listType = new TypeToken<ArrayList<DeviceTypeBean>>() {
//                }.getType();
//
//                return new Gson().fromJson(str, listType);
//            }
//
//            public static List<DeviceTypeBean> arrayDeviceTypeBeanFromData(String str, String key) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(str);
//                    Type listType = new TypeToken<ArrayList<DeviceTypeBean>>() {
//                    }.getType();
//
//                    return new Gson().fromJson(jsonObject.getString(str), listType);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                return new ArrayList();
//
//
//            }
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getTypeSn() {
//                return typeSn;
//            }
//
//            public void setTypeSn(String typeSn) {
//                this.typeSn = typeSn;
//            }
//
//            public String getTypeName() {
//                return typeName;
//            }
//
//            public void setTypeName(String typeName) {
//                this.typeName = typeName;
//            }
//
//            public Object getImageId() {
//                return imageId;
//            }
//
//            public void setImageId(Object imageId) {
//                this.imageId = imageId;
//            }
//
//            public String getImageUrl() {
//                return imageUrl;
//            }
//
//            public void setImageUrl(String imageUrl) {
//                this.imageUrl = imageUrl;
//            }
//
//            public String getBrand() {
//                return brand;
//            }
//
//            public void setBrand(String brand) {
//                this.brand = brand;
//            }
//        }
//
//        public static class UserBean {
//            private int id;
//            private int sn;
//            private String phone;
//            private String email;
//            private String nickname;
//            private String password;
//            private Object zmoney;
//            private int sex;
//            private String birthdate;
//            private int headImageId;
//            private String headImageUrl;
//            private Object state;
//
//            public static UserBean objectFromData(String str) {
//
//                return new Gson().fromJson(str, UserBean.class);
//            }
//
//            public static UserBean objectFromData(String str, String key) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(str);
//
//                    return new Gson().fromJson(jsonObject.getString(str), UserBean.class);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//
//            public static List<UserBean> arrayUserBeanFromData(String str) {
//
//                Type listType = new TypeToken<ArrayList<UserBean>>() {
//                }.getType();
//
//                return new Gson().fromJson(str, listType);
//            }
//
//            public static List<UserBean> arrayUserBeanFromData(String str, String key) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(str);
//                    Type listType = new TypeToken<ArrayList<UserBean>>() {
//                    }.getType();
//
//                    return new Gson().fromJson(jsonObject.getString(str), listType);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                return new ArrayList();
//
//
//            }
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public int getSn() {
//                return sn;
//            }
//
//            public void setSn(int sn) {
//                this.sn = sn;
//            }
//
//            public String getPhone() {
//                return phone;
//            }
//
//            public void setPhone(String phone) {
//                this.phone = phone;
//            }
//
//            public String getEmail() {
//                return email;
//            }
//
//            public void setEmail(String email) {
//                this.email = email;
//            }
//
//            public String getNickname() {
//                return nickname;
//            }
//
//            public void setNickname(String nickname) {
//                this.nickname = nickname;
//            }
//
//            public String getPassword() {
//                return password;
//            }
//
//            public void setPassword(String password) {
//                this.password = password;
//            }
//
//            public Object getZmoney() {
//                return zmoney;
//            }
//
//            public void setZmoney(Object zmoney) {
//                this.zmoney = zmoney;
//            }
//
//            public int getSex() {
//                return sex;
//            }
//
//            public void setSex(int sex) {
//                this.sex = sex;
//            }
//
//            public String getBirthdate() {
//                return birthdate;
//            }
//
//            public void setBirthdate(String birthdate) {
//                this.birthdate = birthdate;
//            }
//
//            public int getHeadImageId() {
//                return headImageId;
//            }
//
//            public void setHeadImageId(int headImageId) {
//                this.headImageId = headImageId;
//            }
//
//            public String getHeadImageUrl() {
//                return headImageUrl;
//            }
//
//            public void setHeadImageUrl(String headImageUrl) {
//                this.headImageUrl = headImageUrl;
//            }
//
//            public Object getState() {
//                return state;
//            }
//
//            public void setState(Object state) {
//                this.state = state;
//            }
//        }
//
//        public static class UserAddressBean {
//            private int id;
//            private int userId;
//            private int userSn;
//            private String addrProvince;
//            private String addrCity;
//            private String addrCounty;
//            private Object addrStreet;
//            private String addrDetail;
//            private int postcode;
//            private String receiverName;
//            private String receiverPhone;
//            private Object receiverTelCode;
//            private Object receiverTelNum;
//            private Object receiverTelExt;
//            private int state;
//
//            public static UserAddressBean objectFromData(String str) {
//
//                return new Gson().fromJson(str, UserAddressBean.class);
//            }
//
//            public static UserAddressBean objectFromData(String str, String key) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(str);
//
//                    return new Gson().fromJson(jsonObject.getString(str), UserAddressBean.class);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//
//            public static List<UserAddressBean> arrayUserAddressBeanFromData(String str) {
//
//                Type listType = new TypeToken<ArrayList<UserAddressBean>>() {
//                }.getType();
//
//                return new Gson().fromJson(str, listType);
//            }
//
//            public static List<UserAddressBean> arrayUserAddressBeanFromData(String str, String key) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(str);
//                    Type listType = new TypeToken<ArrayList<UserAddressBean>>() {
//                    }.getType();
//
//                    return new Gson().fromJson(jsonObject.getString(str), listType);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                return new ArrayList();
//
//
//            }
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public int getUserId() {
//                return userId;
//            }
//
//            public void setUserId(int userId) {
//                this.userId = userId;
//            }
//
//            public int getUserSn() {
//                return userSn;
//            }
//
//            public void setUserSn(int userSn) {
//                this.userSn = userSn;
//            }
//
//            public String getAddrProvince() {
//                return addrProvince;
//            }
//
//            public void setAddrProvince(String addrProvince) {
//                this.addrProvince = addrProvince;
//            }
//
//            public String getAddrCity() {
//                return addrCity;
//            }
//
//            public void setAddrCity(String addrCity) {
//                this.addrCity = addrCity;
//            }
//
//            public String getAddrCounty() {
//                return addrCounty;
//            }
//
//            public void setAddrCounty(String addrCounty) {
//                this.addrCounty = addrCounty;
//            }
//
//            public Object getAddrStreet() {
//                return addrStreet;
//            }
//
//            public void setAddrStreet(Object addrStreet) {
//                this.addrStreet = addrStreet;
//            }
//
//            public String getAddrDetail() {
//                return addrDetail;
//            }
//
//            public void setAddrDetail(String addrDetail) {
//                this.addrDetail = addrDetail;
//            }
//
//            public int getPostcode() {
//                return postcode;
//            }
//
//            public void setPostcode(int postcode) {
//                this.postcode = postcode;
//            }
//
//            public String getReceiverName() {
//                return receiverName;
//            }
//
//            public void setReceiverName(String receiverName) {
//                this.receiverName = receiverName;
//            }
//
//            public String getReceiverPhone() {
//                return receiverPhone;
//            }
//
//            public void setReceiverPhone(String receiverPhone) {
//                this.receiverPhone = receiverPhone;
//            }
//
//            public Object getReceiverTelCode() {
//                return receiverTelCode;
//            }
//
//            public void setReceiverTelCode(Object receiverTelCode) {
//                this.receiverTelCode = receiverTelCode;
//            }
//
//            public Object getReceiverTelNum() {
//                return receiverTelNum;
//            }
//
//            public void setReceiverTelNum(Object receiverTelNum) {
//                this.receiverTelNum = receiverTelNum;
//            }
//
//            public Object getReceiverTelExt() {
//                return receiverTelExt;
//            }
//
//            public void setReceiverTelExt(Object receiverTelExt) {
//                this.receiverTelExt = receiverTelExt;
//            }
//
//            public int getState() {
//                return state;
//            }
//
//            public void setState(int state) {
//                this.state = state;
//            }
//        }
//
//        public static class UserDeviceBean {
//            private int id;
//            private int userId;
//            private int userSn;
//            private int devId;
//            private String devSn;
//            private Object devTypeId;
//            private String devTypeSn;
//
//            public static UserDeviceBean objectFromData(String str) {
//
//                return new Gson().fromJson(str, UserDeviceBean.class);
//            }
//
//            public static UserDeviceBean objectFromData(String str, String key) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(str);
//
//                    return new Gson().fromJson(jsonObject.getString(str), UserDeviceBean.class);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//
//            public static List<UserDeviceBean> arrayUserDeviceBeanFromData(String str) {
//
//                Type listType = new TypeToken<ArrayList<UserDeviceBean>>() {
//                }.getType();
//
//                return new Gson().fromJson(str, listType);
//            }
//
//            public static List<UserDeviceBean> arrayUserDeviceBeanFromData(String str, String key) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(str);
//                    Type listType = new TypeToken<ArrayList<UserDeviceBean>>() {
//                    }.getType();
//
//                    return new Gson().fromJson(jsonObject.getString(str), listType);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                return new ArrayList();
//
//
//            }
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public int getUserId() {
//                return userId;
//            }
//
//            public void setUserId(int userId) {
//                this.userId = userId;
//            }
//
//            public int getUserSn() {
//                return userSn;
//            }
//
//            public void setUserSn(int userSn) {
//                this.userSn = userSn;
//            }
//
//            public int getDevId() {
//                return devId;
//            }
//
//            public void setDevId(int devId) {
//                this.devId = devId;
//            }
//
//            public String getDevSn() {
//                return devSn;
//            }
//
//            public void setDevSn(String devSn) {
//                this.devSn = devSn;
//            }
//
//            public Object getDevTypeId() {
//                return devTypeId;
//            }
//
//            public void setDevTypeId(Object devTypeId) {
//                this.devTypeId = devTypeId;
//            }
//
//            public String getDevTypeSn() {
//                return devTypeSn;
//            }
//
//            public void setDevTypeSn(String devTypeSn) {
//                this.devTypeSn = devTypeSn;
//            }
//        }
//    }


