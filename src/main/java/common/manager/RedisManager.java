package common.manager;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis 工具类
 */
@Component
@RequiredArgsConstructor
public class RedisManager {

    @Value("${spring.security.black-list-prefix}")
    private String blacklistPrefix;

    /**
     * k - v 存储
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加 redis 键值
     *
     * @param key   键
     * @param value 值
     */
    public void addValue(String key, Object value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
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
     * @return v
     */
    public boolean deleteValue(String key) {
        return redisTemplate.delete(key);
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
        return redisTemplate.hasKey(blacklistPrefix + jti);
    }
}










