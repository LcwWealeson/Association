package com.example.association.dao;

import com.example.association.pojo.ApplyAssociation;

public interface ApplyAssociationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplyAssociation record);

    int insertSelective(ApplyAssociation record);

    ApplyAssociation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplyAssociation record);

    int updateByPrimaryKey(ApplyAssociation record);
}