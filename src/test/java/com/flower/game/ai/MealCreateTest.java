package com.flower.game.ai;

import cn.hutool.json.JSONUtil;
import com.flower.game.ai.manager.QwenManager;
import com.flower.game.ai.models.entity.MealCreateMessage;
import com.flower.game.ai.models.entity.NameCreateResponse;
import com.flower.game.dish.models.dto.NewMealGenerateRequest;
import common.exceptions.BusinessException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 灵膳合成测试类
 *
 * @author F1ower
 * @since 2026-2-16
 */
@SpringBootTest
@Slf4j
public class MealCreateTest {

    @Resource
    private QwenManager qwenManager;

    @Test
    public void nameCreateTest() {
        NewMealGenerateRequest newMealGenerateRequest = new NewMealGenerateRequest();
        newMealGenerateRequest.setMainIngredient("紫韵灵果");
        newMealGenerateRequest.setSideIngredient("赤焰灵肉");
        newMealGenerateRequest.setMainIngredientRarity(3);
        newMealGenerateRequest.setSideIngredientRarity(2);
        newMealGenerateRequest.setMainIngredientDescription("玉髓灵叶取自千年灵树，可锁住灵果的灵力不流失，同时增添草木清香，提升食用体验。");
        newMealGenerateRequest.setSideIngredientDescription("赤焰灵肉取自赤炎兽，肉质紧实，蕴含浓郁的火系灵力，淬灵阶段修士食用可强化肉身，提升灵力运转效率。");
        newMealGenerateRequest.setMainIngredientPrice(80000L);
        newMealGenerateRequest.setSideIngredientPrice(28000L);
        newMealGenerateRequest.setSeasoning("清霖灵露");
        newMealGenerateRequest.setSeasoningDescription("淬灵锻体，压制火系灵力反噬");
        NameCreateResponse newMealName = qwenManager.createNewMealName(newMealGenerateRequest);
        log.info("新的灵膳名称为: {}", JSONUtil.toJsonPrettyStr(newMealName));
    }

    @Test
    public void imageCreateTest() {
        NewMealGenerateRequest newMealGenerateRequest = new NewMealGenerateRequest();
        newMealGenerateRequest.setMainIngredient("紫韵灵果");
        newMealGenerateRequest.setSideIngredient("赤焰灵肉");
        newMealGenerateRequest.setMainIngredientRarity(3);
        newMealGenerateRequest.setSideIngredientRarity(2);
        newMealGenerateRequest.setMainIngredientType(4);
        newMealGenerateRequest.setSideIngredientType(2);
        newMealGenerateRequest.setMainIngredientDescription("玉髓灵叶取自千年灵树，可锁住灵果的灵力不流失，同时增添草木清香，提升食用体验。");
        newMealGenerateRequest.setSideIngredientDescription("赤焰灵肉取自赤炎兽，肉质紧实，蕴含浓郁的火系灵力，淬灵阶段修士食用可强化肉身，提升灵力运转效率。");
        newMealGenerateRequest.setMainIngredientPrice(80000L);
        newMealGenerateRequest.setSideIngredientPrice(28000L);
        newMealGenerateRequest.setSeasoning("清霖灵露");
        newMealGenerateRequest.setSeasoningDescription("淬灵锻体，压制火系灵力反噬");
        String newPic = qwenManager.createNewMealPic(newMealGenerateRequest);
        log.info("新的图片url为: {}", newPic);
    }
}
