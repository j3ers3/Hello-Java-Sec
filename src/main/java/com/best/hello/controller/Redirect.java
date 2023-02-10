package com.best.hello.controller;

import com.best.hello.util.Security;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 审计的函数
 * 1. redirect
 * 2. forward
 * 3. setHeader
 */
@Api("重定向漏洞")
@Controller
@RequestMapping("/Redirect")
public class Redirect {
    Logger log = LoggerFactory.getLogger(Redirect.class);

    // http://127.0.0.1:8888/Redirect/vul?url=http://www.baidu.com
    @ApiOperation(value = "vul: Spring Redirect")
    @GetMapping("/vul")
    public String vul(String url) {
        log.info("[vul] 重定向访问：" + url);
        return "redirect:" + url;
    }


    // http://127.0.0.1:8888/Redirect/vul2?url=www.baidu.com
    @ApiOperation(value = "vul: Servlet Redirect")
    @GetMapping("/vul2")
    public ModelAndView vul2(String url) {
        return new ModelAndView("redirect://" + url);
    }


    // http://127.0.0.1:8888/Redirect/vul3?url=http://www.baidu.com
    @ApiOperation(value = "vul: response.sendRedirect")
    @GetMapping("/vul3")
    public void vul3(String url, HttpServletResponse response) throws IOException {
        response.sendRedirect(url);
    }


    // http://127.0.0.1:8888/Redirect/vul4?url=http://www.baidu.com
    @ApiOperation(value = "vul: response.setStatus&response.setHeader")
    @GetMapping("/vul4")
    public void vul4(String url, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", url);
    }


    @ApiOperation(value = "safe: 白名单模式")
    @GetMapping("/safe")
    @ResponseBody
    public String safe(String url) {
        log.info("[safe] 安全重定向");
        if (Security.isWhite(url)) {
            return "安全域名：" + url;
        } else {
            return "非法重定向域名！！";
        }
    }

}
