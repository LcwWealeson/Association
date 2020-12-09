package com.example.association.dao;

import com.example.association.pojo.Propaganda;

public interface PropagandaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Propaganda record);

    int insertSelective(Propaganda record);

    Propaganda selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Propaganda record);

    int updateByPrimaryKey(Propaganda record);
}