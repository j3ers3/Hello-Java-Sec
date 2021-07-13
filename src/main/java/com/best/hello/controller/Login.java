package com.best.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class Login {
    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session) {
        if ("admin".equals(username) && "admin".equals(password)){
            session.setAttribute("LoginUser", username);
            return "redirect:/index";
        } else {
            model.addAttribute("msg", "用户名或者密码错误"); //显示错误信息
            return "login";
        }
    }

    // 注销
    @RequestMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
