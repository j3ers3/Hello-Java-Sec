package com.best.hello.controller;

import com.best.hello.util.Security;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @date 2021/07/15
 */
@Api("目录遍历")
@Slf4j
@RestController
@RequestMapping("/Traversal")
public class Traversal {

    /**
     * @poc http://127.0.0.1:8888/Traversal/download?filename=../../../../../../../etc/passwd
     */
    @ApiOperation(value = "vul：任意文件下载")
    @GetMapping("/download")
    public String download(String filename, HttpServletRequest request, HttpServletResponse response) {
        // 下载的文件路径
        String filePath = System.getProperty("user.dir") + "/logs/" + filename;
        log.info("[vul] 目录遍历：" + filePath);

        // 使用流的形式下载文件
        try {
            // 加载文件
            File file = new File(filePath);
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 设置response的Header
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            return "下载文件成功：" + filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return "未找到文件：" + filePath;
        }
    }

    /**
     * @poc http://127.0.0.1:8888/Traversal/download/safe?filename=../
     */
    @ApiOperation(value = "safe：过滤../")
    @GetMapping("/download/safe")
    public String download_safe(String filename) {

        if (!Security.checkTraversal(filename)) {
            String filePath = System.getProperty("user.dir") + "/logs/" + filename;
            return "安全路径：" + filePath;
        } else {
            return "检测到非法遍历";
        }
    }
}

