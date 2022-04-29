package com.best.hello.controller.Deserialize;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

@Slf4j
@RestController
@RequestMapping("/Deserialize/yaml")
public class YamlVul {


    @ApiOperation(value = "vul：SnakeYaml 反序列化漏洞", notes = "yaml是用来处理数据的，相对于xml和json来说较少见")
    @PostMapping("/vul")
    public void yaml(String content) {
        Yaml y = new Yaml();
        y.load(content);
        log.info("[vul] SnakeYaml反序列化: " + content);
    }
}
