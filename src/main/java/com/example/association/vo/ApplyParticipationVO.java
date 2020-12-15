package com.example.association.vo;

import com.example.association.pojo.ApplyParticipation;
import com.example.association.pojo.User;

public class ApplyParticipationVO extends ApplyParticipation {
    private UserVO participant;

    public UserVO getParticipant() {
        return participant;
    }

    public void setParticipant(UserVO participant) {
        this.participant = participant;
    }
}
