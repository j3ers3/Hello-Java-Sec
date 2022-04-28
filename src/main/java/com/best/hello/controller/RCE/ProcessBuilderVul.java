package com.best.hello.controller.RCE;

import com.best.hello.util.Security;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@RestController
@RequestMapping("/RCE/ProcessBuilder")
public class ProcessBuilderVul {

    /**
     * @poc http://127.0.0.1:8888/RCE/ProcessBuilder/vul?filepath=/tmp;whoami
     */
    @ApiOperation(value = "vul：ProcessBuilder", notes = "调用ProcessBuilder执行ls命令，接收参数filepath，拼接命令语句")
    @RequestMapping("/vul")
    public static String vul(String filepath) {

        String[] cmdList = {"sh", "-c", "ls -l " + filepath};
        StringBuilder sb = new StringBuilder();
        String line;

        log.info("[vul] 执行ProcessBuilder：" + filepath);

        // 利用指定的操作系统程序和参数构造一个进程生成器
        ProcessBuilder pb = new ProcessBuilder(cmdList);
        pb.redirectErrorStream(true);

        // 使用此进程生成器的属性启动一个新进程
        Process process = null;
        try {
            process = pb.start();
            // 取得命令结果的输出流
            InputStream fis = process.getInputStream();
            // 用一个读输出流类去读
            InputStreamReader isr = new InputStreamReader(fis);
            // 用缓存器读行
            BufferedReader br = new BufferedReader(isr);
            //直到读完为止
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * @poc http://127.0.0.1:8888/RCE/ProcessBuilder/safe?filepath=/tmp;whoami
     */
    @ApiOperation(value = "safe: 过滤特殊字符")
    @RequestMapping("/safe")
    public static String processbuilderSafe(String filepath) {

        if (!Security.checkOs(filepath)) {

            String[] cmdList = {"sh", "-c", "ls -l " + filepath};
            StringBuilder sb = new StringBuilder();
            String line;

            java.lang.ProcessBuilder pb = new java.lang.ProcessBuilder(cmdList);
            pb.redirectErrorStream(true);

            Process process = null;
            try {
                process = pb.start();
                InputStream fis = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        } else {
            return "检测到非法命令注入";
        }
    }

}
