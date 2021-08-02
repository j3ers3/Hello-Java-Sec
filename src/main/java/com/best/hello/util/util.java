package com.best.hello.util;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public class util {

    // 读取文件
    public static String readContent(String file) throws Exception {
        FileInputStream input = new FileInputStream(file);
        byte[] content = new byte[2 * 1024];
        int realBytes = input.read(content);
        input.close();
        return new String(content, 0, realBytes, StandardCharsets.UTF_8);
    }

}
