package com.example.association.pojo;

import java.util.Date;

public class ApplyJoinAssoc {
    private Integer id;

    private Integer applicantId;

    private Integer assocId;

    private Integer appStatus;

    private Date appTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Integer applicantId) {
        this.applicantId = applicantId;
    }

    public Integer getAssocId() {
        return assocId;
    }

    public void setAssocId(Integer assocId) {
        this.assocId = assocId;
    }

    public Integer getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(Integer appStatus) {
        this.appStatus = appStatus;
    }

    public Date getAppTime() {
        return appTime;
    }

    public void setAppTime(Date appTime) {
        this.appTime = appTime;
    }
}