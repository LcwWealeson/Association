package com.example.association.dao;

import com.example.association.pojo.ApplyEvent;

public interface ApplyEventMapper {
    int deleteByPrimaryKey(Integer eventId);

    int insert(ApplyEvent record);

    int insertSelective(ApplyEvent record);

    ApplyEvent selectByPrimaryKey(Integer eventId);

    int updateByPrimaryKeySelective(ApplyEvent record);

    int updateByPrimaryKey(ApplyEvent record);
}