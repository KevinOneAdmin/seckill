package com.mdiai.seckill.common.access;

import java.lang.annotation.*;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/13  11:34
 * @Description 接口限流注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {

    /**
     * 时间单位,默认是秒
     *
     * @return
     */
    int seconds();

    /**
     * 单位时间内的最大访问次数
     *
     * @return
     */
    int maxCount();

    /**
     * 是否需登录
     *
     * @return
     */
    boolean needLogin() default true;

}
