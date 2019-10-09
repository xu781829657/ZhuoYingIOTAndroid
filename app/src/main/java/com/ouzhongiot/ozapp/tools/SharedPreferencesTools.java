package com.ouzhongiot.ozapp.tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * @author hxf
 * @date 创建时间: 2017/2/13
 * @Description sharedpreferences 工具类
 */

public class SharedPreferencesTools {
    private static SharedPreferencesTools instance;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public static SharedPreferencesTools getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesTools(context);
        }
        return instance;
    }

    public SharedPreferencesTools(Context context) {
        sp = context.getSharedPreferences("sp_data", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 添加数据
     *
     * @param key
     * @param val
     * @return
     */
    public boolean putData(String key, Object val) {
        try {
            if (val instanceof Integer) {
                editor.putInt(key, (Integer) val);
            } else if (val instanceof Long) {
                editor.putLong(key, (Long) val);
            } else if (val instanceof Boolean) {
                editor.putBoolean(key, (Boolean) val);
            } else if (val instanceof String) {
                editor.putString(key, (String) val);
            } else if (val instanceof Float) {
                editor.putFloat(key, (Float) val);
            }
            editor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    ;

    /**
     * 获取指定数据
     */
    public Object getData(String key) {
        Map<String, Object> map = (Map<String, Object>) sp.getAll();
        if (map.get(key) == null) {
            return "";
        }
        return map.get(key);
    }

    /**
     * 清除指定内容
     */
    public void removeData(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有
     */
    public void removeAll() {
        editor.clear();
        editor.commit();
    }
}
