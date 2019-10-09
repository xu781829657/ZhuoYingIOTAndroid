package com.ouzhongiot.ozapp.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hxf
 * @date 创建时间: 2016/11/23
 * @Description 正则表达式匹配工具类
 */

public class RegularMatchTools {
    /**
     * 验证邮箱格式
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^\\S+@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 验证手机格式
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|17[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断是否全是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 英文字母或者数字或者两者的组合
     *
     * @param str
     * @return
     */
    public static boolean isNumerAndLetter(String str) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    /**
     * 26个英文字母（包括大小写）
     *
     * @param str
     * @return
     */
    public static boolean isLetter(String str) {
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


}
