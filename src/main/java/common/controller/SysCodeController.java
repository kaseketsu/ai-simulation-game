package common.controller;

import common.annotations.ApiErrorCode;
import common.baseEntities.BaseResponse;
import common.exceptions.ErrorCode;
import common.model.dto.SysCodeAddRequest;
import common.model.entity.SysCode;
import common.service.ISysCodeService;
import common.utils.ResultUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 码值表 前端控制器
 * </p>
 *
 * @author F1ower
 * @since 2026-01-03
 */
@RestController
@RequestMapping("/sysCode")
public class SysCodeController {

    @Resource
    private ISysCodeService sysCodeService;

    /**
     * 添加码值
     *
     * @param addRequest 添加请求
     * @return 自定义响应
     */
    @PostMapping("add")
    @ApiErrorCode(ErrorCode.SYS_CODE_ADD_ERROR)
    public BaseResponse<String> addSysCode(@RequestBody SysCodeAddRequest addRequest) {
        sysCodeService.addSysCode(addRequest);
        return ResultUtils.success("码值添加成功");
    }
}
