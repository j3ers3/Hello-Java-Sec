package com.best.hello.controller.ComponentsVul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/*
 * Fastjson 是一个 Java 库，可以将 Java 对象转换为 JSON 格式，当然它也可以将 JSON 字符串转换为 Java 对象
 * Github：https://github.com/alibaba/fastjson/wiki/Quick-Start-CN
 *
 */
@Api("Fastjson反序列化漏洞")
@Slf4j
@RestController
@RequestMapping("/Fastjson")
public class FastjsonVul {

    @RequestMapping(value = "/vul", method = {RequestMethod.POST})
    public String vul(@RequestBody String content) {

        try {
            // 转换成object
            JSONObject jsonToObject = JSON.parseObject(content);
            log.info("[vul] Fastjson");

            return jsonToObject.get("name").toString();

        } catch (Exception e) {
            return e.toString();
        }
    }

}
