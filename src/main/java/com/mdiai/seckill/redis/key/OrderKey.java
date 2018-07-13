package com.mdiai.seckill.redis.key;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/5  16:35
 * @Description
 */
public class OrderKey extends BaseProfix {

    private OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getSeckillOrder = new OrderKey(3600,"seckillOrder");

}
