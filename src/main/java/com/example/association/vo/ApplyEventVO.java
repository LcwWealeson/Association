package com.example.association.vo;

import com.example.association.pojo.ApplyEvent;

public class ApplyEventVO extends ApplyEvent {
    private String timeStart;
    private String timeEnd;
    private String timeApply;
    private String timeVerify;
    private String status;
    private Boolean disable;

    public String getTimeApply() {
        return timeApply;
    }

    public void setTimeApply(String timeApply) {
        this.timeApply = timeApply;
    }

    public String getTimeVerify() {
        return timeVerify;
    }

    public void setTimeVerify(String timeVerify) {
        this.timeVerify = timeVerify;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDisable(Boolean disable) {
        this.disable=disable;
    }

    public Boolean getDisable() {
        return disable;
    }
}
