package com.zhen.community.dao;

import com.zhen.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    User selectById(int id);
    User selectByName(String name);
    User selectByEmail(String email);

    int insertUser(User user);
    int updateStatus(int id, int status);
    int updateHeader(int id, String herderUrl);
    int updatePassword(int id, String password);

}
