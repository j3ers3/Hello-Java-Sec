package com.best.hello.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/test")
public class Test {

    /* 设置cookie */
    @RequestMapping(value = "/xsrf")
    public void test(HttpServletResponse response, String empId) {
        System.out.println(empId);
        Cookie cookie = new Cookie("XSRF-TOKEN", "123");
        cookie.setDomain("800best.com");
        cookie.setMaxAge(-1); // forever time
        response.addCookie(cookie);
    }


}