package com.example.association.pojo;

import java.util.Date;

public class Association {
    private Integer id;

    private String assocName;

    private String assocIntro;

    private Integer memberNum;

    private String institution;

    private String icon;

    private Date estabTime;

    private Integer adminId;

    public Association() {
    }

    public Association(Integer id, String assocName, String assocIntro, Integer memberNum, String institution, String icon, Date estabTime, Integer adminId) {
        this.id = id;
        this.assocName = assocName;
        this.assocIntro = assocIntro;
        this.memberNum = memberNum;
        this.institution = institution;
        this.icon = icon;
        this.estabTime = estabTime;
        this.adminId = adminId;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAssocName() {
        return assocName;
    }

    public void setAssocName(String assocName) {
        this.assocName = assocName == null ? null : assocName.trim();
    }

    public String getAssocIntro() {
        return assocIntro;
    }

    public void setAssocIntro(String assocIntro) {
        this.assocIntro = assocIntro == null ? null : assocIntro.trim();
    }

    public Integer getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution == null ? null : institution.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public Date getEstabTime() {
        return estabTime;
    }

    public void setEstabTime(Date estabTime) {
        this.estabTime = estabTime;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
}
