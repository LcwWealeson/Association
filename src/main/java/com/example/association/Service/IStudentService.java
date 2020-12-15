package com.example.association.Service;

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
}
