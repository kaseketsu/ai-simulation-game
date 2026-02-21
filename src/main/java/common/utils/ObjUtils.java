package common.utils;

import cn.hutool.core.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Set;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;

/**
 * 针对对象的自定义工具类
 *
 * @author F1ower
 * @since 2026-2-21
 */
@Slf4j
public class ObjUtils extends ObjUtil {

    /**
     * 清除 obj 的自动生成属性（如 id、create_time、update_time、modifier, creator, is_deleted）
     *
     * @param obj 对象
     */
    public static void removeAutoParams(Object obj) {
        if (obj == null) {
            return;
        }
        final Set<String> removeParams = Set.of(
                "id", "createTime", "updateTime", "modifier", "creator", "isDeleted",
                "create_time", "update_time", "is_deleted"
        );
        Class<?> clazz = obj.getClass();
        // 递归向上删除本体和父类
        while (clazz != null && clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!removeParams.contains(field.getName())) {
                    continue;
                }
                int mod = field.getModifiers();
                if (isStatic(mod) || isFinal(mod)) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    if (!type.isPrimitive()) {
                        field.set(obj, null);
                    } else {
                        // 基本类型赋默认值
                        if (type == boolean.class) field.setBoolean(obj, false);
                        else if (type == byte.class) field.setByte(obj, (byte) 0);
                        else if (type == short.class) field.setShort(obj, (short) 0);
                        else if (type == int.class) field.setInt(obj, 0);
                        else if (type == long.class) field.setLong(obj, 0L);
                        else if (type == float.class) field.setFloat(obj, 0f);
                        else if (type == double.class) field.setDouble(obj, 0d);
                        else if (type == char.class) field.setChar(obj, '\0');
                    }
                } catch (Exception e) {
                    log.error("属性清除失败, 类: {}, 属性名: {}, 原因: {}",
                            clazz.getName(), field.getName(), e.getMessage());
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}
