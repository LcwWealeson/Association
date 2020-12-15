package com.example.association.dao;

import com.example.association.pojo.ApplyEvent;

import java.util.List;

public interface ApplyEventMapper {
    int deleteByPrimaryKey(Integer eventId);

    int insert(ApplyEvent record);

    int insertSelective(ApplyEvent record);

    ApplyEvent selectByPrimaryKey(Integer eventId);

    int updateByPrimaryKeySelective(ApplyEvent record);

    int updateByPrimaryKey(ApplyEvent record);

    List<ApplyEvent> getList(ApplyEvent record);

    List<ApplyEvent> selectByStatusIs1();
}