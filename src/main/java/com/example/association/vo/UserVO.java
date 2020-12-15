package com.example.association.vo;

import com.example.association.pojo.User;

public class UserVO extends User {
    private String collegeStr;
    private String majorStr;
    private String genderStr;

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
