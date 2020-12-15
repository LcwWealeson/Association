package com.example.association.dao;

import com.example.association.pojo.Academy;

import java.util.List;

public interface AcademyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Academy record);

    int insertSelective(Academy record);

    Academy selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Academy record);

    int updateByPrimaryKey(Academy record);

    List<Academy> getAll();
}