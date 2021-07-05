package com.best.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

/**
 * XSS漏洞
 *
 * @author nul1
 * @date 2021/6/29
 */

@RestController
@RequestMapping("/XSS")
public class XSS {

    /**
     * 反射型XSS
     * http://god.com:8888/XSS/reflect?content=<img/src=1 onerror=alert(1)>
     */
    @GetMapping("/reflect")
    public static String input(String content) {
        return content;
    }


    /**
     * 安全方案一
     * 采用spring的HtmlUtils.htmlEscape()来过滤
     */
    @GetMapping("/safe1")
    public static String safe1(String content) {
        return HtmlUtils.htmlEscape(content);
    }


    /**
     * 安全方案二
     * 自己做一个XssFilter，好处是对于富文本可以更灵活
     * http://god.com:8888/XSS/safe2?content=<img/src=1%20onerror=alert(1)>
     */
    @GetMapping("/safe2")
    public static String safe2(String content) {
        return XssFilter(content);
    }

    private static String XssFilter(String content) {
        content = StringUtils.replace(content, "&", "&amp;");
        content = StringUtils.replace(content, "<", "&lt;");
        content = StringUtils.replace(content, ">", "&gt;");
        content = StringUtils.replace(content, "\"", "&quot;");
        content = StringUtils.replace(content, "'", "&#x27;");
        content = StringUtils.replace(content, "/", "&#x2F;");
        return content;
    }

}
