package com.best.hello.controller;

import com.best.hello.util.Security;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
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
     *
     * @poc http://127.0.0.1:8888/XSS/reflect?content=<img/src=1 onerror=alert(1)>
     */
    @GetMapping("/reflect")
    public static String reflect(String content) {
        System.out.println("[vul] 执行xss");
        return content;
    }


    /**
     * @safe 方案一、采用自带函数HtmlUtils.htmlEscape()来过滤
     */
    @GetMapping("/escape")
    public static String safe1(String content) {
        return HtmlUtils.htmlEscape(content);
    }


    /**
     * @safe 方案二、自己做一个XssFilter, 基于转义的方式
     */
    @GetMapping("/filter")
    public static String safe2(String content) {
        return Security.filterXss(content);
    }

    /**
     * @safe 富文本情况下，做黑白名单
     */
    @GetMapping("/whitelist")
    public static String safe3(String content) {
        Whitelist whitelist = (new Whitelist())
                .addTags("p", "hr", "div", "img", "span", "textarea")  // 设置允许的标签
                .addAttributes("a", "href", "title") // 设置标签允许的属性, 避免如nmouseover属性
                .addProtocols("img", "src", "http", "https")  // img的src属性只允许http和https开头
                .addProtocols("a", "href", "http", "https");
        return Jsoup.clean(content, whitelist);
    }
}
