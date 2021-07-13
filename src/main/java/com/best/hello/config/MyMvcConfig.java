package com.best.hello.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 视图跳转
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/index/xss").setViewName("xss");
        registry.addViewController("/index/rce").setViewName("rce");
        registry.addViewController("/index/spel").setViewName("spel");
        registry.addViewController("/index/ssti").setViewName("ssti");
        registry.addViewController("/index/sqli").setViewName("sqli");
        registry.addViewController("/index/ssrf").setViewName("ssrf");
        registry.addViewController("/index/ssrf").setViewName("ssrf");

    }

    /**
     * 拦截器，判断是否登录成功
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/index", "/", "/user/login", "/login", "/css/**", "/js/**", "/img/**");
    }
}
