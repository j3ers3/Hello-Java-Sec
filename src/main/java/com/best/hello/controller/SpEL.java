package com.best.hello.controller;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * SpEL Injection (Spring 表达式注入)
 * @date 2021/06/24
 */

@RestController
public class SpEL {
    /**
     * 算数运算：http://127.0.0.1:8888/spel?ex=100*2
     * 对象实例化: http://127.0.0.1:8888/spel?ex=new%20java.util.Date().getTime()
     * T(Type): 使用“T(Type)”来表示java.lang.Class实例，同样，只有java.lang 下的类才可以省略包名
     * 执行命令：http://127.0.0.1:8888/spel?ex=T(java.lang.Runtime).getRuntime().exec(%22open%20-a%20Calculator%22)
     */
    @GetMapping("/spel")
    public String rce(String ex) {
        // 1. 创建解析器：SpEL使用ExpressionParser接口表示解析器，提供SpelExpressionParser默认实现
        ExpressionParser parser = new SpelExpressionParser();

        // 2. 解析表达式: 使用ExpressionParser的parseExpression来解析相应的表达式为Expression对象
        // 3. 求值：通过 Expression 接口的 getValue 方法根据上下文获得表达式值
        String result = parser.parseExpression(ex).getValue().toString();
        System.out.println(result);
        return result;
    }

}
