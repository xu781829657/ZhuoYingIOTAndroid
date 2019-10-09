package com.ouzhongiot.ozapp.constant;

/**
 * @date 创建时间: 2017/2/13
 * @author hxf
 * @Description 存在本地的sp key常量
 */

public class SpConstant {
    //系统通知最新时间
    public static final String SYSTEM_NOTI_NEWEST_TIME = "system_noti_newest_time";
    //登录信息
    public static final String LOGIN_USERNAME = "username";//用户名
    public static final String LOGIN_PWD = "password";//密码
    public static final String LOGIN_USERSN = "userSn";//用户sn
    public static final String LOGIN_PHONE = "phone";//手机
    public static final String LOGIN_ID = "userId";//用户id
    public static final String LOGIN_EMAIL = "email";//邮箱
    public static final String LOGIN_NICKNAME = "nickname";//昵称
    public static final String LOGIN_SEX = "sex";//性别
    public static final String LOGIN_BIRTHDATE = "birthdate";//生日
    public static final String LOGIN_HEADURL = "headImageUrl";//头像url
    //头像名称时间戳
    public static String HEAD_FILE_NAME_TIME = "head_file_name_time";

    //城市位置
    public static final String LOCATION_PROVINCE = "province";//省
    public static final String LOCATION_CITY = "city";//市
    public static final String LOCATION_DISTRICT = "district";//区
    //天气信息
    public static final String WEATHER_INFO = "weatherinfo";//天气
    public static final String WEATHER_TEMP = "temperature";//温度
    public static final String WEATHER_HUM = "humidity";//湿度
    public static final String WEATHER_QUALITY = "quality";//空气质量
    public static final String WEATHER_WIND = "wind";//风
    public static final String WEATHER_CHUANYI= "des";//穿衣


    //在我的设备列表点击每一项
    public static final String MY_MACHINE_ITEM_CLICKED_TYPE_SN = "workmachinetype";//设备类型sn
    public static final String MY_MACHINE_ITEM_CLICKED_DEVICE_SN = "workmachineid";//设备id
    public static final String MY_MACHINE_ITEM_CLICKED_ID = "UserDeviceID";//id
    public static final String MY_MACHINE_ITEM_CLICKED_INDEX_URL = "IndexUrl";//html控制页地址
    public static final String MY_MACHINE_ITEM_CLICKED_TYPE_STRING = "workmachinetypestring";

    //添加设备
    public static final String ADD_MACHINE_SMARTLINK_PROTOCOL = "add_machine_smartlink_protocol";//udp发送的协议
    public static final String ADD_MACHINE_TYPESN = "add_machine_typesn";//添加设备的typeSn
    public static final String ADD_MACHINE_TYPENUMBER = "add_machine_typeNumber";//添加设备的typeNumber

    public static final String ADD_MACHINE_BINDURL = "add_machine_bindurl";//绑定设备的url
    public static final String MACHINE_LIST_UPDATE = "add_machine_update";//添加设备更新

    //用户上一次打开app的时间戳
    public static final String LAST_TIME_OPEN_TIME = "last_time_open_time";//udp发送的协议

    //新风空气净化器pm2.5状态
    public static final String STATE_PM = "state_pm";


}
