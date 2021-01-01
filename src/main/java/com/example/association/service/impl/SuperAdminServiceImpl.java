package com.example.association.service.impl;

import com.example.association.dao.*;
import com.example.association.pojo.*;
import com.example.association.service.ISuperAdminService;
import com.example.association.vo.ApplyAssociationVO;
import com.example.association.vo.ApplyEventVO;
import com.example.association.common.ServerResponse;
import com.example.association.utils.DateUtil;
import com.example.association.utils.MD5Util;
import com.example.association.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    AssocMemberMapper assocMemberMapper;

    @Autowired
    AcademyMapper academyMapper;

    @Autowired
    MajorMapper majorMapper;

    @Autowired
    ClassesMapper classesMapper;

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
            association.setMemberNum(1);
            associationMapper.insert(association);//审批通过后插入社团
            userMapper.updateRoleById(applyAssociation.getUserId());//将申请人升级为社团负责人

            //获取新插入的社团实体，包含id
            Association newAssoc= associationMapper.selectByAssocName(association.getAssocName());
            //向社团成员关系表中添加本社团负责人与社团的关联，他也算一个社团成员
            assocMemberMapper.insertSelective(new AssocMember(newAssoc.getAdminId(),newAssoc.getId()));
            return ServerResponse.createBySuccessMessage("审批通过，成立新社团"+newAssoc.getAssocName()+"成功");
        }if(operation==2){
            applyAssociation.setApplyStatus(2);
            applyAssociationMapper.updateByPrimaryKeySelective(applyAssociation);
            return ServerResponse.createBySuccessMessage("审批不通过，成立新社团失败");
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

    /**
     * 审核活动的申请
     * @param eventId
     * @param operation
     * @return
     */
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

    /**
     * 获取所有用户的信息，除了密码
     * @return
     */
    @Override
    public ServerResponse getUsersListWithoutPwd() {
        return ServerResponse.createBySuccessMessage("获取所有用户信息列表，包括超级管理员自己",
                userList2UserVOList(userMapper.getUsersListWithoutPwd()));
    }

    /**
     * 修改某个用户的密码，一般修改为一个默认的重置密码
     * @param userId
     * @param newPassword
     * @return
     */
    @Override
    public ServerResponse changePassword(int userId,String newPassword) {
        int resultRow = userMapper.updatePassword(userId, MD5Util.getMD5(newPassword));
        if(resultRow==0){
            return ServerResponse.createByErrorMessage("修改失败");
        }
        return ServerResponse.createBySuccessMessage("修改成功");
    }

    /**
     * User转称UserVO
     * @param userList
     * @return
     */
    public List<UserVO> userList2UserVOList(List<User> userList){
        List<UserVO> userVOList = new ArrayList<>();
        for (User user:userList){
            UserVO userVO = new UserVO();
            String college = academyMapper.selectByPrimaryKey(user.getCollege()).getAcademyName();
            String major = majorMapper.selectByPrimaryKey(user.getMajor()).getMajorName();
            Integer classNumber = classesMapper.selectByPrimaryKey(user.getClassFrom()).getClassNumber();
            String gender = user.getGender()==0?"男":"女";
            BeanUtils.copyProperties(user,userVO);
            userVO.setCollegeStr(college);
            userVO.setMajorStr(major);
            userVO.setClassNumber(classNumber);
            userVO.setGenderStr(gender);
            switch (user.getRole()){
                case 1:
                    userVO.setRoleStr("学生");
                    break;
                case 2:
                    userVO.setRoleStr("社团负责人");
                    break;
                case 3:
                    userVO.setRoleStr("超级管理员");
                    break;
            }

            userVOList.add(userVO);
        }
        return userVOList;
    }

    /**
     * 删除某个社团
     * @param associationId
     * @return
     */
    @Override
    public ServerResponse removeAssociation(Integer associationId) {
        Association association = associationMapper.selectByPrimaryKey(associationId);
        Integer adminId = association.getAdminId();
        //判断当前社团的负责人是否也是其他社团的负责人，若名下的社团总数只有一个，则还原他的role为1
        //若社团总数不止一个，总不还原他的role，role依然是2
        if (associationMapper.selectAssociationCountByadminId(adminId)==1){
            //将社团负责人的role还原为学生role——1
            int rowUpdate = userMapper.updateRoleTo1ById(adminId);
        }
        //清空与该社团相关的所有社团成员关系
        int rowclear = assocMemberMapper.clearMemberOfAssocByAssocId(associationId);
        int rowDelete = associationMapper.deleteByPrimaryKey(associationId);
        return ServerResponse.createBySuccessMessage("成功删除"+association.getAssocName());
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
