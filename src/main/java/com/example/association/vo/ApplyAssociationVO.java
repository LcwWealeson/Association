package com.example.association.vo;

import com.example.association.pojo.ApplyAssociation;

public class ApplyAssociationVO extends ApplyAssociation {
    private String dateTime ;
    private String status;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
