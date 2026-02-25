package com.flower.game.templates;

import cn.hutool.json.JSONUtil;
import com.flower.game.dish.models.entity.NameCreateResponse;
import com.flower.game.ai.templates.MealCreateTextTemplate;
import com.flower.game.dish.models.dto.NewMealGenerateRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 测试文本模型模板类
 */
@SpringBootTest
@Slf4j
public class TextModelTemplateTests {

    @Resource
    private MealCreateTextTemplate mealCreateTextTemplate;

    @Test
    public void mealCreateTest() {
        NewMealGenerateRequest newMealGenerateRequest = new NewMealGenerateRequest();
        newMealGenerateRequest.setUserId(7L);
        newMealGenerateRequest.setMainId(1L);
        newMealGenerateRequest.setSideId(1L);
        newMealGenerateRequest.setMainIngredient("紫韵灵果");
        newMealGenerateRequest.setSideIngredient("赤焰灵肉");
        newMealGenerateRequest.setMainIngredientRarity(3);
        newMealGenerateRequest.setSideIngredientRarity(2);
        newMealGenerateRequest.setMainIngredientType(1);
        newMealGenerateRequest.setSideIngredientType(1);
        newMealGenerateRequest.setMainIngredientDescription("玉髓灵叶取自千年灵树，可锁住灵果的灵力不流失，同时增添草木清香，提升食用体验。");
        newMealGenerateRequest.setSideIngredientDescription("赤焰灵肉取自赤炎兽，肉质紧实，蕴含浓郁的火系灵力，淬灵阶段修士食用可强化肉身，提升灵力运转效率。");
        newMealGenerateRequest.setMainIngredientPrice(80000L);
        newMealGenerateRequest.setSideIngredientPrice(28000L);
        newMealGenerateRequest.setSeasoning("清霖灵露");
        newMealGenerateRequest.setSeasoningDescription("淬灵锻体，压制火系灵力反噬");
        NameCreateResponse nameCreateResponse = mealCreateTextTemplate.fetchTextResponse(newMealGenerateRequest);
        log.info("新的灵膳名称为: {}", JSONUtil.toJsonPrettyStr(nameCreateResponse));
    }
}
