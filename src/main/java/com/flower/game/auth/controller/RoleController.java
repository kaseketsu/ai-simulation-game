package com.flower.game.auth.controller;

import cn.hutool.core.lang.Assert;
import com.flower.game.auth.models.dto.RoleAddRequest;
import com.flower.game.auth.service.IRoleService;
import common.annotations.ApiErrorCode;
import common.baseEntities.BaseResponse;
import common.exceptions.ErrorCode;
import common.utils.ResultUtils;
import jakarta.annotation.Resource;
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

    @Resource
    private IRoleService roleService;

    /**
     * 添加角色
     *
     * @param addRequest 添加请求
     * @return 自定义响应
     */
    @PostMapping("/add")
    @ApiErrorCode(ErrorCode.ROLE_ADD_ERROR)
    public BaseResponse<String> addRole(@RequestBody RoleAddRequest addRequest) {
        roleService.addRole(addRequest);
        return ResultUtils.success("角色添加成功");
    }
}
