package com.mdiai.seckill.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/5  17:37
 * @Description MD5工具类
 */
public class MD5Util {


    private static final String SALT = "c014fe74550d4b4fa78170db3c8c177f";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    /**
     * 根据固定值做摘要MD5
     * @param inputPass
     * @return
     *
     * 输入1234567得到17510ce6b0124ec64a252a645cdaf3d2
     */
    public static String inputPassFormPass(String inputPass) {
        int end = SALT.length();
        int start = 0;
        String one = "";
        String two = "";
        while (end > 0 && start < SALT.length()) {
            end -= 3;
            start += 3;
            if (end > 0 && start < SALT.length()) {
                one += SALT.charAt(end);
                two += SALT.charAt(start);
            }
        }

        String rel = one + inputPass + two;
        return md5(rel);
    }

    /**
     * 数据库动态摘要MD5
     * @param fromPass
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String fromPass, String salt) {
        int end = salt.length();
        int start = 0;
        String one = "";
        String two = "";
        while (end > 0 && start < salt.length()) {
            end -= 3;
            start += 3;
            if (end > 0 && start < salt.length()) {
                one += salt.charAt(end);
                two += salt.charAt(start);
            }
        }
        String rel = one + fromPass + two;
        return md5(rel);
    }


    public static String inputPassTODBPass(String inputPass,String salt) {
        return formPassToDBPass(inputPassFormPass(inputPass),salt);
    }

    public static void main(String[] args) {
        String pass = inputPassTODBPass("123456a", "18a29be611754afa8c9e5b59677c89ce");
        System.out.println(">>>>>>>>>>>>>>>>>>"+pass);
    }
}
