package common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import common.exceptions.ErrorCode;
import common.model.dto.SysCodeAddRequest;
import common.model.entity.SysCode;
import common.dao.SysCodeMapper;
import common.service.ISysCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import common.utils.ParamsCheckUtils;
import common.utils.ThrowUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 码值表 服务实现类
 * </p>
 *
 * @author F1ower
 * @since 2026-01-03
 */
@Service
public class SysCodeServiceImpl extends ServiceImpl<SysCodeMapper, SysCode> implements ISysCodeService {

    /**
     * 添加码值
     *
     * @param addRequest 添加请求
     */
    @Override
    public void addSysCode(SysCodeAddRequest addRequest) {
        // 参数校验
        ThrowUtils.throwIf(Objects.isNull(addRequest), ErrorCode.PARAM_ERROR);
        String codeCategory = addRequest.getCodeCategory();
        String codeKey = addRequest.getCodeKey();
        String codeValue = addRequest.getCodeValue();
        ParamsCheckUtils.checkAll(codeCategory, codeKey, codeValue);
        // 查看是否有相同码值
        LambdaQueryWrapper<SysCode> sysCodeWrapper = new LambdaQueryWrapper<>();
        sysCodeWrapper.eq(SysCode::getCodeCategory, codeCategory)
                .eq(SysCode::getCodeKey, codeKey);
        long count = this.count(sysCodeWrapper);
        ThrowUtils.throwIf(count > 0, ErrorCode.ALREADY_EXIST_ERROR, "存在相同码值");
        // 添加码值
        SysCode sysCode = new SysCode();
        sysCode.setCodeCategory(codeCategory);
        sysCode.setCodeKey(codeKey);
        sysCode.setCodeValue(codeValue);
        boolean saveRes = this.save(sysCode);
        ThrowUtils.throwIf(!saveRes, ErrorCode.SYS_CODE_ADD_ERROR, "码值保存失败");
    }
}
