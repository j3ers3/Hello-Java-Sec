package com.best.hello.controller;

import com.best.hello.util.Http;
import com.best.hello.util.Security;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/SSRF")
public class SSRF {

    /**
     * @poc http://127.0.0.1:8888/SSRF/URLConnection?url=http://www.baidu.com
     * @poc http://127.0.0.1:8888/SSRF/URLConnection?url=file:///etc/passwd
     */
    @GetMapping("/URLConnection")
    public String URLConnection(String url) {
        return Http.URLConnection(url);
    }


    /**
     * @poc bypass 短链接：http://127.0.0.1:8888/SSRF/HTTPURLConnection/safe?url=http://surl-8.cn/0
     */
    @GetMapping("/URLConnection/safe")
    public String URLConnectionSafe(String url) {
        if (!Security.is_http(url)){
            return "不允许非http/https协议!!!";
        }else if (Security.is_intranet(url)) {
            return "不允许访问内网!!!";
        }else{
            return Http.URLConnection(url);
        }
    }

    /**
     * @safe 不允许重定向
     */
    @GetMapping("/HTTPURLConnection/safe")
    public String HTTPURLConnection(String url) {
        if (!Security.is_http(url)){
            return "不允许非http/https协议!!!";
        }else if (Security.is_intranet(url)) {
            return "不允许访问内网!!!";
        }else{
            return Http.HTTPURLConnection(url);
        }
    }
}
