package com.example.association.dao;

import com.example.association.pojo.Association;

public interface AssociationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Association record);

    int insertSelective(Association record);

    Association selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Association record);

    int updateByPrimaryKey(Association record);
}