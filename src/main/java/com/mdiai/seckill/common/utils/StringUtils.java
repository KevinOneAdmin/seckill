package com.mdiai.seckill.common.utils;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  17:08
 * @Description
 */
public class StringUtils {

    /**
     * 字符判空,此方法在传入不为NULL的值时会自动trim然后判断
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return null==str||str.trim().length()<1?true:false;
    }
}
