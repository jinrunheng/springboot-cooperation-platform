package com.github.springbootcooperationplatform.service;

import com.github.springbootcooperationplatform.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserService {

    @Select("SELECT * FROM myuser where id = #{id}")
    User findUserById(int id);
}
