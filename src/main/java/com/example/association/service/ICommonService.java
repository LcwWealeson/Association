package com.example.association.service;

import com.example.association.common.ServerResponse;
import com.example.association.pojo.User;
import org.springframework.stereotype.Service;

@Service
public interface ICommonService {

    ServerResponse register(User user);

    ServerResponse login(String username, String password);

    ServerResponse changeInfo(User user);

    ServerResponse getAcademy();

    ServerResponse getMajor(int academyId);

    ServerResponse getClasses(int academyId, int majorId);
}
