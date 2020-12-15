package com.example.association.dao;

import com.example.association.pojo.Classes;

import java.util.List;

public interface ClassesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Classes record);

    int insertSelective(Classes record);

    Classes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Classes record);

    int updateByPrimaryKey(Classes record);

    List<Classes> getAll(int academyId, int majorId);
}