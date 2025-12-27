package common.utils;

import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
@Slf4j
public class JwtUtils {

    /**
     * 计算密钥 base
     */
    private String secret;

    /**
     * 请求凭证过期时间
     */
    private Long accessTokenExpire;

    /**
     * 刷新凭证过期时间
     */
    private Long refreshTokenExpire;

    /**
     * token 前缀（header）
     */
    private String tokenPrefix;

    /**
     * token header 名称
     */
    private String tokenHeader;

    /**
     * 密钥
     */
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 生成请求 token
     *
     * @param userDetails 用户信息
     * @return 请求 token
     */
    public String generateAccessToken(final UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String jti = UUID.randomUUID().toString();
        claims.put("jti", jti);
        claims.put("type", "access");
        return generateToken(claims, userDetails.getUsername(), accessTokenExpire);
    }

    /**
     * 生成刷新 token
     *
     * @param userDetails 用户信息
     * @return 刷新 token
     */
    public String generateRefreshToken(final UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String jti = UUID.randomUUID().toString();
        claims.put("jti", jti);
        claims.put("type", "refresh");
        return generateToken(claims, userDetails.getUsername(), refreshTokenExpire);
    }

    /**
     * 通用的 token 生成方法
     *
     * @param claims     负荷
     * @param subject    用户名
     * @param expireTime 过期时间
     * @return token
     */
    public String generateToken(final Map<String, Object> claims, final String subject, final Long expireTime) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 获取用户名
     *
     * @param token jwt
     * @return 用户名
     */
    public String extractUserName(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 获取过期时间
     *
     * @param token jwt
     * @return 过期时间
     */
    public Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 获取唯一标识
     *
     * @param token jwt
     * @return jti
     */
    public String extractJti(final String token) {
        return extractClaim(token, claims -> claims.get("jti", String.class));
    }

    /**
     * 通用获取属性方法
     *
     * @param token          jwt
     * @param claimsResolver 函数
     * @return 所需属性值
     */
    public <T> T extractClaim(final String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从 token 获取负荷
     *
     * @param token jwt
     * @return 负荷信息
     */
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("token 负载解析失败，原因是: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.PARSE_ERROR, "token 解析失败，原因是: %s".formatted(e.getMessage()));
        }
    }

    /**
     * 判断 token 是否有效
     *
     * @param token       jwt
     * @param userDetails 用户信息
     * @return 是否有效
     */
    public boolean isValidToken(final String token, final UserDetails userDetails) {
        String userName = extractUserName(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断 token 是否过期
     *
     * @param token jwt
     * @return 是否过期
     */
    public boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }
}
