package common.filters;

import cn.hutool.core.util.StrUtil;
import common.manager.JwtManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * jwt 权限认证过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;

    private final UserDetailsService userDetailsService;

    /**
     * 实现权限认证
     *
     * @param request     servlet 请求
     * @param response    servlet 响应
     * @param filterChain 过滤器链
     * @throws ServletException e1
     * @throws IOException      e2
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // 获取 token
            String token = jwtManager.extractTokenFromHttpRequest(request);
            if (StrUtil.isBlank(token)) {
                filterChain.doFilter(request, response);
                return;
            }
            // 获取用户名
            String userName = jwtManager.extractUserName(token);
            if (StrUtil.isBlank(userName) || SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }
            // 根据用户名获取 userDetail
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            // 验证 jwt 有效性
            if (!jwtManager.isValidToken(token, userDetails)) {
                filterChain.doFilter(request, response);
                return;
            }
            // 放入 securityContext 上下文
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // 额外的上下文信息
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            log.error("权限过滤失败，原因是: {}", e.getMessage(), e);
        }
        filterChain.doFilter(request, response);
    }
}
