package com.best.hello.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * SpEL Injection (Spring 表达式注入)
 * @date 2021/06/24
 *
 * 审计的函数
 *      1. SpelExpressionParser
 *      2. getValue
 */

@Api("Spring 表达式注入")
@Slf4j
@RestController
@RequestMapping("/SPEL")
public class SpEL {
    /**
     * 执行命令：http://127.0.0.1:8888/SPEL/vul?ex=T(java.lang.Runtime).getRuntime().exec(%22open%20-a%20Calculator%22)
     */
    @GetMapping("/vul")
    public String spelVul(String ex) {
        // 1. 创建解析器：SpEL使用ExpressionParser接口表示解析器，提供SpelExpressionParser默认实现
        ExpressionParser parser = new SpelExpressionParser();
        // StandardEvaluationContext权限过大，可以执行任意代码
        EvaluationContext evaluationContext = new StandardEvaluationContext();

        // 2. 解析表达式: 使用ExpressionParser的parseExpression来解析相应的表达式为Expression对象
        // 3. 求值：通过 Expression 接口的 getValue 方法根据上下文获得表达式值
        String result = parser.parseExpression(ex).getValue(evaluationContext).toString();
        log.info("[vul] SPEL");
        return result;
    }

    @GetMapping("/safe")
    public String spelSafe(String ex) {
        return null;
    }

}
