package common.utils;

import cn.hutool.core.util.StrUtil;
import common.exceptions.ErrorCode;

import java.util.HashMap;
import java.util.Map;

/**
 * threadLocal 工具类
 *
 * @author F1ower
 * @since 2026-1-31
 */
public class ThreadLocalUtils {

    private static final ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<>();

    static {
        threadLocal.set(new HashMap<>());
    }

    private ThreadLocalUtils() {
    }

    /**
     * 获取 threadLocal 存储遍历
     *
     * @return 存储遍历
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        ThrowUtils.throwIf(StrUtil.isBlank(key), ErrorCode.PARAM_ERROR);
        if (!isEmpty()) {
            return (T) threadLocal.get().get(key);
        }
        return null;
    }

    /**
     * 移除 map 中的指定 key
     *
     * @param key 键
     */
    public static void removeKey(String key) {
        if (isEmpty() || StrUtil.isBlank(key)) {
            return;
        }
        Map<Object, Object> map = threadLocal.get();
        map.remove(key);
    }

    /**
     * 判断 key 是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    public static boolean hasKey(String key) {
        if (isEmpty() || StrUtil.isBlank(key)) {
            return false;
        }
        return get(key) != null;
    }

    /**
     * 添加键值对
     *
     * @param key   键
     * @param value 值
     */
    public static <K, V> void add(K key, V value) {
        ThrowUtils.throwIf(key == null, ErrorCode.PARAM_ERROR, "键不能为空");
        if (!isEmpty()) {
            threadLocal.get().put(key, value);
        }
    }

    /**
     * 添加一整个 map
     *
     * @param map 待添加 map
     */
    public static <K, V> void addMap(Map<K, V> map) {
        ThrowUtils.throwIf(map == null || map.isEmpty(), ErrorCode.PARAM_ERROR);
        if (!isEmpty()) {
            threadLocal.get().putAll(map);
        } else {
            threadLocal.set(new HashMap<>());
            threadLocal.get().putAll(map);
        }
    }

    /**
     * 判断当前 map 是否为空
     *
     * @return 判断
     */
    public static boolean isEmpty() {
        Map<Object, Object> currMap = threadLocal.get();
        return currMap == null || currMap.isEmpty();
    }

    /**
     * 清除 threadLocal
     */
    public static void clear() {
        threadLocal.remove();
    }
}
