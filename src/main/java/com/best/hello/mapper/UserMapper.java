package com.best.hello.mapper;

import com.best.hello.Dao.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from users where user= #{user}")
    User findByUserName(@Param("user") String user);  //

}
