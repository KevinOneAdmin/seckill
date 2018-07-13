package com.mdiai.seckill.redis.key;

/**
 * @Auther Kevin.Liu
 * @Date create in 2018/7/13  11:17
 * @Description
 */
public class AccessKey extends BaseProfix  {

    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey whihExpire(int expireSeconds) {
        return  new AccessKey(expireSeconds,"access");
    }
}
