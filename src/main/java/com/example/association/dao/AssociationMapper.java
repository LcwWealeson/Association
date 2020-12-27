package com.example.association.dao;

import com.example.association.pojo.Association;

import java.util.List;

public interface AssociationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Association record);

    int insertSelective(Association record);

    Association selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Association record);

    int updateByPrimaryKey(Association record);
    
    List<Association> getList(String assocName);

    Integer getIdByName(String assocName);

    int increaseMemberNumByAssocId(Integer associationId, Integer gap);

    int decreaseMemberNumberByAssocId(Integer associationId, Integer gap);

    List<Association> selectAssociationByAdminId(Integer adminId);

    Association selectByAssocName(String assocName);

    Integer selectAssociationCountByadminId(Integer adminId);

    String getNameById(Integer associationId);
}
