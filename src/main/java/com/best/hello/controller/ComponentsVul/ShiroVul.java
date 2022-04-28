package com.best.hello.controller.ComponentsVul;

import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@Controller
@RequestMapping("/Shiro")
public class ShiroVul {

    @RequestMapping(value = "/getKey")
    public void getShiroKey(HttpServletResponse resp){
        try{
            byte[] key = new CookieRememberMeManager().getCipherKey();
            resp.getWriter().write("shiro key: \n" + new String(Base64.getEncoder().encode(key)));
        }catch (Exception ignored){}

    }

}
