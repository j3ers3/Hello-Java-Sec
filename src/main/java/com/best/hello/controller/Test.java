package com.best.hello.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Constructor;

import java.util.Map;
import java.util.HashMap;

import org.apache.catalina.util.ServerInfo;
import com.alibaba.fastjson.JSON;


/**
 * 创建一个java类
 * <p>
 * \@Controller注解：会为该类注册一个控制器组件，解析return 的jsp,html页面，并且跳转到相应页面
 * \@RestController 会直接return值
 *
 * @date 2021/06
 */

@RestController
public class Test {

    @Value("#{1+2}")
    String a;


    @RequestMapping("/test")
    public String test() {
        return a;
    }

    @RequestMapping("/sysinfo")
    @ResponseBody  // @ResponseBody的作用其实是将java对象转为json格式的数据
    public String sysInfo() {
        Map<String, String> m = new HashMap<>();

        m.put("app", "Hello Java SEC");
        m.put("author", "nul1");
        m.put("tomcat_version", ServerInfo.getServerInfo());
        m.put("java_version", System.getProperty("java.version"));
        m.put("fastjson_version", JSON.VERSION);

        return JSON.toJSONString(m);
    }

    @GetMapping("/aabb")
    public void aa() {
        try {
            Class clazz = Class.forName("java.lang.Runtime");
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            Constructor<?> constructor = constructors[0];
            constructor.setAccessible(true);
            clazz.getMethod("exec", String.class).invoke(constructor.newInstance(), "open -a Calculator");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}
