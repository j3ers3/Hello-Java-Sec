package com.best.hello.mapper;

import com.best.hello.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

// 标识这个类是一个数据访问层的bean，并交给spring容器管理
@Mapper
public interface UserMapper {

    /**
     * 传统的xml配置
     */
    List<User> orderBy(String field, String sort);

    List<User> orderBySafe(String field);


    /**
     * MyBatis3提供了新的基于注解的配置，通过注解不在需要配置繁杂的xml文件
     */
    @Select("select * from users where user like CONCAT('%', #{user}, '%')")
    List<User> queryByUser(@Param("user") String user);

    @Select("select * from users where id = ${id}")
    List<User> queryById1(@Param("id") String id);

    @Select("select * from users where id = ${id}")
    List<User> queryById2(@Param("id") Integer id);


    // 使用#{}会产生报错
    @Select("select * from users order by ${field} desc")
    List<User> orderBy2(@Param("field") String field);

    @Select("select * from users")
    List<User> list();

    // 模糊搜索，直接'%#{q}%' 会报错
    // 安全代码：@Select("select * from users where user like concat('%',#{q},'%')")
    @Select("select * from users where user like '%${q}%'")
    List<User> search(String q);

}
