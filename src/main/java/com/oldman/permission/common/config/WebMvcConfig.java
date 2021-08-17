package com.oldman.permission.common.config;

import com.oldman.permission.common.interceptor.CrossOriginInteceptor;
import com.oldman.permission.common.interceptor.JwtTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author oldman
 * @date 2021/8/15 3:02
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*registry.addInterceptor(new CrossOriginInteceptor())
                .addPathPatterns("/**");*/
        registry.addInterceptor(new JwtTokenInterceptor())
                .addPathPatterns("/sys/**")
                .excludePathPatterns("/sys/user/login","/sys/user/file/captcha");
    }
}
