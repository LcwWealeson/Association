package com.example.association.dao;

import com.example.association.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //自定义
    int updateRoleById(Integer id);

    User selectUserByName(String username);

    Integer getIdByUser(String nickname);

    int updatePassword(int userId, String newPassword);

    String getNameById(Integer userId);

    int updateRoleTo1ById(Integer adminId);
}
