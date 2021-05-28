package com.github.springbootcooperationplatform.repository;

import com.github.springbootcooperationplatform.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User findUserByName(String username);

    void save(String username,String encryptedPassword);

    User findUserById(int id);
}
