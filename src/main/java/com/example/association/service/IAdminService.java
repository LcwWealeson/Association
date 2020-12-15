package com.example.association.service;

import com.example.association.common.ServerResponse;
import com.example.association.pojo.ApplyEvent;
import com.example.association.pojo.Association;
import com.example.association.pojo.Notice;

import javax.servlet.http.HttpServletResponse;

public interface IAdminService {
    ServerResponse applyEvent(ApplyEvent applyEventInfor);

    ServerResponse getMember(Integer associationId);

    ServerResponse removeOneMemberById(Integer memberId, Integer associationId);

    ServerResponse publishNotice(Notice notice);

    ServerResponse getAppliedEvents(Integer applicantId);

    ServerResponse modifyAssociation(Association association);

    ServerResponse getApplyJoinAssocList(Integer associationId);

    ServerResponse checkApplyJoinTo1(Integer applyJoinAssocId, Integer applicantId, Integer associationId);

    ServerResponse checkApplyJoinTo2(Integer applyJoinAssocId, Integer applicantId, Integer associationId);

    ServerResponse checkParticipateEventTo1(Integer id, Integer eventId, Integer participantId);

    void getSignInExcelByEventId(Integer eventId, HttpServletResponse response);

    ServerResponse checkParticipateEventTo2(Integer id, Integer eventId, Integer participantId);

    ServerResponse getAllApplyParticipantsByEventId(Integer eventId);

    ServerResponse getParticipantsOfPermitByEventId(Integer eventId);

    ServerResponse getAssociationInfor(Integer associationId);

    ServerResponse getAllAssociations(Integer adminId);
}
