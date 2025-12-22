package org.family.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class RedisUtil {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    // ===================== 通用方法 =====================
    
    /**
     * 设置缓存
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
     * 设置缓存并指定过期时间
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
     * 获取缓存
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }
    
    /**
     * 获取缓存并指定类型
     */
    public <T> T get(String key, Class<T> clazz) {
        Object value = get(key);
        return clazz.isInstance(value) ? clazz.cast(value) : null;
    }
    
    /**
     * 删除缓存
     */
    public boolean delete(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.delete(key));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 批量删除
     */
    public boolean delete(Collection<String> keys) {
        try {
            Long count = redisTemplate.delete(keys);
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 判断 key 是否存在
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 设置过期时间
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                return Boolean.TRUE.equals(redisTemplate.expire(key, time, TimeUnit.SECONDS));
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ===================== 对象缓存 =====================
    
    /**
     * 缓存对象
     */
    public boolean cacheObject(String key, Object obj, long expireSeconds) {
        try {
            redisTemplate.opsForValue().set(key, obj, expireSeconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 获取缓存对象
     */
    public <T> T getObject(String key, Class<T> clazz) {
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj == null) {
                return null;
            }
            return clazz.cast(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // ===================== 列表缓存 =====================
    
    /**
     * 缓存列表
     */
    public boolean cacheList(String key, List<?> list, long expireSeconds) {
        try {
            redisTemplate.opsForList().rightPushAll(key, list.toArray());
            if (expireSeconds > 0) {
                expire(key, expireSeconds);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 获取缓存列表
     */
    public <T> List<T> getList(String key, Class<T> clazz) {
        try {
            List<Object> objects = redisTemplate.opsForList().range(key, 0, -1);
            if (objects == null) {
                return new ArrayList<>();
            }
            
            List<T> result = new ArrayList<>();
            for (Object obj : objects) {
                if (clazz.isInstance(obj)) {
                    result.add(clazz.cast(obj));
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // ===================== 分布式锁 =====================
    
    /**
     * 获取分布式锁
     */
    public boolean tryLock(String lockKey, String requestId, long expireSeconds) {
        try {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(
                lockKey, 
                requestId, 
                expireSeconds, 
                TimeUnit.SECONDS
            );
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 释放分布式锁
     */
    public boolean releaseLock(String lockKey, String requestId) {
        try {
            String currentValue = (String) redisTemplate.opsForValue().get(lockKey);
            if (requestId.equals(currentValue)) {
                return Boolean.TRUE.equals(redisTemplate.delete(lockKey));
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ===================== 缓存穿透保护 =====================
    
    /**
     * 获取缓存，如果不存在则从数据库加载
     */
    public <T> T getOrLoad(String key, Class<T> clazz,
                           Supplier<T> loader, long expireSeconds) {
        // 先查缓存
        T value = getObject(key, clazz);
        
        if (value != null) {
            // 如果是空对象占位符，返回null
            if (value instanceof String && "NULL_PLACEHOLDER".equals(value)) {
                return null;
            }
            return value;
        }
        
        // 缓存不存在，从数据库加载
        T dbValue = loader.get();
        
        if (dbValue == null) {
            // 防止缓存穿透：缓存空值
            cacheObject(key, "NULL_PLACEHOLDER", 300); // 5分钟
            return null;
        }
        
        // 缓存数据库查到的数据
        cacheObject(key, dbValue, expireSeconds);
        return dbValue;
    }
}