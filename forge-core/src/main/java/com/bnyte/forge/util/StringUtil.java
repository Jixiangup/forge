package com.bnyte.forge.util;

import java.util.Locale;

/**
 * @auther bnyte
 * @date 2021-12-11 19:48
 * @email bnytezz@gmail.com
 */
public class StringUtil {

    /**
     * 数据库约束风格转换为Java小驼峰
     * @param sqlStyle 数据库约束风格字符串
     * @return 返回Java约束的小驼峰风格字符串
     */
    public static String transSqlStyleToMiniHump(String sqlStyle) {
        String result = "";
        String frontStr = "";
        String endStr = "";
        int indexOf = sqlStyle.indexOf("_");
        if (indexOf != -1) {
            frontStr = sqlStyle.substring(0, indexOf);
            if (indexOf != sqlStyle.length() - 1) {
                endStr = sqlStyle.substring(indexOf + 1);
                String c = (endStr.charAt(0) + "").toUpperCase(Locale.ROOT);
                endStr = c + endStr.substring(1);
            }
        } else {
            return sqlStyle;
        }
        result = frontStr + endStr;
        if (result.contains("_")) {
            return transSqlStyleToMiniHump(result);
        }
        return result;
    }

    /**
     * 将小驼峰转换为大驼峰
     * @param miniHump 小驼峰字符串
     * @return 返回大驼峰字符串
     */
    public static String transMiniHumpToBigHump (String miniHump) {
        String firstStr = (miniHump.charAt(0) + "").toUpperCase(Locale.ROOT);
        return firstStr + (miniHump.substring(1));
    }

    /**
     * 将数据库中下划线规范的字符串转换为大驼峰
     * @param sqlStyle 下划线风格字符串
     * @return 返回大驼峰字符串
     */
    public static String transSqlStyleToBigHump (String sqlStyle) {
        String miniHump = transSqlStyleToMiniHump(sqlStyle);
        String firstStr = (miniHump.charAt(0) + "").toUpperCase(Locale.ROOT);
        return firstStr + (miniHump.substring(1));
    }

    /**
     * 当前字符串是否有字符串(是否为空)
     * @param obj 需要判断的字符串
     * @return true为有字符串(不为空), false为没有字符串(为空)
     */
    public static boolean hasText(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof String)) {
            return false;
        }

        String tmp = String.valueOf(obj);

        return tmp != null && tmp.trim().length() > 0;
    }

    /**
     * 通过传入以'.'分割的路径字符串获取到当前的包路径，如：'com.bnyte.entity.User', 调用该方法时则会获取到'com.bnyte.entity'字符串的返回
     * @param javaPath 以'.'分割的路径字符串
     * @return 返回以.分割的包路径，如果没有"."作为分割则会返回默认引入的包,则是"java.lang"
     */
    public static String getPackageName (String javaPath) {
        int lastIndexOf = javaPath.lastIndexOf(".");
        if (lastIndexOf != -1) {
            return javaPath.substring(0, lastIndexOf);
        }
        return "java.lang";
    }

    /**
     * 通过传入以'.'分割的路径字符串获取到当前的类名，如：'com.bnyte.entity.User', 调用该方法时则会获取到'User'字符串的返回
     * @param javaPath 以'.'分割的路径字符串
     * @return 返回以.分割的类名
     */
    public static String getClassName (String javaPath) {
        int lastIndexOf = javaPath.lastIndexOf(".");
        if (lastIndexOf != -1) {
            return javaPath.substring(lastIndexOf + 1);
        }
        return javaPath;
    }

    /**
     * 判断字符串是否以\n结束
     */
    public static boolean isPrintln(String str) {
        if (!hasText(str)) return false;
        if (str.length() < 2) return false;
        int i = str.lastIndexOf("\n");
        int endIndex = str.length() - 1;
        return i != -1 && i == endIndex;
    }

}
