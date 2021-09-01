package com.best.hello.controller;

import com.best.hello.entity.User;
import com.best.hello.mapper.UserMapper;
import com.best.hello.util.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.*;
import java.util.List;

/**
 * SQL注入，包括常规的拼接和Mybatis排序注入
 *
 * @author nul1
 * @date 2021/07/24
 */

@RestController
@RequestMapping("/SQLI")
public class SQLI {

    // @Value注解 application.properties配置的属性的值
    @Value("${spring.datasource.url}")
    private String db_url;

    @Value("${spring.datasource.username}")
    private String db_user;

    @Value("${spring.datasource.password}")
    private String db_pass;

    // 自动导入依赖的bean
    @Autowired
    private UserMapper userMapper;

    /**
     * @vul JDBC模式下，采用拼接的SQL语句
     * @poc http://127.0.0.1:8888/SQLI/jdbc?id=1' and updatexml(1,concat(0x7e,(SELECT user()),0x7e),1)--%20+
     */
    @RequestMapping("/jdbc")
    public String jdbcVul(String id) {

        StringBuilder result = new StringBuilder();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(db_url, db_user, db_pass);

            Statement stmt = conn.createStatement();
            String sql = "select * from users where id = '" + id + "'";
            System.out.println("[vul] 执行SQL语句：" + sql);
            ResultSet rs = stmt.executeQuery(sql);

            // 获取查询结果
            while (rs.next()) {
                String res_name = rs.getString("user");
                String res_pass = rs.getString("pass");

                String info = String.format("查询结果 %s: %s", res_name, res_pass);
                result.append(info);
            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            // 输出错误，用于报错注入。。。
            return e.toString();
        }
        return result.toString();
    }

    /**
     * @safe 采用预编译的方法，使用?占位，而不是参数拼接
     * @poc http://127.0.0.1:8888/SQLI/jdbc/pre?id=1'
     */
    @RequestMapping("/jdbc/pre")
    public String preVul(String id) {

        StringBuilder result = new StringBuilder();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(db_url, db_user, db_pass);

            String sql = "select * from users where id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, id);
            System.out.println("[vul] 执行SQL语句：" + st);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String res_name = rs.getString("user");
                String res_pass = rs.getString("pass");

                String info = String.format("查询结果%n %s: %s%n", res_name, res_pass);
                result.append(info);
            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            return e.toString();
        }
        return result.toString();
    }

    /**
     * @safe 采用预编译的方法，但没使用?占位，开发者为方便会直接采取拼接的方式构造SQL语句，此时进行预编译也无法阻止SQL注入
     * @poc http://127.0.0.1:8888/SQLI/jdbc/vul/pre?id=2%20and%201=1
     */
    @RequestMapping("/jdbc/vul/pre")
    public String preSafe(String id) {

        StringBuilder result = new StringBuilder();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(db_url, db_user, db_pass);

            String sql = "select * from users where id = " + id;
            PreparedStatement st = conn.prepareStatement(sql);
            System.out.println("[Safe] 执行SQL语句：" + st);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String res_name = rs.getString("user");
                String res_pass = rs.getString("pass");

                String info = String.format("查询结果%n %s: %s%n", res_name, res_pass);
                result.append(info);
            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            return e.toString();
        }
        return result.toString();
    }

    /**
     * @safe 采用过滤的方式
     * @poc http://127.0.0.1:8888/SQLI/jdbc/filter?id=1'
     */
    @RequestMapping("/jdbc/filter")
    public String sqlFilter(String id) {

        if (!Security.checkSql(id)) {
            StringBuilder result = new StringBuilder();

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                Connection conn = DriverManager.getConnection(db_url, db_user, db_pass);

                Statement stmt = conn.createStatement();
                String sql = "select * from users where id = '" + id + "'";

                System.out.println("[Safe] 执行SQL语句：" + sql);
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String res_name = rs.getString("user");
                    String res_pass = rs.getString("pass");
                    String info = String.format("查询结果%n %s: %s%n", res_name, res_pass);
                    result.append(info);
                }

                rs.close();
                conn.close();

            } catch (Exception e) {
                return e.toString();
            }
            return result.toString();
        } else {
            return "检测到非法注入！";
        }
    }


    /**
     * *******************************************************************
     * Mybatis相关
     * *******************************************************************
     *
     * @safe 正常查询
     * @poc http://127.0.0.1:8888/SQLI/mybatis/query?user=admin
     */
    @GetMapping("/mybatis/query")
    public List<User> queryByUser(String user) {
        return userMapper.queryByUser(user);
    }

    /**
     * @vul String id + ${} 参数注入
     * @poc http://127.0.0.1:8888/SQLI/mybatis/vul/id/1 or 1=1
     */
    @GetMapping("/mybatis/vul/id/{id}")
    public List<User> queryById(@PathVariable String id) {
        return userMapper.queryById1(id);
    }

    /**
     * @safe Int id + ${} 不存在注入
     * @poc http://127.0.0.1:8888/SQLI/mybatis/safe/id/1 or 1=1
     */
    @GetMapping("/mybatis/safe/id/{id}")
    public List<User> queryById(@PathVariable Integer id) {
        return userMapper.queryById2(id);
    }


    /**
     * @vul order by 注入，#{} 会将对象转成字符串，形成 order by "user" desc 造成错误，因此很多研发会采用${}来解决，从而造成SQL注入
     * @poc http://127.0.0.1:8888/SQLI/mybatis/vul/order?field=id&sort=desc,1
     */
    @GetMapping("/mybatis/vul/order")
    public List<User> orderBy(String field, String sort) {
        return userMapper.orderBy(field, sort);
    }

    /**
     * @safe order by 做映射来解决注入和报错问题
     * @poc http://127.0.0.1:8888/SQLI/mybatis/safe/order?field=id
     */
    @GetMapping("/mybatis/safe/order")
    public List<User> orderBySafe(String field) {
        return userMapper.orderBySafe(field);
    }

    /**
     * @poc http://127.0.0.1:8888/SQLI/mybatis/vul/order2?field=id
     */
    @GetMapping("/mybatis/vul/order2")
    public List<User> order2(String field) {
        return userMapper.orderBy2(field);
    }


    @GetMapping("/mybatis/list")
    public List<User> xx() {
        return userMapper.list();
    }

    /**
     * Hibernate todo
     */

}