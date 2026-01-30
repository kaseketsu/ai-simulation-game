package com.flower.game.character.controller;

import com.flower.game.character.service.LevelUpService;
import common.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import common.annotations.ApiErrorCode;
import common.baseEntities.BaseResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import common.exceptions.ErrorCode;
import jakarta.annotation.Resource;
import com.flower.game.character.models.dto.LevelUpRequest;




/**
 * 角色控制器
 * @author F1ower
 * @since 2026-1-29
 */
@RestController
@RequestMapping("/character")
public class CharacterController {

    @Resource
    private LevelUpService levelUpService;
    
    /**
     * 角色升级
     * @param levelUpRequest 升级请求
     * @return 升级成功消息
     */
    @PostMapping("/levelUp")
    @ApiErrorCode(ErrorCode.CHARACTER_LEVEL_UP_ERROR)
    public BaseResponse<String> levelUp(@RequestBody LevelUpRequest levelUpRequest) {
        levelUpService.levelUp(levelUpRequest);
        return ResultUtils.success("角色升级成功");
    }
}
