package com.mdiai.seckill.redis.key;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/5  16:35
 * @Description
 */
public class GoodsKey extends BaseProfix {

    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey GETGOODSLIST = new GoodsKey(60,"goodsList");

    public static GoodsKey GETSECKILLGOODSSTOCK = new GoodsKey(0,"goodsSock");
}
