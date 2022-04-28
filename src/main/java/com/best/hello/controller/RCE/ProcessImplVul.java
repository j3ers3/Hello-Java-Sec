package com.best.hello.controller.RCE;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/RCE/ProcessImpl")
public class ProcessImplVul {

    @ApiOperation(value = "vul：ProcessImpl", notes = "ProcessImpl是更为底层的实现，Runtime和ProcessBuilder执行命令实际上也是调用了ProcessImpl这个类")
    @RequestMapping("/vul")
    public static String vul(String cmd) throws Exception {

        // 对于ProcessImpl类不能直接调用，但可以通过反射来间接调用ProcessImpl来达到执行命令的目的
        Class clazz = Class.forName("java.lang.ProcessImpl");
        Method method = clazz.getDeclaredMethod("start", String[].class, Map.class, String.class, ProcessBuilder.Redirect[].class, boolean.class);
        method.setAccessible(true);
        Process e = (Process) method.invoke(null, new String[]{cmd}, null, null, null, false);

        byte[] bs = new byte[2048];
        int readSize = 0;
        ByteArrayOutputStream infoStream = new ByteArrayOutputStream();
        while ((readSize = e.getInputStream().read(bs)) > 0) {
            infoStream.write(bs, 0, readSize);
        }
        return infoStream.toString();
    }

}
