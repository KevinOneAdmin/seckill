package com.mdiai.seckill.redis.key;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/5  16:30
 * @Description
 */
public abstract class BaseProfix implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    public BaseProfix(String prefix) {
        this(0, prefix);
    }


    public BaseProfix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        //0代表不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String simpleName = getClass().getSimpleName();
        return simpleName + ":" + prefix;
    }
}
