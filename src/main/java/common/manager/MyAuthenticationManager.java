//package common.manager;
//
//import jakarta.annotation.Resource;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.stereotype.Component;
//
///**
// * 权限校验类
// */
//@Component
//public class MyAuthenticationManager {
//
//    @Resource
//    private AuthenticationManager authenticationManager;
//
//    /**
//     * 账号密码校验
//     *
//     * @param userAccount  账号
//     * @param userPassword 密码
//     */
//    public void authenticate(String userAccount, String userPassword) {
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userAccount, userPassword);
//        authenticationManager.authenticate(token);
//    }
//}
