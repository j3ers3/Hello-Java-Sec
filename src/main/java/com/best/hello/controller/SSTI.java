package com.best.hello.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * SSTI （服务端模板注入）
 *
 * @author nul1
 * @date 2021/6/30
 */
@Api("服务端模版注入")
@Slf4j
@Controller
@RequestMapping("/SSTI")
public class SSTI {

    /**
     * @poc http://127.0.0.1:8888/SSTI/thymeleaf?lang=index
     */
    @ApiOperation(value = "vul：thymeleaf模版注入")
    @GetMapping("/thymeleaf")
    public String thymeleaf(@RequestParam String lang) {
        return "user/" + lang + "/welcome"; //template path is tainted
    }

    /**
     * @poc http://127.0.0.1:8888/SSTI/doc/__${T(java.lang.Runtime).getRuntime().exec("open -a Calculator")}__::.x
     */
    @ApiOperation(value = "vul", notes = "将请求的url作为视图名称，调用模板引擎去解析，在这种情况下，我们只要可以控制请求的controller的参数，一样可以造成RCE漏洞")
    @GetMapping("/doc/{document}")
    public void getDocument(@PathVariable String document) {
        System.out.println(document);
    }


    @ApiOperation(value = "safe", notes = "由于controller的参数被设置为HttpServletResponse，Spring认为它已经处理了HTTP Response，因此不会发生视图名称解析")
    @GetMapping("/doc/safe/{document}")
    public void getDocument(@PathVariable String document, HttpServletResponse response) {
        System.out.println("Retrieving " + document);
    }

    /*
     * velocity模板引擎被是springboot2.0已经遗弃
     */
    @GetMapping("/velocity")
    public String velocity(Map map) {
        map.put("message", "获取用户信息");
        map.put("name", "张三");
        map.put("age", "24");
        return "velocity";
    }


}
