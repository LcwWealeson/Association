package com.example.association.dao;

import com.example.association.pojo.Notice;

public interface NoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Notice record);

    int insertSelective(Notice record);

    Notice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Notice record);

    int updateByPrimaryKey(Notice record);

    //自定义
    Notice selectByAssocId(Integer associationId);

    int updateNoticeByAssociationId(Notice notice);

}