package com.mdiai.seckill.redis.key;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/5  16:26
 * @Description
 */
public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();

}
