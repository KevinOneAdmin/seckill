package com.mdiai.seckill.common.utils;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.alibaba.fastjson.JSON;

/**
 * @Auther Kevin.Liu
 * @Date create in 2018/7/12  11:11
 * @Description
 */
public class JsonUtils {

    /**
     * 对象转JSON
     * @param vlue
     * @param <T>
     * @return
     */
    public static  <T> String beanToString(T vlue) {
        if (null == vlue) {
            return null;
        }
        Class<?> aClass = vlue.getClass();
        if (aClass == int.class || aClass == Integer.class) {
            return "" + vlue;
        } else if (aClass == String.class) {
            return (String) vlue;
        } else if (aClass == char.class || aClass == Char.class) {

        } else if (aClass == float.class || aClass == Float.class) {
            return "" + vlue;
        } else if (aClass == double.class || aClass == Double.class) {
            return "" + vlue;
        } else if (aClass == short.class || aClass == Short.class) {
            return "" + vlue;
        } else if (aClass == long.class || aClass == Long.class) {
            return "" + vlue;
        } else if (aClass == boolean.class || aClass == Boolean.class) {

        } else if (aClass == byte.class || aClass == Byte.class) {

        }
        return JSON.toJSONString(vlue);
    }

    /**
     * json转对象
     * @param sobj
     * @param aClass
     * @param <T>
     * @return
     */
    public static <T> T stringToBean(String sobj, Class<T> aClass) {
        if (null == sobj || sobj.length() < 1 || null == aClass) {
            return null;
        }

        if (aClass == int.class || aClass == Integer.class) {
            return (T) Integer.valueOf(sobj);
        } else if (aClass == String.class) {
            return (T) sobj;
        } else if (aClass == char.class || aClass == Char.class) {

        } else if (aClass == float.class || aClass == Float.class) {
            return (T) Float.valueOf(sobj);
        } else if (aClass == double.class || aClass == Double.class) {
            return (T) Double.valueOf(sobj);
        } else if (aClass == short.class || aClass == Short.class) {
            return (T) Short.valueOf(sobj);
        } else if (aClass == long.class || aClass == Long.class) {
            return (T) Long.valueOf(sobj);
        } else if (aClass == boolean.class || aClass == Boolean.class) {

        } else if (aClass == byte.class || aClass == Byte.class) {

        }
        return JSON.toJavaObject(JSON.parseObject(sobj), aClass);
    }
}
