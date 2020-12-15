package com.example.association.pojo;

import io.swagger.annotations.ApiModel;

import java.util.Date;

public class ApplyEvent {

    private Integer eventId;

    private Integer assocId;

    private String eventName;

    private String eventIntro;

    private Integer applicantId;

    private String eventPlace;

    private Date startTime;

    private Date endTime;

    private Date applyTime;

    private Integer eventStatus;

    private Date verifyTime;

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getAssocId() {
        return assocId;
    }

    public void setAssocId(Integer assocId) {
        this.assocId = assocId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName == null ? null : eventName.trim();
    }

    public String getEventIntro() {
        return eventIntro;
    }

    public void setEventIntro(String eventIntro) {
        this.eventIntro = eventIntro == null ? null : eventIntro.trim();
    }

    public Integer getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Integer applicantId) {
        this.applicantId = applicantId;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace == null ? null : eventPlace.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Integer getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Integer eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }
}