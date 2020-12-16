package com.example.association.dao;

import com.example.association.pojo.AssocMember;

import java.util.List;

public interface AssocMemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AssocMember record);

    int insertSelective(AssocMember record);

    AssocMember selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AssocMember record);

    int updateByPrimaryKey(AssocMember record);

    List<Integer> getAllAssocMembersId(Integer associationId);

    int removeMemberById(Integer memberId, Integer associationId);

    Integer getByMemberId(Integer applicantId, Integer associationId);

    int clearMemberOfAssocByAssocId(Integer associationId);
}