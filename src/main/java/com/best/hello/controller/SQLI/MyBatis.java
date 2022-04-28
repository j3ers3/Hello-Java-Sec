package com.best.hello.controller.SQLI;

import com.best.hello.entity.User;
import com.best.hello.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("SQL注入 - MyBatis")
@Slf4j
@RestController
@RequestMapping("/SQLI/MyBatis")
public class MyBatis {

    @Autowired
    private UserMapper userMapper;


    @ApiOperation(value = "vul：Mybatis使用${}查询1")
    @GetMapping("/vul/id/{id}")
    public List<User> queryById(@PathVariable String id) {
        return userMapper.queryById1(id);
    }

    @ApiOperation(value = "vul：Mybatis使用${}查询2")
    @GetMapping("/vul/search")
    public List<User> search(@RequestParam("q") String q) {
        log.info("[vul] mybaits: " + q);
        return userMapper.search(q);
    }


    /**
     * @poc http://127.0.0.1:8888/SQLI/MyBatis/vul/order?field=id&sort=desc,1
     */
    @ApiOperation(value = "vul：Mybatis order by 注入（xml方式）", notes = "#{} 会将对象转成字符串，形成 order by \"user\" desc 造成错误(无法正常排序)，因此很多研发会采用${}来解决，从而造成SQL注入")
    @GetMapping("/vul/order")
    public List<User> orderBy(String field, String sort) {
        log.info("[vul] mybaits: order by " + field + " " + sort);
        return userMapper.orderBy(field, sort);
    }

    /**
     * @poc http://127.0.0.1:8888/SQLI/MyBatis/vul/order2?field=id
     */
    @ApiOperation(value = "vul：Mybatis order by 注入（注解方式）")
    @GetMapping("/vul/order2")
    public List<User> order2(String field) {
        log.info("[vul] mybaits: " + field);
        return userMapper.orderBy2(field);
    }


    // -----------------------------------安全分割线-----------------------------------

    @ApiOperation(value = "safe：Mybatis使用 #{}")
    @GetMapping("/safe/query")
    public List<User> queryByUser(String user) {
        return userMapper.queryByUser(user);
    }

    @ApiOperation(value = "safe：Mybatis 查询所有用户")
    @GetMapping("/safe/list")
    public List<User> list() {
        return userMapper.list();
    }

    /**
     * @poc http://127.0.0.1:8888/SQLI/MyBatis/safe/id/1 or 1=1
     */
    @ApiOperation(value = "safe：Mybatis使用${}，但写死Int类型，无法注入")
    @GetMapping("/safe/id/{id}")
    public List<User> queryById(@PathVariable Integer id) {
        log.info("[safe] mybaits: " + id);
        return userMapper.queryById2(id);
    }

    /**
     * @poc http://127.0.0.1:8888/SQLI/MyBatis/safe/order?field=id
     */
    @ApiOperation(value = "safe：Mybatis order by 做映射")
    @GetMapping("/safe/order")
    public List<User> orderBySafe(String field) {
        return userMapper.orderBySafe(field);
    }


}
