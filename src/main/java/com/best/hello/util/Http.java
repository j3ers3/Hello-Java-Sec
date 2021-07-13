package com.best.hello.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Http {

    // HTTPURLConnection类
    // 是继承了URLConnection
    public static String HTTPURLConnection(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setInstanceFollowRedirects(false); // 不允许重定向
            conn.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String content;
            StringBuilder html = new StringBuilder();

            while ((content = reader.readLine()) != null) {
                html.append(content);
            }
            reader.close();
            return html.toString();

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // URLConnection类
    public static String URLConnection(String url) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            // 通过getInputStream() 读取 URL 所引用的资源数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String content;
            StringBuffer html = new StringBuffer();

            while ((content = reader.readLine()) != null) {
                html.append(content);
            }
            reader.close();
            return html.toString();

        } catch (Exception e) {
            return e.getMessage();
        }
    }
}

