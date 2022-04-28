package com.best.hello.controller;

import com.best.hello.entity.User;
import com.best.hello.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/BAC")
public class BAC {

    @Autowired
    private UserMapper userMapper;

    @ApiOperation(value = "vul：根据name查询用户信息")
    @GetMapping("/vul/info")
    public List<User> vul(String name) {
        log.info("[vul] 水平越权查询：" + name);
        return userMapper.queryByUser(name);
    }

    @ApiOperation(value = "safe：只允许查询自己信息")
    @GetMapping("/safe/info")
    @ResponseBody
    public Object safe(String name, HttpSession session) {
        log.info("[safe] 水平越权查询：" + name);
        String loginUser = (String) session.getAttribute("LoginUser");

        if (loginUser.equals(name)) {
            return userMapper.queryByUser(name);
        } else {
            return String.format("当前登录：%s，只能查询自己的信息!", loginUser);
        }
    }


}
