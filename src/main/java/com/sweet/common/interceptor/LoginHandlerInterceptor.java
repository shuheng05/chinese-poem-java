package com.sweet.common.interceptor;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.sweet.common.utils.JwtUtil;
import com.sweet.common.utils.UserIdUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 登录拦截器
 * @author shuheng
 */
@Component
@Slf4j
public class LoginHandlerInterceptor implements HandlerInterceptor {

    /**
     * 在目标方式执行之前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // 令牌建议是放在请求头中，获取请求头中令牌
        String token = request.getHeader("token");
        System.out.println(token);
        if (token == null) {
            return  false;
        }

        try {
            // 解析令牌获取用户id
            DecodedJWT tokenInfo = JwtUtil.getTokenInfo(token);
            String userId = tokenInfo.getClaim("id").toString();
            userId = userId.substring(1, userId.length() - 1);
            UserIdUtil.setCurrentUserId(userId);
            // 验证令牌
            JwtUtil.verify(token);
            return true;  // 放行请求
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

