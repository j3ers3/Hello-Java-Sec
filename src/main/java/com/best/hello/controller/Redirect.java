package com.best.hello.controller;

import com.best.hello.util.Security;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/Redirect")
public class Redirect {

    /**
     * @poc http://127.0.0.1:8888/Redirect/vul?url=http://www.baidu.com
     */
    @GetMapping("/vul")
    public String vul(String url) {
        return "redirect:" + url;
    }

    /**
     * @safe 白名单模式
     */
    @GetMapping("/safe")
    @ResponseBody
    public String safe(String url) {
        if (Security.isWhite(url)) {
            return "安全域名：" + url;
        } else {
            return "非法重定向域名！！";
        }
    }

}
