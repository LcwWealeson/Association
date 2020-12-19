package com.example.association.dao;

import com.example.association.pojo.ApplyParticipation;

import java.util.List;

public interface ApplyParticipationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplyParticipation record);

    int insertSelective(ApplyParticipation record);

    ApplyParticipation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplyParticipation record);

    int updateByPrimaryKey(ApplyParticipation record);

    int updateStatusByIdTo1(Integer id, Integer eventId, Integer participantId);

    int updateStatusByIdTo2(Integer id, Integer eventId, Integer participantId);

    List<ApplyParticipation> getOneEventParticipantsWithPermit(Integer eventId);

    List<ApplyParticipation> getAllApplyParticipantsByEventId(Integer eventId);

    ApplyParticipation selectNotWith2(int userId, int eventId);
}