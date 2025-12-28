package common.filters;

import cn.hutool.core.util.StrUtil;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.manager.JwtManager;
import common.manager.RedisManager;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 黑名单过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtBlackListFilter extends OncePerRequestFilter {

    private final RedisManager redisManager;

    private final JwtManager jwtManager;

    /**
     * 黑名单过滤
     *
     * @param request     servlet 请求
     * @param response    servlet 响应
     * @param filterChain 过滤器链
     * @throws ServletException e1
     * @throws IOException      e2
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtManager.extractTokenFromHttpRequest(request);
            if (StrUtil.isNotBlank(token)) {
                String jti = jwtManager.extractJti(token);
                if (redisManager.isInBlackList(jti)) {
                    String userName = jwtManager.extractUserName(token);
                    log.warn("黑名单用户，已拦截，用户名为: {}", userName);
                    throw new BusinessException(ErrorCode.ILLEGAL_USER, "非法用户，无访问权限");
                }
            }
        } catch (Exception e) {
            log.error("黑名单过滤失败，原因是: {}", e.getMessage(), e);
        }
        filterChain.doFilter(request, response);
    }
}
