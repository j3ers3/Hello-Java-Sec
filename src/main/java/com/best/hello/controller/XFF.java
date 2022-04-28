package com.best.hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/*
 * 获取真实IP
 * 1. 没有使用代理的情况下，直接从 getRemoteAddr() 获取目标真实IP
 * 2. 使用nginx等反向代理的情况下，由于在客户端和服务之间增加了中间层，因此服务器无法直接拿到客户端的 IP，这时取 X-Forwarded-For 中第一个IP得到的确实为客户端真实IP
 */

@RestController
@RequestMapping("/XFF")
public class XFF {

    /**
     * 不使用代理情况下，RemoteAddr获取真实IP
     */
    @GetMapping("/remote")
    public static String remote(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return "RemoteAddr: " + ip;
    }

    @GetMapping("/vul")
    public static String vul(HttpServletRequest request) {
        //String ip1 = request.getHeader("X-Real-IP");
        String ip2 = request.getHeader("X-Forwarded-For");
        System.out.println(ip2);
        if (!Objects.equals(ip2, "127.0.0.1")) {
            return "禁止访问，只允许本地IP！";
        } else {
            return "success，你的IP：" + ip2;
        }
    }

    /**
     * 代理情况下获取IP
     * proxy_set_header X-Real-IP $remote_addr;
     * Nginx以$remote_addr变量作为访客的真实IP，伪造添加xxf时，每个xxf在后面追加，形式：X-Forwarded-For: 客户端ip， 一级代理ip， 二级代理ip
     * proxy_set_header X-Forwarded-For $remote_addr;
     */
    @RequestMapping("/proxy")
    public static String ip(HttpServletRequest request) {
        String ip1 = request.getRemoteAddr();
        String ip3 = request.getHeader("X-Forwarded-For");

        if (ip1 != null) {
            return ip1;
        } else {
            return ip3;
        }

    }
}
