package common.utils;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件操作相关工具
 *
 * @author F1ower
 * @since 2026-2-18
 */
public class FileUtils {

    /**
     * 获取随机文件名
     *
     * @param suffix 后缀
     * @return 完整文件名
     */
    public static String fetchFileName(String suffix) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formatted = LocalDateTimeUtil.format(now, formatter);
        return String.format("%s_%s.%s", uuid, formatted, suffix);
    }
}
