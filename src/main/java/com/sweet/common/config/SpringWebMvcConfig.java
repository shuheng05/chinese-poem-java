package com.sweet.common.config;


import com.sweet.common.interceptor.LoginHandlerInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC 配置
 * @author shuheng
 */
@Configuration
public class SpringWebMvcConfig implements WebMvcConfigurer {
    @Resource
    private LoginHandlerInterceptor loginHandlerInterceptor;

    /**
     * 注册登录拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginHandlerInterceptor)
                // 拦截的请求
                .addPathPatterns("/poems/like")
                .addPathPatterns("/poems/dislike")
                .addPathPatterns("/poems/favorite")
                .addPathPatterns("/poems/isLikeDislikeFavorite")
                .addPathPatterns("/user/info")
                .addPathPatterns("/user/getFavoritePoems")
                // 不拦截的请求（放行）
                .excludePathPatterns("/user/login")
                .excludePathPatterns(("/user/signup"))
                .excludePathPatterns("/user/getCode")
               ;
    }
}
