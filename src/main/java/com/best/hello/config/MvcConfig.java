package com.best.hello.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 视图跳转
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 路由 -> 视图.html
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/index/xss").setViewName("xss");
        registry.addViewController("/index/rce").setViewName("rce");
        registry.addViewController("/index/spel").setViewName("spel");
        registry.addViewController("/index/ssti").setViewName("ssti");
        registry.addViewController("/index/sqli/jdbc").setViewName("sqli_jdbc");
        registry.addViewController("/index/sqli/mybatis").setViewName("sqli_mybatis");
        registry.addViewController("/index/ssrf").setViewName("ssrf");
        registry.addViewController("/index/traversal").setViewName("traversal");
        registry.addViewController("/index/xxe").setViewName("xxe");
        registry.addViewController("/index/deserialize").setViewName("deserialize");
        registry.addViewController("/index/redirect").setViewName("redirect");
        registry.addViewController("/index/actuator").setViewName("actuator");
        registry.addViewController("/index/broken_access_control").setViewName("bac");
        registry.addViewController("/index/upload").setViewName("upload");
        registry.addViewController("/index/password").setViewName("password");
        registry.addViewController("/index/xstream").setViewName("xstream");
        registry.addViewController("/index/fastjson").setViewName("fastjson");

    }

    /**
     * 拦截器，判断是否登录成功
     * todo
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/login", "/", "/css/**", "/js/**", "/img/**");
    }
}
