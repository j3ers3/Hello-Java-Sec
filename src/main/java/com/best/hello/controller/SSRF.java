package com.best.hello.controller;

import com.best.hello.util.Http;
import com.best.hello.util.Security;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 审计的函数
 * 1. URL
 * 2. HttpClient
 * 3. OkHttpClient
 * 4. HttpURLConnection
 * 5. Socket
 * 6. ImageIO
 * 7. DriverManager.getConnection
 * 8. SimpleDriverDataSource.getConnection
 */

@Api("SSRF")
@Slf4j
@RestController
@RequestMapping("/SSRF")
public class SSRF {

    /**
     * @poc http://127.0.0.1:8888/SSRF/URLConnection?url=http://www.baidu.com
     * @poc http://127.0.0.1:8888/SSRF/URLConnection?url=file:///etc/passwd
     */
    @ApiOperation(value = "vul：HTTPURLConnection类")
    @GetMapping("/URLConnection")
    public String URLConnection(String url) {
        log.info("[vul] SSRF：" + url);
        return Http.URLConnection(url);
    }


    /**
     * @poc http://127.0.0.1:8888/SSRF/HTTPURLConnection/safe?url=http://surl-8.cn/0
     */
    @ApiOperation(value = "vul：短链接绕过")
    @GetMapping("/URLConnection/safe")
    public String URLConnectionSafe(String url) {
        if (!Security.isHttp(url)) {
            return "不允许非http/https协议!!!";
        } else if (Security.isIntranet(url)) {
            return "不允许访问内网!!!";
        } else {
            return Http.URLConnection(url);
        }
    }


    @ApiOperation(value = "safe：不允许重定向")
    @GetMapping("/HTTPURLConnection/safe")
    public String HTTPURLConnection(String url) {
        if (!Security.isHttp(url)) {
            return "不允许非http/https协议!!!";
        } else if (Security.isIntranet(url)) {
            return "不允许访问内网!!!";
        } else {
            return Http.HTTPURLConnection(url);
        }
    }
}
