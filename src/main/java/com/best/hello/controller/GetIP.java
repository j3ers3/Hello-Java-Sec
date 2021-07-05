package com.best.hello.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/GetIP")
public class GetIP {
    /*
     * 没有使用代理的情况下，直接从 getRemoteAddr() 获取目标真实IP
     * 使用反向代理的情况下，由于在客户端和服务之间增加了中间层，因此服务器无法直接拿到客户端的 IP，这时取 X-Forwarded-For 中第一个IP得到的确实为客户端真实IP
     * 伪造添加xxf时，每个xxf在后面追加
     */
    @RequestMapping("/remote")
    public static String remote(HttpServletRequest request) {
        // 当使用 nginx 做反向代理时，通过 HttpServletRequest 的 getRemoteAddr() 得到的是最后一个代理所在机器的IP
        return request.getRemoteAddr();
    }

    @RequestMapping("/xxf")
    public static String xxf(HttpServletRequest request) {
        String ip1 = request.getHeader("X-Real-IP");
        String ip2 = request.getHeader("X-Forwarded-For");
        return "X-Real-IP: " + ip1 + " X-Forwarded-For: " + ip2;
    }

    // 获取真实ip
    @RequestMapping("/realIp")
    public static String ip(HttpServletRequest request) {
        String ip1 = request.getRemoteAddr();
        String ip2 = request.getHeader("X-Real-IP");
        String ip3 = request.getHeader("X-Forwarded-For");

        if (ip1 != null) {
            return ip1;
        } else if (ip2 != null) {
            return ip2;
        } else {
            return ip3;
        }

    }
}
