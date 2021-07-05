package com.best.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class Login {
    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        if ("admin".equals(username) && "123456".equals(password))
            return "redirect:/index";
        else {
            model.addAttribute("msg", "用户名或者密码错误"); //显示错误信息
            return "admin789";
        }
    }
}
