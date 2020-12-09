package com.example.association.dao;

import com.example.association.pojo.ApplyJoinAssoc;

public interface ApplyJoinAssocMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplyJoinAssoc record);

    int insertSelective(ApplyJoinAssoc record);

    ApplyJoinAssoc selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplyJoinAssoc record);

    int updateByPrimaryKey(ApplyJoinAssoc record);
}