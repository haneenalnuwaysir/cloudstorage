package org.springframework.boot.cloudstorage.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.boot.cloudstorage.model.User;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS WHERE username = #{userName}")
    User getUserByName(String userName);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{userName}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

//    @Select("SELECT * FROM USERS WHERE userid = #{userId}")
//    User getUserById(Integer userId);

}
