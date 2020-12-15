package com.example.association.Service.impl;

import com.example.association.Service.IStudentService;
import com.example.association.VO.ApplyEventVO;
import com.example.association.VO.AssociationVO;
import com.example.association.common.ServerResponse;
import com.example.association.dao.*;
import com.example.association.pojo.*;
import com.example.association.utils.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    ApplyAssociationMapper applyAssociationMapper;

    @Autowired
    AssociationMapper associationMapper;

    @Autowired
    AssocMemberMapper assocMemberMapper;

    @Autowired
    ApplyJoinAssocMapper applyJoinAssocMapper;

    @Autowired
    ApplyParticipationMapper applyParticipationMapper;

    @Autowired
    ApplyEventMapper applyEventMapper;

    @Override
    public ServerResponse apply(ApplyAssociation applyAssociation) {
        int resultRow = applyAssociationMapper.insert(applyAssociation);
        applyAssociation.setAssocName("0");
        if(resultRow==0){
            return ServerResponse.createByErrorMessage("插入失败");
        }
        return ServerResponse.createBySuccessMessage("插入成功");
    }

    @Override
    public ServerResponse getAssociationList(String assocName) {
        List<Association> associations = associationMapper.getList(assocName);
        List<AssociationVO> associationVOS = new ArrayList<>();
        for(Association association:associations){
            AssociationVO associationVO = new AssociationVO();
            BeanUtils.copyProperties(association,associationVO);
            String time = DateUtil.dateToStr(association.getEstabTime());
            associationVO.setDateTime(time);
            associationVOS.add(associationVO);
        }
        return ServerResponse.createBySuccessMessage("查询成功",associationVOS);
    }

    @Override
    public ServerResponse applyJoinAssociation(int userId, int associationId) {
        ApplyJoinAssoc applyJoinAssoc = new ApplyJoinAssoc();
        applyJoinAssoc.setApplicantId(userId);
        applyJoinAssoc.setAssocId(associationId);
        int resultRow = applyJoinAssocMapper.insertSelective(applyJoinAssoc);
        if(resultRow==0){
            return ServerResponse.createByErrorMessage("申请失败");
        }
        return ServerResponse.createBySuccessMessage("申请成功");
    }

    @Override
    public ServerResponse applyJoinEvent(int userId, int eventId) {
        ApplyEvent applyEvent = applyEventMapper.selectByPrimaryKey(eventId);
        Date date = new Date();
        if(date.compareTo(applyEvent.getStartTime())<0) {
            ApplyParticipation applyParticipation = new ApplyParticipation();
            applyParticipation.setParticipantId(userId);
            applyParticipation.setEventId(eventId);
            int resultRow = applyParticipationMapper.insertSelective(applyParticipation);
            if (resultRow == 0) {
                return ServerResponse.createByErrorMessage("申请失败");
            }
            return ServerResponse.createBySuccessMessage("申请成功");
        }return ServerResponse.createByErrorMessage("报名时间已截止");
    }

    @Override
    public ServerResponse getCheckedEvent() {
        List<ApplyEvent> applyEvents = applyEventMapper.selectByStatusIs1();
        return ServerResponse.createBySuccessMessage("查询成功",applyEvent2ApplyEventVO(applyEvents));
    }


    public List<ApplyEventVO> applyEvent2ApplyEventVO(List<ApplyEvent> applyEventList){
        List<ApplyEventVO> applyEventVOList = new ArrayList<>();
        for (ApplyEvent applyEvent:applyEventList){
            String timeStart;
            String timeEnd;
            String timeApply;
            String timeVerify;
            ApplyEventVO applyEventVO = new ApplyEventVO();

            timeStart = DateUtil.dateToStr(applyEvent.getStartTime());
            timeEnd = DateUtil.dateToStr(applyEvent.getEndTime());
            timeApply = DateUtil.dateToStr(applyEvent.getApplyTime());
            timeVerify = DateUtil.dateToStr(applyEvent.getVerifyTime());

            BeanUtils.copyProperties(applyEvent, applyEventVO);

            applyEventVO.setTimeStart(timeStart);
            applyEventVO.setTimeEnd(timeEnd);
            applyEventVO.setTimeApply(timeApply);
            applyEventVO.setTimeVerify(timeVerify);
            applyEventVOList.add(applyEventVO);

        }
        return applyEventVOList;
    }
}