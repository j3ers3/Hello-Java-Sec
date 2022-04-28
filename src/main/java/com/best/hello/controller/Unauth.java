package com.best.hello.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("接口未授权")
@Slf4j
@RestController
@RequestMapping("/Unauth")
public class Unauth {

    @GetMapping("/api/info")
    public String vul() {
        log.info("[vul] 未授权访问");
        return "未授权访问\n张伟 330102199003072495 13888888888";
    }
}
