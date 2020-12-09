package com.example.association.dao;

import com.example.association.pojo.AssocMember;

public interface AssocMemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AssocMember record);

    int insertSelective(AssocMember record);

    AssocMember selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AssocMember record);

    int updateByPrimaryKey(AssocMember record);
}