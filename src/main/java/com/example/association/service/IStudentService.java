package com.example.association.service;

import com.example.association.common.ServerResponse;
import com.example.association.pojo.ApplyAssociation;
import org.springframework.stereotype.Service;

@Service
public interface IStudentService {
    ServerResponse apply(ApplyAssociation applyAssociation);

    ServerResponse getAssociationList(String assocName);

    ServerResponse applyJoinAssociation(int userId, int associationId);

    ServerResponse applyJoinEvent(int userId, int eventId);

    ServerResponse getCheckedEvent();

    ServerResponse getApplyNewAssocByApplicantId(Integer applicantId);

    ServerResponse getApplyJoinAssocByUserId(Integer applicantId);

    ServerResponse getHasJoinedAssocAndNotice(Integer memberId);
}
