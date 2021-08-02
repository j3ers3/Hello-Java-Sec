package com.best.hello.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 用户登录成功后获取session
        Object session = request.getSession().getAttribute("LoginUser");
        System.out.println("[*] session用户：" + session);
        if (session == null) {
            request.setAttribute("msg", "请先登录");
            request.getRequestDispatcher("/login").forward(request, response);
            return false;
        } else {
            return true;
        }
    }
}
