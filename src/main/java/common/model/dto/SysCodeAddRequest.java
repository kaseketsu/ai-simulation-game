package common.model.dto;

import lombok.Data;

/**
 * 添加 sysCode
 *
 * @author F1ower
 * @since 2026-1-3
 */
@Data
public class SysCodeAddRequest {

    /**
     * code 种类
     */
    private String codeCategory;

    /**
     * code-key
     */
    private String codeKey;

    /**
     * 中文描述
     */
    private String codeValue;
}
