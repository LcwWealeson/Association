package com.example.association.service.impl;

import com.example.association.service.ISuperAdminService;
import com.example.association.vo.ApplyAssociationVO;
import com.example.association.vo.ApplyEventVO;
import com.example.association.common.ServerResponse;
import com.example.association.dao.ApplyAssociationMapper;
import com.example.association.dao.ApplyEventMapper;
import com.example.association.dao.AssociationMapper;
import com.example.association.dao.UserMapper;
import com.example.association.pojo.ApplyAssociation;
import com.example.association.pojo.ApplyEvent;
import com.example.association.pojo.Association;
import com.example.association.utils.DateUtil;
import com.example.association.utils.MD5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.MD5;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SuperAdminServiceImpl implements ISuperAdminService {
    @Autowired
    ApplyAssociationMapper applyAssociationMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AssociationMapper associationMapper;

    @Autowired
    ApplyEventMapper applyEventMapper;

    @Override
    public ServerResponse checkApplyAssociation(int applyId, int adminId, int operation) {
        ApplyAssociation applyAssociation = applyAssociationMapper.selectByPrimaryKey(applyId);
        applyAssociation.setAdminId(adminId);
        applyAssociation.setVerifyTime(new Date());
        if(operation==1){
            applyAssociation.setApplyStatus(1);
            applyAssociationMapper.updateByPrimaryKeySelective(applyAssociation);
            Association association = new Association();
            association.setAssocName(applyAssociation.getAssocName());
            association.setEstabTime(applyAssociation.getVerifyTime());
            association.setAdminId(applyAssociation.getUserId());
            associationMapper.insert(association);//审批通过后插入社团
            userMapper.updateRoleById(applyAssociation.getUserId());//将申请人升级为社团负责人
            return ServerResponse.createBySuccessMessage("审批通过");
        }if(operation==2){
            applyAssociation.setApplyStatus(2);
            applyAssociationMapper.updateByPrimaryKeySelective(applyAssociation);
            return ServerResponse.createBySuccessMessage("审批不通过");
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse getCheckAssociationList(String assocName) {
        List<ApplyAssociation> applyAssociationList = applyAssociationMapper.selectList(assocName);
        List<ApplyAssociationVO> applyAssociationVOS = new ArrayList<>();
        for(ApplyAssociation applyAssociation:applyAssociationList){
            ApplyAssociationVO applyAssociationVO = new ApplyAssociationVO();
            BeanUtils.copyProperties(applyAssociation,applyAssociationVO);
            String dateTime = DateUtil.dateToStr(applyAssociation.getVerifyTime());
            applyAssociationVO.setDateTime(dateTime);
            applyAssociationVOS.add(applyAssociationVO);
        }
        return ServerResponse.createBySuccessMessage("查询成功",applyAssociationVOS);
    }

    @Override
    public ServerResponse getEventCheckList(String eventName, String assocName, String applicant) {
        ApplyEvent applyEvent = new ApplyEvent();
        if(assocName!=null){
            Integer  assocId = associationMapper.getIdByName(assocName);
            applyEvent.setAssocId(assocId);
        }
        if(applicant!=null){
            Integer applicantId = userMapper.getIdByUser(applicant);
            applyEvent.setApplicantId(applicantId);
        }
        applyEvent.setEventName(eventName);
        List<ApplyEvent> applyEvents = applyEventMapper.getList(applyEvent);
        return ServerResponse.createBySuccessMessage("查询成功",applyEvent2ApplyEventVO(applyEvents));
    }

    @Override
    public ServerResponse checkApplyEvent(int eventId, int operation) {
        ApplyEvent applyEvent = applyEventMapper.selectByPrimaryKey(eventId);
        if(operation==1){
            applyEvent.setEventStatus(1);
            applyEvent.setVerifyTime(new Date());
            applyEventMapper.updateByPrimaryKey(applyEvent);
            return ServerResponse.createBySuccessMessage("审核通过");
        }  if(operation==2){
            applyEvent.setEventStatus(1);
            applyEvent.setVerifyTime(new Date());
            applyEventMapper.updateByPrimaryKey(applyEvent);
            return ServerResponse.createBySuccessMessage("审核失败");
        }
        return ServerResponse.createByErrorMessage("请求失败");
    }

    @Override
    public ServerResponse changePassword(int userId,String newPassword) {
        int resultRow = userMapper.updatePassword(userId, MD5Util.getMD5(newPassword));
        if(resultRow==0){
            return ServerResponse.createByErrorMessage("修改失败");
        }
        return ServerResponse.createBySuccessMessage("修改成功");
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
