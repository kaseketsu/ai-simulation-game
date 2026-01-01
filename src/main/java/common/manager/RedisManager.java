package common.manager;

import cn.hutool.core.collection.CollUtil;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.utils.ParamsCheckUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 *
 * @author F1ower
 * @since 2026-1-1
 */
@Component
@RequiredArgsConstructor
public class RedisManager {

    @Value("${spring.security.black-list-prefix}")
    private String blacklistPrefix;

    @Value("${spring.data.redis.default-expiration}")
    private Long defaultExpiration;

    /**
     * k - v 存储
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加 redis 键值
     *
     * @param key      键
     * @param value    值
     * @param expire   过期时间
     * @param timeUnit 时间单位
     */
    public void addValue(@NonNull final String key, @NonNull final Object value, @NonNull final Long expire, @NonNull final TimeUnit timeUnit) {
        ParamsCheckUtils.checkAll(key, value, expire, timeUnit);
        redisTemplate.opsForValue().setIfAbsent(key, value, expire, timeUnit);
    }

    /**
     * 添加 redis 键值
     *
     * @param key   键
     * @param value 值
     */
    public void addValue(@NonNull final String key, @NonNull final Object value) {
        addValue(key, value, defaultExpiration, TimeUnit.MILLISECONDS);
    }

    /**
     * 添加 hash 键值对到 redis
     *
     * @param key      键
     * @param hashKey  hash 键
     * @param value    值
     * @param expire   过期时间
     * @param timeUnit 时间单位
     */
    public void addHash(@NonNull final String key, @NonNull final Object hashKey, @NonNull final Object value, @NonNull final Long expire, @NonNull final TimeUnit timeUnit) {
        ParamsCheckUtils.checkAll(key, value, expire, timeUnit);
        redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
        redisTemplate.expire(key, expire, timeUnit);
    }

    /**
     * 添加 hash 键值对到 redis
     *
     * @param key     键
     * @param hashKey hash 键
     * @param value   值
     */
    public void addHash(@NonNull final String key, @NonNull final Object hashKey, @NonNull final Object value) {
        addHash(key, hashKey, value, defaultExpiration, TimeUnit.MILLISECONDS);
    }


    /**
     * 根据 hash 键获取值
     *
     * @param key     键
     * @param hashKey hash 键
     */
    public <T> T getHash(@NonNull final String key, @NonNull final Object hashKey, @NonNull final Class<T> clazz) {
        ParamsCheckUtils.checkAll(hashKey);
        Object res = redisTemplate.opsForHash().get(key, hashKey);
        if (Objects.isNull(res) || !clazz.isInstance(res)) {
            throw new BusinessException(ErrorCode.TYPE_TRANSFER_ERROR);
        }
        return clazz.cast(res);
    }

    /**
     * 添加 hash 键值对到 redis
     *
     * @param key      键
     * @param hashMap  多键值对 hash
     * @param expire   过期时间
     * @param timeUnit 时间单位
     */
    public void addMultiHash(@NonNull final String key, Map<String, Object> hashMap, @NonNull final Long expire, @NonNull final TimeUnit timeUnit) {
        ParamsCheckUtils.checkAll(hashMap);
        redisTemplate.opsForHash().putAll(key, hashMap);
        redisTemplate.expire(key, expire, timeUnit);
    }

    /**
     * 添加 hash 键值对到 redis
     *
     * @param key     键
     * @param hashMap 多键值对 hash
     */
    public void addMultiHash(@NonNull final String key, Map<String, Object> hashMap) {
        addMultiHash(key, hashMap, defaultExpiration, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取指定类型的 map
     *
     * @param key    键
     * @param pClass hash 键类型
     * @param qClass hashValue 类型
     * @param <P>    p
     * @param <Q>    q
     * @return 指定类型 map
     */
    public <P, Q> Map<P, Q> getMultiHash(@NonNull final String key, @NonNull Class<P> pClass, @NonNull final Class<Q> qClass) {
        ParamsCheckUtils.checkAll(key, pClass, qClass);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        Map<P, Q> res = new HashMap<>(entries.size());
        // 转换为指定类型的 map
        if (CollUtil.isNotEmpty(entries)) {
            for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                if (
                        ParamsCheckUtils.isSpecifiedTypeThrow(entry.getKey(), pClass) && ParamsCheckUtils.isSpecifiedTypeThrow(entry.getValue(), qClass)
                ) {
                    res.put((P) entry.getKey(), (Q) entry.getValue());
                }
            }
        }
        return res;
    }


    /**
     * 针对指定 key，批量添加 values
     *
     * @param key      键
     * @param values   值...
     * @param expire   过期时间
     * @param timeUnit 时间单位
     */
    public void addListValue(String key, List<Object> values, Long expire, TimeUnit timeUnit) {
        redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 获取指定类型的参数 List
     *
     * @param key   键
     * @param clazz 值...
     * @return 指定类型的 list
     */
    public <T> List<T> fetchListValue(String key, Class<T> clazz) {
        List<Object> values = redisTemplate.opsForList().range(key, 0, -1);
        List<T> result = new ArrayList<>();
        // 判空
        if (CollUtil.isEmpty(values)) {
            return result;
        }
        // 类型转换
        for (Object value : values) {
            if (Objects.isNull(value) || !clazz.isInstance(value)) {
                // 即时抛出，避免进一步出错
                throw new BusinessException(ErrorCode.TYPE_TRANSFER_ERROR);
            }
            result.add(clazz.cast(value));
        }
        return result;
    }

    /**
     * 获取指定类型的 value
     *
     * @param key   键
     * @param clazz 类型
     * @return 值
     */
    public <T> T getValue(String key, Class<T> clazz) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取通用类型的 value
     *
     * @param key 键
     * @return 值
     */
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除指定 key
     *
     * @param key k
     */
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 检查是否有 key
     *
     * @param key k
     * @return Y / N
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 检查是否在黑名单
     *
     * @param jti jwt 唯一标识符
     * @return Y / N
     */
    public boolean isInBlackList(String jti) {
        return redisTemplate.hasKey("%s:%s".formatted(blacklistPrefix, jti));
    }


}










