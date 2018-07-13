package com.mdiai.seckill.redis.key;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/5  16:34
 * @Description
 */
public class UserKey extends BaseProfix {

    private UserKey(String prefix) {
        super( prefix);
    }


    public static UserKey getById= new UserKey("id");
    public static UserKey getByName= new UserKey("name");
}
