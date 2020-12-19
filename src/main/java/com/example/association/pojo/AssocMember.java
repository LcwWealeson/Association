package com.example.association.pojo;

public class AssocMember {
    private Integer id;

    private Integer memberId;

    private Integer assocId;

    public AssocMember() {
    }

    public AssocMember(Integer memberId, Integer assocId) {
        this.memberId = memberId;
        this.assocId = assocId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getAssocId() {
        return assocId;
    }

    public void setAssocId(Integer assocId) {
        this.assocId = assocId;
    }
}