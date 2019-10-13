package com.ouzhongiot.ozapp.tools;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtils {
    /**
     * 判断一个字符串不是空的（服务可能会传null作为字符串内空，将null认为是空的）
     *
     * @param arg
     * @return
     */
    public static boolean isNotEmpty(String arg) {
        if (null == arg) {
            return false;
        }
        if ("".equals(arg) || "".equals(arg.trim())) {
            return false;
        }
        return !"NULL".equals(arg.toUpperCase(Locale.CHINA));
    }

    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str)) {
            return true;
        } else if ("".equals(str.trim())) {
            return true;
        } else return "".equals(replaceBlank(str));
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\\\\r|\\\\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 判断一个字符串不是空的（服务可能会传null作为字符串内空，将null认为是空的）
     *
     * @param arg
     * @return
     */
    public static boolean isNULL(String arg) {
        if (null == arg) {
            return true;
        }
        if ("".equals(arg) || "".equals(arg.trim())) {
            return true;
        }
        return "null".equalsIgnoreCase(arg);
    }

    /**
     * 去掉右边的零
     *
     * @param str
     * @return
     */
    public static String trimRightZero(String str) {
        if (StringUtils.isNotEmpty(str) && str.indexOf(".") > 0) {
            String temp = str.trim();
            temp = temp.replaceAll("([0])*$", "");// 去掉末位为0
            temp = temp.replaceAll("(\\.)$", "");// 去掉末位为.
            return temp;
        }
        return str;
    }

    /**
     * 去掉左边的零
     *
     * @param strValue
     * @return
     */
    public static String trimLeftZero(String strValue) {
        if (null != strValue && strValue.matches("(0)(\\d)+")) {
            return strValue.replaceAll("^(0)+", "");
        }
        return strValue;
    }

    /**
     * 在字符串判断是否存在匹配的字符
     *
     * @param paramStr 要查找的字符串
     * @param subStr   待匹配的字符
     * @return
     */
    public static boolean isExistSubString(String paramStr, String subStr) {
        if (null == paramStr || null == subStr) {
            return false;
        }
        return paramStr.indexOf(subStr) >= 0;
    }

    /**
     * 在字符串判断是否存在匹配的字符
     *
     * @param paramStr 要查找的字符串
     * @param subStr   待匹配的字符
     * @return
     */
    public static boolean isExistSubStringOrBlank(String paramStr, String subStr) {
        if (null == paramStr || null == subStr) {
            return true;
        }
        return paramStr.indexOf(subStr) >= 0;
    }

    public static boolean isEquals(String str1, String str2) {
        if (null == str1 && null == str2) {
            return true;
        }
        if (null != str1 && null != str2) {
            return str1.trim().equals(str2.trim());
        }
        return false;
    }

    //不区分大小写
    public static boolean isEqualsNoCase(String str1, String str2) {
        if (null == str1 && null == str2) {
            return true;
        }
        if (null != str1 && null != str2) {
            return str1.trim().toLowerCase().equals(str2.trim().toLowerCase());
        }
        return false;
    }

    public static String trimNull(String s) {
        return s == null ? "" : s;
    }

    /**
     * 判断字符串里是否含有汉字
     */
    public static boolean isHasChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (p.matcher(String.valueOf(c)).matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否由数子和字母组合而成
     */
    public static boolean isByNumAndLetter(String str) {
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z])([\\S]{8,20})$";
        return str.matches(regex);
    }


    /**
     * 判断字符串是不是手机号格式（1开头，后面10个数字）
     *
     * @return 返回true表示通过，返回false表示失败
     */
    public static boolean isTelePhoneNum(String str) {
        boolean bo = false;
        Pattern pattern = Pattern.compile("^1\\d{10}$");
        // 验证
        if (StringUtils.isNotEmpty(str) && null != pattern) {
            Matcher matcher = pattern.matcher(str);
            bo = matcher.matches();
        }
        // 返回验证结果
        return bo;
    }


    /**
     * 获取掩码手机号
     *
     * @param phoneNum
     * @return
     */
    public static String getMaskPhoneNum(String phoneNum) {
        try {
            if (isTelePhoneNum(phoneNum)) {
                // String maskPortion = phoneNum.substring(3, 7);
                char[] phoneChar = phoneNum.toCharArray();
                String phoneStr = "";
                for (int i = 0; i < phoneChar.length; i++) {
                    if (i > 2 && i < 7) {
                        phoneStr += "*";
                    } else {
                        phoneStr += phoneChar[i];
                    }
                }
                return phoneStr;
            } else {
                Pattern pattern = Pattern.compile("^1\\d{2}\\*{3}\\d{4}$");
                if (pattern.matcher(phoneNum).matches()) {
                    return phoneNum;
                }
            }
        } catch (Exception ex) {
            LogManager.e(ex);
        }
        return "";
    }


    /**
     * 格式化字符串
     *
     * @param argString
     * @return
     * @author TangWei 2014-1-13下午4:15:52
     */
    public static String nullToBlank(String argString) {
        if (null == argString) {
            return "";
        }
        return argString;
    }


}
