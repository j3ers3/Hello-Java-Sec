package com.best.hello.controller;

import com.best.hello.Dao.User;
import com.best.hello.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.*;


@RestController
@RequestMapping("/SQLI")
public class SQLI {

    @Value("${spring.datasource.url}")
    private String db_url;

    @Value("${spring.datasource.username}")
    private String db_user;

    @Value("${spring.datasource.password}")
    private String db_pass;

    @Autowired
    private UserMapper userMapper;

    /**
     * @vul 采用拼接的SQL语句
     * @poc http://127.0.0.1:8888/SQLI/sql_1?id=1' and updatexml(1,concat(0x7e,(SELECT user()),0x7e),1)--%20+
     * @param id
     */
    @RequestMapping("/sql_1")
    public String sql_1(String id) {

        StringBuilder result = new StringBuilder();

        try {
            /*
             * 注册 JDBC 驱动
             * com.mysql.jdbc.Driver 对应版本 5
             * com.mysql.cj.jdbc.Driver 对应高版本
             */
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 建立连接
            Connection conn = DriverManager.getConnection(db_url, db_user, db_pass);

            // 执行查询
            // System.out.println("[*] 实例化Statement对象...");
            Statement stmt = conn.createStatement();
            String sql = "select * from users where id = '" + id + "'";
            System.out.println("[*] 执行SQL语句：" + sql);
            ResultSet rs = stmt.executeQuery(sql);

            // 获取查询结果
            while (rs.next()) {
                int res_id = rs.getInt("id");
                String res_name = rs.getString("user");
                String res_pass = rs.getString("pass");

                String info = String.format("查询结果%n %s: %s%n", res_name, res_pass);
                result.append(info);
            }

            rs.close();
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            // 输出错误，用于报错注入。。。
            return ex.toString();
        }
        return result.toString();
    }

    /**
     * @safe 采用预编译的方法
     * @poc http://127.0.0.1:8888/SQLI/prepareStatement?id=1'
     * @param id
     */
    @RequestMapping("/prepareStatement")
    public String sql_2(String id) {

        StringBuilder result = new StringBuilder();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 建立连接
            Connection conn = DriverManager.getConnection(db_url, db_user, db_pass);

            Statement stmt = conn.createStatement();

            String sql = "select * from users where id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, id);
            System.out.println("[*] 执行SQL语句：" + st.toString());
            ResultSet rs = st.executeQuery();

            // 获取查询结果
            while (rs.next()) {
                int res_id = rs.getInt("id");
                String res_name = rs.getString("user");
                String res_pass = rs.getString("pass");

                String info = String.format("查询结果%n %s: %s%n", res_name, res_pass);
                result.append(info);
            }

            rs.close();
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
        return result.toString();
    }

    @RequestMapping("/mybatis")
    public User mybatis(String name) {
        return userMapper.findByUserName(name);
    }


}