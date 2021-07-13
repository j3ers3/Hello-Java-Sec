package com.best.hello.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 存放安全类
 */
public class Security {

    /**
     * 判断是否为内网地址
     *
     * @return True or False
     */
    public static boolean is_intranet(String url) {
        Pattern reg = Pattern.compile("^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$");
        Matcher match = reg.matcher(url);
        Boolean a = match.find();
        return a;
    }

    /**
     * 判断是否是http类型
     *
     * @return True or False
     */
    public static boolean is_http(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    /**
     * 判断url是否在白名单内
     */
    public static boolean is_white(String url) {
        List<String> url_list = new ArrayList<String>();
        url_list.add("baidu.com");
        url_list.add("www.baidu.com");
        url_list.add("oa.baidu.com");

        // 从url转换host

        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            System.out.print(e);
        }
        String host = uri.getHost().toLowerCase();
        System.out.println(host);

        return url_list.contains(host);

    }

}