package com.flower.game.cultivation;

import com.flower.game.cultivator.models.dto.CultivationCreateRequest;
import com.flower.game.cultivator.service.CultivationService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 修士创建测试类
 */
@SpringBootTest
public class CultivatorCreateTest {

    @Resource
    CultivationService cultivationService;

    @Test
    public void test() {
        CultivationCreateRequest cultivationCreateRequest = new CultivationCreateRequest();
        cultivationCreateRequest.setBatchSize(10);
        cultivationCreateRequest.setRegion(-1);
        cultivationCreateRequest.setStoreType(-1);
        cultivationService.createCultivationByBatch(cultivationCreateRequest);
    }
}
