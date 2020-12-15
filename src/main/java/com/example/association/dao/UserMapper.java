package com.example.association.dao;

import com.example.association.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    int updateRoleById(Integer id);

    User selectUserByName(String username);

    Integer getIdByUser(String nickname);

    int updatePassword(int userId, String newPassword);

    //自定义
    String getNameById(Integer userId);
}
