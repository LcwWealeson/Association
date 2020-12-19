package com.example.association.vo;

import com.example.association.pojo.AssocMember;
import com.example.association.pojo.Association;
import com.example.association.pojo.Notice;

public class AssocMemberVO extends AssocMember {

    private Association association;
    private Notice notice;

    public AssocMemberVO(Integer memberId, Integer assocId) {
        super(memberId, assocId);
    }

    public AssocMemberVO() {
        super();
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
