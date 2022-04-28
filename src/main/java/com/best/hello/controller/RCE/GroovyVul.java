package com.best.hello.controller.RCE;

import groovy.lang.GroovyShell;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/RCE/Groovy")
public class GroovyVul {

    /**
     * @poc http://127.0.0.1:8888/RCE/Groovy/vul?cmd="open -a Calculator".execute()
     */
    @ApiOperation(value = "vul：groovy类")
    @GetMapping("/vul")
    public void vul(String cmd) {
        GroovyShell shell = new GroovyShell();
        log.info("[vul] 执行groovy：" + cmd);
        shell.evaluate(cmd);
    }
}
