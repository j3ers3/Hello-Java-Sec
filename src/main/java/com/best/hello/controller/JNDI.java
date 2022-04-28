package com.best.hello.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.Context;
import javax.naming.InitialContext;


@Api("JNDI注入")
@Slf4j
@Controller
@RequestMapping("/JNDI")
public class JNDI {

    @ApiOperation(value = "vul：JNDI注入")
    @GetMapping("/vul")
    public void vul(String content) {
        log.info("[vul] JNDI注入：" + content);

        try {
            //String payload = "rmi://127.0.0.1:1099/beu8rp";
            // lookup：通过名字检索执行的对象，当lookup()方法的参数可控时，攻击者便能提供一个恶意的url地址来加载恶意类。

            Context ctx = new InitialContext();
            ctx.lookup(content);

        } catch (Exception e) {
            log.warn("错误消息");
        }
    }


}
