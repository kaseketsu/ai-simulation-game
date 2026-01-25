//package common.config;
//
//import lombok.Setter;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.List;
//
///**
// * spring security 配置类
// */
//@Configuration
//@EnableMethodSecurity
//@ConfigurationProperties(prefix = "spring.security")
//@Setter
//public class SecurityConfig {
//
////    private List<String> ignoreUrls;
////
////    @Resource
////    private MyUserDetailServiceImpl userDetailsService;
////
////    @Resource
////    private JwtBlackListFilter jwtBlackListFilter;
////
////    @Resource
////    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
////    /**
////     * 动态盐值编码器（默认强度 10）
////     *
////     * @return 一个防暴力破解的编码器
////     */
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder(10);
////    }
//
////    /**
////     * 认证逻辑载体
////     *
////     * @return provider
////     */
////    @Bean
////    public DaoAuthenticationProvider daoAuthenticationProvider() {
////        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
////        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
////        return daoAuthenticationProvider;
////    }
////
////    /**
////     * 创建权限管理，负责调用 provider
////     *
////     * @param configuration 配置
////     * @return manger
////     * @throws Exception 生成异常
////     */
////    @Bean
////    public AuthenticationManager authenticationManager(@NonNull final AuthenticationConfiguration configuration) throws Exception {
////        return configuration.getAuthenticationManager();
////    }
//
//    /**
//     * 创建跨域配置
//     *
//     * @return 跨域配置
//     */
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        // 跨域配置
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        // 允许跨域携带 cookie
//        corsConfiguration.setAllowCredentials(true);
//        // 允许所有 url
//        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
//        // 允许 post 等方法
//        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        // 允许所有请求头
//        corsConfiguration.setAllowedHeaders(List.of("*"));
//        // 一小时内不需要
//        corsConfiguration.setMaxAge(3600L);
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//        return urlBasedCorsConfigurationSource;
//    }
//
////    /**
////     * 创建过滤器链
////     *
////     * @param http http
////     * @return 过滤器链
////     * @throws Exception 生成错误
////     */
////    @Bean
////    public SecurityFilterChain securityFilterChain(@NonNull final HttpSecurity http) throws Exception {
////        http.csrf(AbstractHttpConfigurer::disable)
////                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
////                .httpBasic(AbstractHttpConfigurer::disable)
////                .authorizeHttpRequests(
////                        // 开发阶段方便配置
////                        auth -> auth.anyRequest().permitAll()
//////                        auth -> auth.requestMatchers(ignoreUrls.toArray(new String[0])).permitAll()
//////                                .anyRequest().authenticated()
////                )
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .addFilterBefore(jwtBlackListFilter, UsernamePasswordAuthenticationFilter.class)
////                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
////                .authenticationProvider(daoAuthenticationProvider());
////        return http.build();
////    }
//}
