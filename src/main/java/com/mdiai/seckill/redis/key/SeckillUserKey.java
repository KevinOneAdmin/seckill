package com.mdiai.seckill.redis.key;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/9  10:23
 * @Description
 */
public class SeckillUserKey extends BaseProfix {

    public static final int TOKEN_EXPIRESECONDS = 1800;

    private SeckillUserKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static SeckillUserKey token= new SeckillUserKey(TOKEN_EXPIRESECONDS,"token");
    public static SeckillUserKey GETBYID= new SeckillUserKey(0,"id");

}
