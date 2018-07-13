package com.mdiai.seckill.common.access;

import com.mdiai.seckill.domain.SeckillUser;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/13  12:28
 * @Description 用户上下文
 */
public class UserConText {
    private static final ThreadLocal<SeckillUser> userHolder = new ThreadLocal<SeckillUser>();

    public static void setUser(SeckillUser user){
        userHolder.set(user);
    }

    public static SeckillUser getUser(){
        return userHolder.get();
    }

}
