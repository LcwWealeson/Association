package com.example.association.service;

import com.example.association.common.ServerResponse;
import org.springframework.stereotype.Service;

@Service
public interface ISuperAdminService {
    ServerResponse checkApplyAssociation(int applyId, int adminId, int operation);
    //获取列表
    ServerResponse getCheckAssociationList(String assocName);

    ServerResponse getEventCheckList(String eventName, String assocName, String applicant);

    ServerResponse checkApplyEvent(int eventId, int operation);

    ServerResponse changePassword(int userId,String newPassword);
}
