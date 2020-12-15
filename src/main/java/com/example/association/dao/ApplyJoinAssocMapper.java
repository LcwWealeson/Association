package com.example.association.dao;

import com.example.association.pojo.ApplyJoinAssoc;

import java.util.List;

public interface ApplyJoinAssocMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplyJoinAssoc record);

    int insertSelective(ApplyJoinAssoc record);

    ApplyJoinAssoc selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplyJoinAssoc record);

    int updateByPrimaryKey(ApplyJoinAssoc record);

    List<ApplyJoinAssoc> getApplyJoinAssocList(Integer associationId);

    int checkApplyJoinTo1(Integer applyJoinAssocId, Integer applicantId, Integer associationId);

    int checkApplyJoinTo2(Integer applyJoinAssocId, Integer applicantId, Integer associationId);
}