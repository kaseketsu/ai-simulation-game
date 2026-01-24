package common.service;

import common.model.dto.SysCodeAddRequest;
import common.model.entity.SysCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 码值表 服务类
 * </p>
 *
 * @author F1ower
 * @since 2026-01-03
 */
public interface ISysCodeService extends IService<SysCode> {

    /**
     * 添加码值
     *
     * @param addRequest 添加请求
     */
    void addSysCode(SysCodeAddRequest addRequest);
}
