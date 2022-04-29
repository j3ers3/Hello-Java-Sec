package com.best.hello.controller.RCE;

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
@RequestMapping("/RCE/Runtime")
public class RuntimeVul {

    /**
     * @poc http://127.0.0.1:8888/RCE/Runtime/vul?cmd=id
     */
    @ApiOperation(value = "vul：Runtime.getRuntime().exec()")
    @RequestMapping("/vul")
    public static String vul(String cmd) {
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            log.info("[vul] 执行runtime：" + cmd);

            InputStream fis = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    @ApiOperation(value = "vul：Runtime.getRuntime().load()")
    public static void main(String[] args) {
        try {
            Runtime rt = Runtime.getRuntime();
            //加载动态链接库，如linux下的so文件，windows下的dll文件
            rt.load("xx.dll");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
