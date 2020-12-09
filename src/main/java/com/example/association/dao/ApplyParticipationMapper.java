package com.example.association.dao;

import com.example.association.pojo.ApplyParticipation;

public interface ApplyParticipationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplyParticipation record);

    int insertSelective(ApplyParticipation record);

    ApplyParticipation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplyParticipation record);

    int updateByPrimaryKey(ApplyParticipation record);
}