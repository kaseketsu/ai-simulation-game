package com.flower.game.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class commonTests {

    @Test
    public void simpleTest() {
        System.out.println("系统默认编码=" + java.nio.charset.Charset.defaultCharset());
        System.out.println("中文测试=用户登录失败");
        log.error("中文log测试=用户登录失败");

    }
}
