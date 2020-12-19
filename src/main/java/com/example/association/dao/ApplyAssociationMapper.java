package com.example.association.dao;

import com.example.association.pojo.ApplyAssociation;

import java.util.List;

public interface ApplyAssociationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplyAssociation record);

    int insertSelective(ApplyAssociation record);

    ApplyAssociation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplyAssociation record);

    int updateByPrimaryKey(ApplyAssociation record);

    List<ApplyAssociation> selectList(String assocName);

    List<ApplyAssociation> getByApplicantId(Integer applicantId);

    ApplyAssociation selectByAssocNameNotWith2(String assocName);
}