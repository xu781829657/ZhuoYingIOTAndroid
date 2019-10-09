package com.ouzhongiot.ozapp.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 2016/5/25.
 */
public class Weather {
    /**
     * reason : successed!
     * result : {"data":{"realtime":{"wind":{"windspeed":"7.0","direct":"西风","power":"1级","offset":null},"time":"20:00:00","weather":{"humidity":"76","img":"1","info":"多云","temperature":"26"},"dataUptime":1464178036,"date":"2016-05-25","city_code":"101210101","city_name":"杭州","week":3,"moon":"四月十九"},"life":{"date":"2016-5-25","info":{"kongtiao":["暂缺","暂缺"],"yundong":["暂缺","暂缺"],"ziwaixian":["暂缺","暂缺"],"ganmao":["暂缺","暂缺"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":["暂缺","暂缺"],"chuanyi":["暂缺","暂缺"]}},"weather":[{"date":"2016-05-25","info":{"night":["3","阵雨","22","东南风","微风","18:52"],"day":["2","阴","29","东南风","微风","04:59"]},"week":"三","nongli":"四月十九"},{"date":"2016-05-26","info":{"dawn":["3","阵雨","22","东南风","微风","18:52"],"night":["8","中雨","21","东北风","微风","18:53"],"day":["8","中雨","26","东北风","微风","04:59"]},"week":"四","nongli":"四月二十"},{"date":"2016-05-27","info":{"dawn":["8","中雨","21","东北风","微风","18:53"],"night":["3","阵雨","20","东风","微风","18:53"],"day":["3","阵雨","25","东风","微风","04:59"]},"week":"五","nongli":"四月廿一"},{"date":"2016-05-28","info":{"dawn":["3","阵雨","20","东风","微风","18:53"],"night":["3","阵雨","21","东风","微风","18:54"],"day":["3","阵雨","25","东风","微风","04:58"]},"week":"六","nongli":"四月廿二"},{"date":"2016-05-29","info":{"dawn":["3","阵雨","21","东风","微风","18:54"],"night":["3","阵雨","19","北风","微风","18:54"],"day":["3","阵雨","24","北风","微风","04:58"]},"week":"日","nongli":"四月廿三"},{"date":"2016-05-30","info":{"night":["3","阵雨","20","","微风","19:30"],"day":["2","阴","26","","微风","07:30"]},"week":"一","nongli":"四月廿四"},{"date":"2016-05-31","info":{"night":["3","阵雨","21","东南风","微风","19:30"],"day":["3","阵雨","29","东南风","微风","07:30"]},"week":"二","nongli":"四月廿五"}],"pm25":{"key":"","show_desc":0,"pm25":{"curPm":"66","pm25":"47","pm10":"66","level":2,"quality":"良","des":"今天的空气质量是可以接受的，除少数异常敏感体质的人群外，大家可在户外正常活动。"},"dateTime":"2016年05月25日20时","cityName":"杭州"},"date":null,"isForeign":0}}
     * error_code : 0
     */

    private String reason;
    /**
     * data : {"realtime":{"wind":{"windspeed":"7.0","direct":"西风","power":"1级","offset":null},"time":"20:00:00","weather":{"humidity":"76","img":"1","info":"多云","temperature":"26"},"dataUptime":1464178036,"date":"2016-05-25","city_code":"101210101","city_name":"杭州","week":3,"moon":"四月十九"},"life":{"date":"2016-5-25","info":{"kongtiao":["暂缺","暂缺"],"yundong":["暂缺","暂缺"],"ziwaixian":["暂缺","暂缺"],"ganmao":["暂缺","暂缺"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":["暂缺","暂缺"],"chuanyi":["暂缺","暂缺"]}},"weather":[{"date":"2016-05-25","info":{"night":["3","阵雨","22","东南风","微风","18:52"],"day":["2","阴","29","东南风","微风","04:59"]},"week":"三","nongli":"四月十九"},{"date":"2016-05-26","info":{"dawn":["3","阵雨","22","东南风","微风","18:52"],"night":["8","中雨","21","东北风","微风","18:53"],"day":["8","中雨","26","东北风","微风","04:59"]},"week":"四","nongli":"四月二十"},{"date":"2016-05-27","info":{"dawn":["8","中雨","21","东北风","微风","18:53"],"night":["3","阵雨","20","东风","微风","18:53"],"day":["3","阵雨","25","东风","微风","04:59"]},"week":"五","nongli":"四月廿一"},{"date":"2016-05-28","info":{"dawn":["3","阵雨","20","东风","微风","18:53"],"night":["3","阵雨","21","东风","微风","18:54"],"day":["3","阵雨","25","东风","微风","04:58"]},"week":"六","nongli":"四月廿二"},{"date":"2016-05-29","info":{"dawn":["3","阵雨","21","东风","微风","18:54"],"night":["3","阵雨","19","北风","微风","18:54"],"day":["3","阵雨","24","北风","微风","04:58"]},"week":"日","nongli":"四月廿三"},{"date":"2016-05-30","info":{"night":["3","阵雨","20","","微风","19:30"],"day":["2","阴","26","","微风","07:30"]},"week":"一","nongli":"四月廿四"},{"date":"2016-05-31","info":{"night":["3","阵雨","21","东南风","微风","19:30"],"day":["3","阵雨","29","东南风","微风","07:30"]},"week":"二","nongli":"四月廿五"}],"pm25":{"key":"","show_desc":0,"pm25":{"curPm":"66","pm25":"47","pm10":"66","level":2,"quality":"良","des":"今天的空气质量是可以接受的，除少数异常敏感体质的人群外，大家可在户外正常活动。"},"dateTime":"2016年05月25日20时","cityName":"杭州"},"date":null,"isForeign":0}
     */

    private ResultBean result;
    private int error_code;

    public static Weather objectFromData(String str) {

        return new Gson().fromJson(str, Weather.class);
    }

    public static Weather objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Weather> arrayWeatherFromData(String str) {

        Type listType = new TypeToken<ArrayList<Weather>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<Weather> arrayWeatherFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Weather>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        /**
         * realtime : {"wind":{"windspeed":"7.0","direct":"西风","power":"1级","offset":null},"time":"20:00:00","weather":{"humidity":"76","img":"1","info":"多云","temperature":"26"},"dataUptime":1464178036,"date":"2016-05-25","city_code":"101210101","city_name":"杭州","week":3,"moon":"四月十九"}
         * life : {"date":"2016-5-25","info":{"kongtiao":["暂缺","暂缺"],"yundong":["暂缺","暂缺"],"ziwaixian":["暂缺","暂缺"],"ganmao":["暂缺","暂缺"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":["暂缺","暂缺"],"chuanyi":["暂缺","暂缺"]}}
         * weather : [{"date":"2016-05-25","info":{"night":["3","阵雨","22","东南风","微风","18:52"],"day":["2","阴","29","东南风","微风","04:59"]},"week":"三","nongli":"四月十九"},{"date":"2016-05-26","info":{"dawn":["3","阵雨","22","东南风","微风","18:52"],"night":["8","中雨","21","东北风","微风","18:53"],"day":["8","中雨","26","东北风","微风","04:59"]},"week":"四","nongli":"四月二十"},{"date":"2016-05-27","info":{"dawn":["8","中雨","21","东北风","微风","18:53"],"night":["3","阵雨","20","东风","微风","18:53"],"day":["3","阵雨","25","东风","微风","04:59"]},"week":"五","nongli":"四月廿一"},{"date":"2016-05-28","info":{"dawn":["3","阵雨","20","东风","微风","18:53"],"night":["3","阵雨","21","东风","微风","18:54"],"day":["3","阵雨","25","东风","微风","04:58"]},"week":"六","nongli":"四月廿二"},{"date":"2016-05-29","info":{"dawn":["3","阵雨","21","东风","微风","18:54"],"night":["3","阵雨","19","北风","微风","18:54"],"day":["3","阵雨","24","北风","微风","04:58"]},"week":"日","nongli":"四月廿三"},{"date":"2016-05-30","info":{"night":["3","阵雨","20","","微风","19:30"],"day":["2","阴","26","","微风","07:30"]},"week":"一","nongli":"四月廿四"},{"date":"2016-05-31","info":{"night":["3","阵雨","21","东南风","微风","19:30"],"day":["3","阵雨","29","东南风","微风","07:30"]},"week":"二","nongli":"四月廿五"}]
         * pm25 : {"key":"","show_desc":0,"pm25":{"curPm":"66","pm25":"47","pm10":"66","level":2,"quality":"良","des":"今天的空气质量是可以接受的，除少数异常敏感体质的人群外，大家可在户外正常活动。"},"dateTime":"2016年05月25日20时","cityName":"杭州"}
         * date : null
         * isForeign : 0
         */

        private DataBean data;

        public static ResultBean objectFromData(String str) {

            return new Gson().fromJson(str, ResultBean.class);
        }

        public static ResultBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), ResultBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<ResultBean> arrayResultBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<ResultBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<ResultBean> arrayResultBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<ResultBean>>() {
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

        public static class DataBean {
            /**
             * wind : {"windspeed":"7.0","direct":"西风","power":"1级","offset":null}
             * time : 20:00:00
             * weather : {"humidity":"76","img":"1","info":"多云","temperature":"26"}
             * dataUptime : 1464178036
             * date : 2016-05-25
             * city_code : 101210101
             * city_name : 杭州
             * week : 3
             * moon : 四月十九
             */

            private RealtimeBean realtime;
            /**
             * date : 2016-5-25
             * info : {"kongtiao":["暂缺","暂缺"],"yundong":["暂缺","暂缺"],"ziwaixian":["暂缺","暂缺"],"ganmao":["暂缺","暂缺"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":["暂缺","暂缺"],"chuanyi":["暂缺","暂缺"]}
             */

            private LifeBean life;
            /**
             * key :
             * show_desc : 0
             * pm25 : {"curPm":"66","pm25":"47","pm10":"66","level":2,"quality":"良","des":"今天的空气质量是可以接受的，除少数异常敏感体质的人群外，大家可在户外正常活动。"}
             * dateTime : 2016年05月25日20时
             * cityName : 杭州
             */

            private Pm25Bean pm25;
            private Object date;
            private int isForeign;
            /**
             * date : 2016-05-25
             * info : {"night":["3","阵雨","22","东南风","微风","18:52"],"day":["2","阴","29","东南风","微风","04:59"]}
             * week : 三
             * nongli : 四月十九
             */

            private List<WeatherBean> weather;

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

            public RealtimeBean getRealtime() {
                return realtime;
            }

            public void setRealtime(RealtimeBean realtime) {
                this.realtime = realtime;
            }

            public LifeBean getLife() {
                return life;
            }

            public void setLife(LifeBean life) {
                this.life = life;
            }

            public Pm25Bean getPm25() {
                return pm25;
            }

            public void setPm25(Pm25Bean pm25) {
                this.pm25 = pm25;
            }

            public Object getDate() {
                return date;
            }

            public void setDate(Object date) {
                this.date = date;
            }

            public int getIsForeign() {
                return isForeign;
            }

            public void setIsForeign(int isForeign) {
                this.isForeign = isForeign;
            }

            public List<WeatherBean> getWeather() {
                return weather;
            }

            public void setWeather(List<WeatherBean> weather) {
                this.weather = weather;
            }

            public static class RealtimeBean {
                /**
                 * windspeed : 7.0
                 * direct : 西风
                 * power : 1级
                 * offset : null
                 */

                private WindBean wind;
                private String time;
                /**
                 * humidity : 76
                 * img : 1
                 * info : 多云
                 * temperature : 26
                 */

                private WeatherBean weather;
                private int dataUptime;
                private String date;
                private String city_code;
                private String city_name;
                private int week;
                private String moon;

                public static RealtimeBean objectFromData(String str) {

                    return new Gson().fromJson(str, RealtimeBean.class);
                }

                public static RealtimeBean objectFromData(String str, String key) {

                    try {
                        JSONObject jsonObject = new JSONObject(str);

                        return new Gson().fromJson(jsonObject.getString(str), RealtimeBean.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                public static List<RealtimeBean> arrayRealtimeBeanFromData(String str) {

                    Type listType = new TypeToken<ArrayList<RealtimeBean>>() {
                    }.getType();

                    return new Gson().fromJson(str, listType);
                }

                public static List<RealtimeBean> arrayRealtimeBeanFromData(String str, String key) {

                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        Type listType = new TypeToken<ArrayList<RealtimeBean>>() {
                        }.getType();

                        return new Gson().fromJson(jsonObject.getString(str), listType);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return new ArrayList();


                }

                public WindBean getWind() {
                    return wind;
                }

                public void setWind(WindBean wind) {
                    this.wind = wind;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public WeatherBean getWeather() {
                    return weather;
                }

                public void setWeather(WeatherBean weather) {
                    this.weather = weather;
                }

                public int getDataUptime() {
                    return dataUptime;
                }

                public void setDataUptime(int dataUptime) {
                    this.dataUptime = dataUptime;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getCity_code() {
                    return city_code;
                }

                public void setCity_code(String city_code) {
                    this.city_code = city_code;
                }

                public String getCity_name() {
                    return city_name;
                }

                public void setCity_name(String city_name) {
                    this.city_name = city_name;
                }

                public int getWeek() {
                    return week;
                }

                public void setWeek(int week) {
                    this.week = week;
                }

                public String getMoon() {
                    return moon;
                }

                public void setMoon(String moon) {
                    this.moon = moon;
                }

                public static class WindBean {
                    private String windspeed;
                    private String direct;
                    private String power;
                    private Object offset;

                    public static WindBean objectFromData(String str) {

                        return new Gson().fromJson(str, WindBean.class);
                    }

                    public static WindBean objectFromData(String str, String key) {

                        try {
                            JSONObject jsonObject = new JSONObject(str);

                            return new Gson().fromJson(jsonObject.getString(str), WindBean.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    public static List<WindBean> arrayWindBeanFromData(String str) {

                        Type listType = new TypeToken<ArrayList<WindBean>>() {
                        }.getType();

                        return new Gson().fromJson(str, listType);
                    }

                    public static List<WindBean> arrayWindBeanFromData(String str, String key) {

                        try {
                            JSONObject jsonObject = new JSONObject(str);
                            Type listType = new TypeToken<ArrayList<WindBean>>() {
                            }.getType();

                            return new Gson().fromJson(jsonObject.getString(str), listType);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return new ArrayList();


                    }

                    public String getWindspeed() {
                        return windspeed;
                    }

                    public void setWindspeed(String windspeed) {
                        this.windspeed = windspeed;
                    }

                    public String getDirect() {
                        return direct;
                    }

                    public void setDirect(String direct) {
                        this.direct = direct;
                    }

                    public String getPower() {
                        return power;
                    }

                    public void setPower(String power) {
                        this.power = power;
                    }

                    public Object getOffset() {
                        return offset;
                    }

                    public void setOffset(Object offset) {
                        this.offset = offset;
                    }
                }

                public static class WeatherBean {
                    private String humidity;
                    private String img;
                    private String info;
                    private String temperature;

                    public static WeatherBean objectFromData(String str) {

                        return new Gson().fromJson(str, WeatherBean.class);
                    }

                    public static WeatherBean objectFromData(String str, String key) {

                        try {
                            JSONObject jsonObject = new JSONObject(str);

                            return new Gson().fromJson(jsonObject.getString(str), WeatherBean.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    public static List<WeatherBean> arrayWeatherBeanFromData(String str) {

                        Type listType = new TypeToken<ArrayList<WeatherBean>>() {
                        }.getType();

                        return new Gson().fromJson(str, listType);
                    }

                    public static List<WeatherBean> arrayWeatherBeanFromData(String str, String key) {

                        try {
                            JSONObject jsonObject = new JSONObject(str);
                            Type listType = new TypeToken<ArrayList<WeatherBean>>() {
                            }.getType();

                            return new Gson().fromJson(jsonObject.getString(str), listType);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return new ArrayList();


                    }

                    public String getHumidity() {
                        return humidity;
                    }

                    public void setHumidity(String humidity) {
                        this.humidity = humidity;
                    }

                    public String getImg() {
                        return img;
                    }

                    public void setImg(String img) {
                        this.img = img;
                    }

                    public String getInfo() {
                        return info;
                    }

                    public void setInfo(String info) {
                        this.info = info;
                    }

                    public String getTemperature() {
                        return temperature;
                    }

                    public void setTemperature(String temperature) {
                        this.temperature = temperature;
                    }
                }
            }

            public static class LifeBean {
                private String date;
                private InfoBean info;

                public static LifeBean objectFromData(String str) {

                    return new Gson().fromJson(str, LifeBean.class);
                }

                public static LifeBean objectFromData(String str, String key) {

                    try {
                        JSONObject jsonObject = new JSONObject(str);

                        return new Gson().fromJson(jsonObject.getString(str), LifeBean.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                public static List<LifeBean> arrayLifeBeanFromData(String str) {

                    Type listType = new TypeToken<ArrayList<LifeBean>>() {
                    }.getType();

                    return new Gson().fromJson(str, listType);
                }

                public static List<LifeBean> arrayLifeBeanFromData(String str, String key) {

                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        Type listType = new TypeToken<ArrayList<LifeBean>>() {
                        }.getType();

                        return new Gson().fromJson(jsonObject.getString(str), listType);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return new ArrayList();


                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public InfoBean getInfo() {
                    return info;
                }

                public void setInfo(InfoBean info) {
                    this.info = info;
                }

                public static class InfoBean {
                    private List<String> kongtiao;
                    private List<String> yundong;
                    private List<String> ziwaixian;
                    private List<String> ganmao;
                    private List<String> xiche;
                    private List<String> wuran;
                    private List<String> chuanyi;

                    public static InfoBean objectFromData(String str) {

                        return new Gson().fromJson(str, InfoBean.class);
                    }

                    public static InfoBean objectFromData(String str, String key) {

                        try {
                            JSONObject jsonObject = new JSONObject(str);

                            return new Gson().fromJson(jsonObject.getString(str), InfoBean.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    public static List<InfoBean> arrayInfoBeanFromData(String str) {

                        Type listType = new TypeToken<ArrayList<InfoBean>>() {
                        }.getType();

                        return new Gson().fromJson(str, listType);
                    }

                    public static List<InfoBean> arrayInfoBeanFromData(String str, String key) {

                        try {
                            JSONObject jsonObject = new JSONObject(str);
                            Type listType = new TypeToken<ArrayList<InfoBean>>() {
                            }.getType();

                            return new Gson().fromJson(jsonObject.getString(str), listType);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return new ArrayList();


                    }

                    public List<String> getKongtiao() {
                        return kongtiao;
                    }

                    public void setKongtiao(List<String> kongtiao) {
                        this.kongtiao = kongtiao;
                    }

                    public List<String> getYundong() {
                        return yundong;
                    }

                    public void setYundong(List<String> yundong) {
                        this.yundong = yundong;
                    }

                    public List<String> getZiwaixian() {
                        return ziwaixian;
                    }

                    public void setZiwaixian(List<String> ziwaixian) {
                        this.ziwaixian = ziwaixian;
                    }

                    public List<String> getGanmao() {
                        return ganmao;
                    }

                    public void setGanmao(List<String> ganmao) {
                        this.ganmao = ganmao;
                    }

                    public List<String> getXiche() {
                        return xiche;
                    }

                    public void setXiche(List<String> xiche) {
                        this.xiche = xiche;
                    }

                    public List<String> getWuran() {
                        return wuran;
                    }

                    public void setWuran(List<String> wuran) {
                        this.wuran = wuran;
                    }

                    public List<String> getChuanyi() {
                        return chuanyi;
                    }

                    public void setChuanyi(List<String> chuanyi) {
                        this.chuanyi = chuanyi;
                    }
                }
            }

            public static class Pm25Bean {
                private String key;
                private int show_desc;
                /**
                 * curPm : 66
                 * pm25 : 47
                 * pm10 : 66
                 * level : 2
                 * quality : 良
                 * des : 今天的空气质量是可以接受的，除少数异常敏感体质的人群外，大家可在户外正常活动。
                 */

                private Pm25Bean pm25;
                private String dateTime;
                private String cityName;

                public static Pm25Bean objectFromData(String str) {

                    return new Gson().fromJson(str, Pm25Bean.class);
                }

                public static Pm25Bean objectFromData(String str, String key) {

                    try {
                        JSONObject jsonObject = new JSONObject(str);

                        return new Gson().fromJson(jsonObject.getString(str), Pm25Bean.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                public static List<Pm25Bean> arrayPm25BeanFromData(String str) {

                    Type listType = new TypeToken<ArrayList<Pm25Bean>>() {
                    }.getType();

                    return new Gson().fromJson(str, listType);
                }

                public static List<Pm25Bean> arrayPm25BeanFromData(String str, String key) {

                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        Type listType = new TypeToken<ArrayList<Pm25Bean>>() {
                        }.getType();

                        return new Gson().fromJson(jsonObject.getString(str), listType);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return new ArrayList();


                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public int getShow_desc() {
                    return show_desc;
                }

                public void setShow_desc(int show_desc) {
                    this.show_desc = show_desc;
                }

                public Pm25Bean getPm25() {
                    return pm25;
                }

                public void setPm25(Pm25Bean pm25) {
                    this.pm25 = pm25;
                }

                public String getDateTime() {
                    return dateTime;
                }

                public void setDateTime(String dateTime) {
                    this.dateTime = dateTime;
                }

                public String getCityName() {
                    return cityName;
                }

                public void setCityName(String cityName) {
                    this.cityName = cityName;
                }

                public static class Pm25Bean1 {
                    private String curPm;
                    private String pm25;
                    private String pm10;
                    private int level;
                    private String quality;
                    private String des;

                    public static Pm25Bean objectFromData(String str) {

                        return new Gson().fromJson(str, Pm25Bean.class);
                    }

                    public static Pm25Bean objectFromData(String str, String key) {

                        try {
                            JSONObject jsonObject = new JSONObject(str);

                            return new Gson().fromJson(jsonObject.getString(str), Pm25Bean.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    public static List<Pm25Bean> arrayPm25BeanFromData(String str) {

                        Type listType = new TypeToken<ArrayList<Pm25Bean>>() {
                        }.getType();

                        return new Gson().fromJson(str, listType);
                    }

                    public static List<Pm25Bean> arrayPm25BeanFromData(String str, String key) {

                        try {
                            JSONObject jsonObject = new JSONObject(str);
                            Type listType = new TypeToken<ArrayList<Pm25Bean>>() {
                            }.getType();

                            return new Gson().fromJson(jsonObject.getString(str), listType);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return new ArrayList();


                    }

                    public String getCurPm() {
                        return curPm;
                    }

                    public void setCurPm(String curPm) {
                        this.curPm = curPm;
                    }

                    public String getPm25() {
                        return pm25;
                    }

                    public void setPm25(String pm25) {
                        this.pm25 = pm25;
                    }

                    public String getPm10() {
                        return pm10;
                    }

                    public void setPm10(String pm10) {
                        this.pm10 = pm10;
                    }

                    public int getLevel() {
                        return level;
                    }

                    public void setLevel(int level) {
                        this.level = level;
                    }

                    public String getQuality() {
                        return quality;
                    }

                    public void setQuality(String quality) {
                        this.quality = quality;
                    }

                    public String getDes() {
                        return des;
                    }

                    public void setDes(String des) {
                        this.des = des;
                    }
                }
            }

            public static class WeatherBean {
                private String date;
                private InfoBean info;
                private String week;
                private String nongli;

                public static WeatherBean objectFromData(String str) {

                    return new Gson().fromJson(str, WeatherBean.class);
                }

                public static WeatherBean objectFromData(String str, String key) {

                    try {
                        JSONObject jsonObject = new JSONObject(str);

                        return new Gson().fromJson(jsonObject.getString(str), WeatherBean.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                public static List<WeatherBean> arrayWeatherBeanFromData(String str) {

                    Type listType = new TypeToken<ArrayList<WeatherBean>>() {
                    }.getType();

                    return new Gson().fromJson(str, listType);
                }

                public static List<WeatherBean> arrayWeatherBeanFromData(String str, String key) {

                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        Type listType = new TypeToken<ArrayList<WeatherBean>>() {
                        }.getType();

                        return new Gson().fromJson(jsonObject.getString(str), listType);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return new ArrayList();


                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public InfoBean getInfo() {
                    return info;
                }

                public void setInfo(InfoBean info) {
                    this.info = info;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getNongli() {
                    return nongli;
                }

                public void setNongli(String nongli) {
                    this.nongli = nongli;
                }

                public static class InfoBean {
                    private List<String> night;
                    private List<String> day;

                    public static InfoBean objectFromData(String str) {

                        return new Gson().fromJson(str, InfoBean.class);
                    }

                    public static InfoBean objectFromData(String str, String key) {

                        try {
                            JSONObject jsonObject = new JSONObject(str);

                            return new Gson().fromJson(jsonObject.getString(str), InfoBean.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    public static List<InfoBean> arrayInfoBeanFromData(String str) {

                        Type listType = new TypeToken<ArrayList<InfoBean>>() {
                        }.getType();

                        return new Gson().fromJson(str, listType);
                    }

                    public static List<InfoBean> arrayInfoBeanFromData(String str, String key) {

                        try {
                            JSONObject jsonObject = new JSONObject(str);
                            Type listType = new TypeToken<ArrayList<InfoBean>>() {
                            }.getType();

                            return new Gson().fromJson(jsonObject.getString(str), listType);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return new ArrayList();


                    }

                    public List<String> getNight() {
                        return night;
                    }

                    public void setNight(List<String> night) {
                        this.night = night;
                    }

                    public List<String> getDay() {
                        return day;
                    }

                    public void setDay(List<String> day) {
                        this.day = day;
                    }
                }
            }
        }
    }
}
