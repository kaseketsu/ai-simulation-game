package common.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * 角色相关枚举
 */
@Getter
public enum RoleEnum {

    USER("0", "用户"),
    VIP("1", "vip"),
    ADMIN("9", "管理员");

    private final String roleCode;

    private final String roleName;

    RoleEnum(String roleCode, String roleName) {
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    /**
     * 根据 code 获取枚举
     *
     * @param roleCode code
     * @return 枚举
     */
    public static RoleEnum getEnumByRoleCode(String roleCode) {
        if (StrUtil.isBlank(roleCode)) {
            return null;
        }
        for (RoleEnum roleConstant : RoleEnum.values()) {
            if (roleCode.equals(roleConstant.roleCode)) {
                return roleConstant;
            }
        }
        return null;
    }
}
