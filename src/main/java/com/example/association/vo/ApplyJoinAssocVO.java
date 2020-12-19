package com.example.association.vo;

import com.example.association.pojo.ApplyJoinAssoc;
import com.example.association.pojo.User;

public class ApplyJoinAssocVO extends ApplyJoinAssoc {
    private User applicant;
    private String timeApply;
    private String assocName;
    private String status;

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

    public String getAssocName() {
        return assocName;
    }

    public void setAssocName(String assocName) {
        this.assocName = assocName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
