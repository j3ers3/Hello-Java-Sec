package com.best.hello.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/*
 * Fastjson 是一个 Java 库，可以将 Java 对象转换为 JSON 格式，当然它也可以将 JSON 字符串转换为 Java 对象
 * Github：https://github.com/alibaba/fastjson/wiki/Quick-Start-CN
 *
 */

@RestController
public class Fastjson {

    @RequestMapping(value = "/fastjson", method = {RequestMethod.POST})
    /*
     * @RequestBody 接收的参数来自请求body中，
     * 就application/json类型的数据而言，使用注解@RequestBody可以将body里面所有的json数据传到后端，后端再进行解析。
     *
     * 触发条件：Fastjson版本小于等于1.2.24、Java版本小于8u121
     */
    public String rce(@RequestBody String params) {

        try {
            // 转换成object
            JSONObject json_ob = JSON.parseObject(params);
            System.out.println(json_ob);

            // 获取ob中name字段;
            return json_ob.get("name").toString();

        } catch (Exception e) {
            return e.toString();
        }
    }

}
