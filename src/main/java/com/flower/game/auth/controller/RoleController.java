package com.flower.game.auth.controller;

import com.flower.game.auth.models.dto.RoleAddRequest;
import common.baseEntities.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对角色进行管理的接口
 *
 * @author F1ower
 * @since 2026-1-3
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @PostMapping("/add")
    public BaseResponse<String> addRole(@RequestBody RoleAddRequest addRequest) {
        return null;
    }
}
