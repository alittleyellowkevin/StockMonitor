package com.stockmonitor.util;

import java.sql.Date;
import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 字符串结构操作
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    public void expireAt(String key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    public void setIfAbsent(String key, Object value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public void setIfAbsent(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public Double increment(String key, double delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * Hash结构操作
     */
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public void hDelete(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    public boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void hPutAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public Long hIncrement(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * List结构操作
     */
    public void lPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public void rPush(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public Object lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public Object rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public Long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * Set结构操作
     */
    public void sAdd(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    public void sRemove(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    public boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public Set<Object> sIntersect(String key, String otherKey) {
        return redisTemplate.opsForSet().intersect(key, otherKey);
    }

    public Set<Object> sUnion(String key, String otherKey) {
        return redisTemplate.opsForSet().union(key, otherKey);
    }

    public Set<Object> sDiff(String key, String otherKey) {
        return redisTemplate.opsForSet().difference(key, otherKey);
    }

    /**
     * ZSet结构操作
     */
    public void zAdd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public void zRemove(String key, Object... values) {
        redisTemplate.opsForZSet().remove(key, values);
    }

    public Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    public Long zRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    public Set<Object> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    public Set<Object> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    public Long zCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    public Long zSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    public Double zIncrScore(String key, Object value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }
}
