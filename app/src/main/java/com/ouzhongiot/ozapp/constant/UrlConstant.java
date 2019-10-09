package com.ouzhongiot.ozapp.constant;

/**
 * @author hxf
 * @date 创建时间: 2016/11/2
 * @Description url常量类
 */

public class UrlConstant {
    public static String BASE_URL = "http://114.55.5.92:8080/";
    public static String BASE_URL_NINETY = "http://114.55.5.92:80/";

//        public static String BASE_URL = "http://192.168.1.104:8080/";
    // 查询最新版本号
    public static String QUERY_LATEST_VERSION = BASE_URL + "smarthome/app/queryLatestVersion";
    // 查询新风空气净化器当前状态
    public static String QUERY_XIN_AIR_STATE = BASE_URL + "smarthome/air/queryDeviceState";
    //查询新风净化器的历史记录
    public static String QUERY_XIN_AIR_HISTORY = BASE_URL + "smarthome/air/queryDeviceHistory";
    //设置新风净化器定时任务
    public static String SET_XIN_AIR_TIME = BASE_URL + "smarthome/air/jobTask";
    //查询定时任务
    public static String QYERY_XIN_AIR_TIME = BASE_URL + "smarthome/air/queryJobTask";
    //删除用户下绑定的设备
    public static String DELETE_USER_DEVICE = BASE_URL + "smarthome/userDevice/deleteUserDevice";
    //修改设备名称
    public static String MODIFY_DEVICE_NAME  = BASE_URL + "smarthome/userDevice/modifyUserDevice";
    //查询系统通知
    public static String PUBLIC_MESSAGE = BASE_URL + "smarthome/app/queryPublicMessage";
    //系统通知增加阅读数
    public static String ADD_PUBLIC_MESSAGE_READ = BASE_URL + "smarthome/app/increasePMReadCount";
    //绑定冷风扇
    public static String BIND_FAN_DEVICE = BASE_URL + "smarthome/fan/bindDevice";
    //绑定空气净化器
    public static String BIND_AIR_DEVICE = BASE_URL + "smarthome/air/bindDevice";
    //绑定干衣机
    public static String BIND_DRYER_DEVICE = BASE_URL + "smarthome/dryer/bindDevice";
    //用户注册之前查询手机号是否已经注册
    public static String REGISTER_QUERY_PHONE = BASE_URL + "smarthome/user/queryPhone";
    //发送短信验证码
    public static String REGISTER_SEND_CODE = BASE_URL + "smarthome/user/sendCode";
    //登录
    public static String LOGIN = BASE_URL + "smarthome/user/login";
    //注册登入一体化
    public static String LOGIN_SIMPLE = BASE_URL + "smarthome/user/loginSimple";
    //注册
    public static String REGISTER = BASE_URL + "smarthome/user/register";
    //忘记密码
    public static String MODIFY_PWD = BASE_URL+"smarthome/user/modifyPassword";
    //联系我们
    public static String CONTACT_US = BASE_URL+"smarthome/app/aboutus";
    //查询用户绑定的设备
    public static String QUERY_USER_DEVICE = BASE_URL + "smarthome/userDevice/queryUserDevice";
    //请求天气接口
    public static String QUERY_WEATHER = BASE_URL_NINETY + "adapter/weather/queryWeather";
    //查询B1空气净化器当前状态
    public static String QUERY_STATE_B1 = BASE_URL + "smarthome/air/queryDeviceState";
    //查询B1空气净化器当前数据
    public static String QUERY_DATA_B1 = BASE_URL + "smarthome/air/queryDeviceData";

    //修改头像
    public static String UPLOAD_AVATAR = BASE_URL + "smarthome/user/uploadUserHeadphoto";
    //修改用户信息
    public static String MODIFY_USERINFO = BASE_URL + "smarthome/user/modifyUserInfo";

    //查询所有设备
    public static String QUERY_ALL_MACHINE = BASE_URL + "smarthome/deviceType/queryMoreProduct";
    //查询类型列表
    public static String QUERY_ALL_TYPE = BASE_URL + "smarthome/deviceType/queryTypeList";
    //添加反馈
    public static String ADD_FEEDBACK = BASE_URL + "smarthome/app/addFeedback";
    //查询地址
    public static String QUERY_ADDRESS = BASE_URL + "smarthome/user/queryUserAddress";
    //添加/修改用户地址
    public static String ADD_ADDRESS = BASE_URL + "smarthome/user/modifyUserAddress";
    //新风空气净化器产品说明
    public static String MACHINE_PRODUCT_DES = "http://112.124.48.212/webpage/";


}
