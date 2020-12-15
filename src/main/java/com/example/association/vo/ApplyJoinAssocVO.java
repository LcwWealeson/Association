package com.example.association.vo;

import com.example.association.pojo.ApplyJoinAssoc;
import com.example.association.pojo.User;

public class ApplyJoinAssocVO extends ApplyJoinAssoc {
    private User applicant;
    private String timeApply;

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public String getTimeApply() {
        return timeApply;
    }

    public void setTimeApply(String timeApply) {
        this.timeApply = timeApply;
    }
}
