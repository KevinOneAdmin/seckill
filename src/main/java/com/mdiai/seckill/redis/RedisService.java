package com.mdiai.seckill.redis;

import com.mdiai.seckill.common.utils.JsonUtils;
import com.mdiai.seckill.redis.key.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/5  15:30
 * @Description redis服务
 */
@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取单个对象
     *
     * @param prefix
     * @param key
     * @param clasz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clasz) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String reallKey = prefix.getPrefix() + ":" + key;
            String sobj = resource.get(reallKey);
            T t = JsonUtils.stringToBean(sobj, clasz);
            return t;
        } finally {
            returnToPool(resource);
        }

    }

    /**
     * 设置单个对象
     *
     * @param prefix
     * @param key
     * @param vlue
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix prefix, String key, T vlue) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String obj = JsonUtils.beanToString(vlue);
            if (null == obj || obj.length() < 1) {
                return false;
            }
            String reallKey = prefix.getPrefix() + ":" + key;
            int seconds = prefix.expireSeconds();
            if (seconds <= 0) {
                resource.set(reallKey, obj);
            } else {
                resource.setex(reallKey, seconds, obj);
            }
            return true;
        } finally {
            returnToPool(resource);
        }

    }


    /**
     * 删除key
     *
     * @param prefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix prefix, String key) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String reallKey = prefix.getPrefix() + ":" + key;
            long stat = resource.del(reallKey);
            return stat > 0;
        } finally {
            returnToPool(resource);
        }

    }

    /**
     * 判断是否存在
     *
     * @param prefix
     * @param key
     * @return
     */
    public boolean exists(KeyPrefix prefix, String key) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String reallKey = prefix.getPrefix() + ":" + key;
            return resource.exists(reallKey);
        } finally {
            returnToPool(resource);
        }

    }

    /**
     * 自增
     *
     * @param prefix
     * @param key
     * @return
     */
    public long incr(KeyPrefix prefix, String key) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String reallKey = prefix.getPrefix() + ":" + key;
            return resource.incr(reallKey);
        } finally {
            returnToPool(resource);
        }

    }

    /**
     * 自减
     *
     * @param prefix
     * @param key
     * @return
     */
    public long decr(KeyPrefix prefix, String key) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String reallKey = prefix.getPrefix() + ":" + key;
            return resource.decr(reallKey);
        } finally {
            returnToPool(resource);
        }

    }


    private void returnToPool(Jedis resource) {
        if (null != resource) {
            resource.close();
        }
    }

}
