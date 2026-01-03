package com.flower.game.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flower.game.auth.models.entity.Role;
import com.flower.game.auth.service.IRoleService;
import com.flower.game.user.dao.UserMapper;
import com.flower.game.user.models.dto.*;
import com.flower.game.user.models.entity.User;
import com.flower.game.user.models.entity.UserRole;
import com.flower.game.user.service.IUserRoleService;
import com.flower.game.user.service.IUserService;
import com.flower.game.user.service.TokenService;
import common.annotations.ExceptionLog;
import common.enums.RoleEnum;
import common.exceptions.ErrorCode;
import common.manager.MyAuthenticationManager;
import common.manager.RedisManager;
import common.utils.ParamsCheckUtils;
import common.utils.ThrowUtils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户基础表 服务实现类
 * </p>
 *
 * @author F1ower
 * @since 2025-12-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private MyAuthenticationManager myAuthenticationManager;

    @Resource
    private TokenService tokenService;

    @Resource
    private RedisManager redisManager;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private IRoleService roleService;

    @Resource
    private IUserRoleService userRoleService;

    @Value("${spring.jwt.access-token-prefix}")
    private String accessTokenPrefix;

    @Value("${spring.jwt.refresh-token-prefix}")
    private String refreshTokenPrefix;

    @Value("${spring.jwt.default-kick-out}")
    private Long defaultKickOut;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册请求
     */
    @Override
    @ExceptionLog("用户注册失败")
    @Transactional(rollbackFor = Exception.class)
    public void userRegister(@Nonnull UserRegisterRequest userRegisterRequest) {
        // 获取参数
        String userName = userRegisterRequest.getUserName();
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        // 空值校验
        ParamsCheckUtils.checkAll(userAccount, userPassword, checkPassword);
        // 查看用户是否存在
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getUserAccount, userAccount)
                .eq(User::getIsDeleted, 0);
        long count = count(userWrapper);
        ThrowUtils.throwIf(count > 0, ErrorCode.PARAM_ERROR, "用户已存在");
        // 长度检验
        ParamsCheckUtils.lengthCheck(6, 12, userName, userAccount, userPassword, checkPassword);
        // 密码校验
        ThrowUtils.throwIf(!StrUtil.equals(userPassword, checkPassword), ErrorCode.PARAM_ERROR, "密码和确认密码不一致");
        // 密码加密
        String encodedPassword = passwordEncoder.encode(userPassword);
        // 存入数据库
        User userDO = new User();
        userDO.setUserName(userName);
        userDO.setUserAccount(userAccount);
        userDO.setUserPassword(encodedPassword);
        boolean saveRes = save(userDO);
        ThrowUtils.throwIf(!saveRes, ErrorCode.OPERATION_ERROR, "用户注册失败");
        // 默认角色是 user
        UserRole userRole = new UserRole();
        userRole.setUserId(userDO.getId());
        LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.eq(Role::getRoleCode, RoleEnum.USER.getRoleCode())
                .eq(Role::getIsDeleted, 0);
        Role role = roleService.getOne(roleWrapper);
        userRole.setRoleId(role.getId());
        userRoleService.save(userRole);
    }

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return jwt
     */
    @Override
    @ExceptionLog("用户登录失败")
    public JwtResponse userLogin(@NonNull UserLoginRequest loginRequest) {
        // 获取用户名和密码，用 AuthenticationManger 进行校验
        String userAccount = loginRequest.getUserAccount();
        String userPassword = loginRequest.getUserPassword();
        myAuthenticationManager.authenticate(userAccount, userPassword);
        // 获取并返回 jwt
        return tokenService.generateToken(userAccount);
    }

    /**
     * 获取新的 accessToken，并更新 refreshToken
     *
     * @param refreshRequest 刷新请求
     * @return 新的 token
     */
    @Override
    @ExceptionLog("token 刷新失败")
    public JwtResponse refreshToken(@NonNull TokenRefreshRequest refreshRequest) {
        // 获取 refreshToken
        String refreshToken = refreshRequest.getRefreshToken();
        // 根据 refreshToken 获取新的 token
        return tokenService.refreshToken(refreshToken);
    }


    /**
     * 踢用户下线
     *
     * @param userAccount 用户账号
     */
    @Override
    @ExceptionLog("用户下线失败")
    public void invalidateToken(@NonNull String userAccount) {
        // 获取 map
        Map<String, String> multiHash = redisManager.getMultiHash(userAccount, String.class, String.class);
        // 获取 token
        if (!Objects.isNull(multiHash)) {
            // 拉黑并删除 refreshToken
            String refreshToken = multiHash.get(refreshTokenPrefix);
            if (StrUtil.isNotBlank(refreshToken)) {
                tokenService.invalidateToken(refreshToken, defaultKickOut, TimeUnit.MILLISECONDS);
            }
            // 拉黑并删除 accessToken
            String accessToken = multiHash.get(accessTokenPrefix);
            if (StrUtil.isNotBlank(accessToken)) {
                tokenService.invalidateToken(accessToken, defaultKickOut, TimeUnit.MILLISECONDS);
            }
            // 删除 k-v 结构
            redisManager.deleteValue(userAccount);
        }
    }

    /**
     * 用户主动下线
     *
     * @param logoutRequest 下线请求
     */
    @Override
    public void userLogOut(@NonNull UserLogOutRequest logoutRequest) {
        invalidateToken(logoutRequest.getUserAccount());
    }
}
