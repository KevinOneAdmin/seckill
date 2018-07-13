package com.mdiai.seckill.common.utils;

import java.util.regex.Pattern;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  17:21
 * @Description 校验工具类
 */
public class ValidatorUtil {

    private static final Pattern MOBILE_NUMBER = Pattern.compile("^1[3-9]\\d{9}");

    public static boolean isMobile(String mobile) {
        return StringUtils.isEmpty(mobile) ? false : MOBILE_NUMBER.matcher(mobile).matches() ? true : false;
    }

//    public static void main(String[] args) {
//        String one = "12514714714";
//        String two = "13514714714";
//        boolean mobile = isMobile(one);
//        boolean mobile1 = isMobile(two);
//        System.out.println(mobile);
//        System.out.println(mobile1);
//    }
}
