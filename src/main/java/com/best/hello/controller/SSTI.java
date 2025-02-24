package com.best.hello.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SSTI （服务端模板注入）
 *
 * @author nul1
 * @date 2022/09/20
 */
@Api("服务端模版注入")
@Controller
@RequestMapping("/vulnapi/SSTI")
public class SSTI {
    Logger log = LoggerFactory.getLogger(SSTI.class);

    /**
     * Spring Boot Thymeleaf 模板注入
     * 当请求/path?lang=en时服务器去自动拼接待查找的模板文件名，为resources/templates/lang/en.html
     *
     * @poc http://127.0.0.1:8888/SSTI/thymeleaf/vul?lang=__$%7bnew%20java.util.Scanner(T(java.lang.Runtime).getRuntime().exec(%27whoami%27).getInputStream()).next()%7d__::.x
     */
    @ApiOperation(value = "vul：thymeleaf模版注入")
    @GetMapping("/thymeleaf/vul")
    public String thymeleafVul(@RequestParam String lang) {
        // 模版文件参数可控
        return "lang/" + lang;
    }

    @ApiOperation(value = "safe：thymeleaf白名单")
    @GetMapping("/thymeleaf/safe")
    public String thymeleafSafe(@RequestParam String lang) {
        List<String> white_list = new ArrayList<String>();
        white_list.add("en");
        white_list.add("zh");

        if (white_list.contains(lang)) {
            return "lang/" + lang;
        } else {
            return "commons/401";
        }
    }

    /**
     * 根据spring boot定义，如果controller无返回值，则以GetMapping的路由为视图名称，即将请求的url作为视图名称，调用模板引擎去解析，在这种情况下，我们只要可以控制请求的controller的参数，一样可以造成RCE漏洞
     *
     * @poc http://127.0.0.1:8888/SSTI/doc/vul/__${T(java.lang.Runtime).getRuntime().exec("open -a Calculator")}__::.x
     */
    @ApiOperation(value = "vul：url作为视图名")
    @GetMapping("/doc/vul/{document}")
    public void getDocument(@PathVariable String document) {
        log.info("[vul] SSTI payload: " + document);
    }


    @ApiOperation(value = "safe", notes = "由于controller的参数被设置为HttpServletResponse，Spring认为它已经处理了HTTP Response，因此不会发生视图名称解析")
    @GetMapping("/doc/safe/{document}")
    public void getDocument(@PathVariable String document, HttpServletResponse response) {
        log.info("[safe] SSTI payload: " + document);
    }

    /**
     * SpringBoot Thymeleaf 片段选择器注入
     * @poc http://127.0.0.1:8888/vulnapi/SSTI/thymeleaf/fragment/vul?section=__$%7bnew%20java.util.Scanner(T(java.lang.Runtime).getRuntime().exec(%22open%20-a%20Calculator%22).getInputStream()).next()%7d__::.x
     */
    @ApiOperation(value = "val：url作为片段选择器")
    @GetMapping("/thymeleaf/fragment/vul")
    public String fragmentVul(@RequestParam String section) {
        return "lang/en :: " + section;
    }

    /**
     * 设置 @ResponseBody 注解告诉 Spring 将返回值作为响应体处理，而不再是视图名称，因此无法进行模版注入攻击
     */
    @ApiOperation(value = "safe", notes = "由于设置 @ResponseBody 注解告诉 Spring 将返回值作为响应体处理，而不再是视图名称，因此无法进行模版注入攻击")
    @GetMapping("/thymeleaf/fragment/safe")
    @ResponseBody
    public String fragmentSafe(@RequestParam String section) {
        return "lang/en :: " + section;
    }
}
