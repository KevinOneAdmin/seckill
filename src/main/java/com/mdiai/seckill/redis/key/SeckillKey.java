package com.mdiai.seckill.redis.key;

/**
 * @Auther Kevin.Liu
 * @Date create in 2018/7/12  15:28
 * @Description
 */
public class SeckillKey extends BaseProfix {

    private SeckillKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static SeckillKey ISGOODSOVER = new SeckillKey(0,"goodsOver");

    public static SeckillKey SECKILLPATH = new SeckillKey(60,"seckillPath");

    public static SeckillKey VERIFYCOD = new SeckillKey(60,"verifyCod");
}
