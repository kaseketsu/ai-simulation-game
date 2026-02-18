package com.flower.game.common;

import common.manager.CosManager;
import jakarta.annotation.Resource;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * 腾讯云存储测试类
 *
 * @author F1ower
 * @since 2026-2-18
 */
@SpringBootTest
public class CosManagerTest {

    @Resource
    private CosManager cosManager;

    @Test
    public void putObjectTest() {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = new LocalDateTime();
        String res = DateTimeFormat.forPattern("yyyyMMddHHmmss").print(now);
        final String key = "/images/%s_%s.png".formatted(uuid, res);
        final String localPath = "/tempPics/testPic.png";
        cosManager.putLocalFile(key, localPath);
    }
}
