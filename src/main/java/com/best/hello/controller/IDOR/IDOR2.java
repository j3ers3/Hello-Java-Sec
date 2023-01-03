package com.best.hello.controller.IDOR;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Api("垂直越权")
@Slf4j
@Controller
@RequestMapping("/IDOR")
public class IDOR2 {


    @GetMapping(value = "/vul/admin")
    public String vul() {
        return "idoradmin";
    }

    // 只允许admin用户可以访问管理页面
    @GetMapping(value = "/safe/admin")
    public String safe(HttpSession session) {
        if (session.getAttribute("LoginUser").equals("admin")) {
            return "idoradmin";
        } else {
            return "commons/403";
        }
    }


}
