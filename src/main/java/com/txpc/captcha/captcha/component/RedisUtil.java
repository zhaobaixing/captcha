package com.txpc.captcha.captcha.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: captcha
 * @Package: com.txpc.captcha.captcha.component
 * @ClassName: RedisUtil
 * @Author: zbx
 * @Description:
 * @Date: 2019/5/21 17:24
 * @Version: 1.0
 */
@Component
public class RedisUtil {
    // redis值存储时间
    public static final int DEFAULT_TIME = 600;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }
    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 普通缓存获得
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取自增值,步进1
     * @param key 键
     * @return true成功 false 失败
     */
    public long incr(String key) {
        try {
            return redisTemplate.opsForValue().increment(key,1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
