package com.best.hello.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("跨站请求伪造")
@RestController
@RequestMapping("/CSRF")
public class CSRF {

}
