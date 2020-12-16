package com.example.association.vo;

import com.example.association.pojo.User;

public class UserVO extends User {
    private String collegeStr;
    private String majorStr;
    private String genderStr;
    private Integer classNumber;
    private String roleStr;


    public Integer getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(Integer classNumber) {
        this.classNumber = classNumber;
    }

    public String getRoleStr() {
        return roleStr;
    }

    public void setRoleStr(String roleStr) {
        this.roleStr = roleStr;
    }

    public String getGenderStr() {
        return genderStr;
    }

    public void setGenderStr(String genderStr) {
        this.genderStr = genderStr;
    }

    public String getCollegeStr() {
        return collegeStr;
    }

    public void setCollegeStr(String collegeStr) {
        this.collegeStr = collegeStr;
    }

    public String getMajorStr() {
        return majorStr;
    }

    public void setMajorStr(String majorStr) {
        this.majorStr = majorStr;
    }


}
