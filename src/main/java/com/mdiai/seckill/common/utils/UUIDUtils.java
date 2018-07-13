package com.mdiai.seckill.common.utils;

import java.util.UUID;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/9  10:19
 * @Description UUID工具类
 */
public class UUIDUtils {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
