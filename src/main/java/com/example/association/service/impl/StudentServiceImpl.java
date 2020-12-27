package com.example.association.service.impl;

import com.example.association.service.IStudentService;
import com.example.association.vo.*;
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

    @Autowired
    NoticeMapper noticeMapper;

    @Autowired
    UserMapper userMapper;
    @Override
    public ServerResponse apply(ApplyAssociation applyAssociation) {

        //先判断是否已经存在重名的社团
        if (associationMapper.selectByAssocName(applyAssociation.getAssocName())!=null){
            return ServerResponse.createBySuccessMessage("社团名字与现有社团重复，请重新输入一个社团名字！");
        }else if (applyAssociationMapper.selectByAssocNameNotWith2(applyAssociation.getAssocName())!=null){
            return ServerResponse.createBySuccessMessage("您已提交申请成立该社团，请不要重复提交！请等待审核或查看审核该申请的审核状态！");
        }
        int resultRow = applyAssociationMapper.insert(applyAssociation);
        applyAssociation.setAssocName("0");
        if(resultRow==0){
            return ServerResponse.createByErrorMessage("插入失败，成功提交社团申请");
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

    /**
     * 申请加入社团，先检查是否已发起申请
     * @param userId
     * @param associationId
     * @return
     */
    @Override
    public ServerResponse applyJoinAssociation(int userId, int associationId) {
        if (applyJoinAssocMapper.selectNotWith2(userId,associationId)!=null){
            return ServerResponse.createBySuccessMessage("您已发起加入该社团的申请，请不要重复提交！" +
                    "请等待查看申请状态或查看审核状态！");
        }
        ApplyJoinAssoc applyJoinAssoc = new ApplyJoinAssoc();
        applyJoinAssoc.setApplicantId(userId);
        applyJoinAssoc.setAssocId(associationId);
        applyJoinAssoc.setAppTime(new Date());
        int resultRow = applyJoinAssocMapper.insertSelective(applyJoinAssoc);
        if(resultRow==0){
            return ServerResponse.createByErrorMessage("申请失败");
        }
        return ServerResponse.createBySuccessMessage("申请成功");
    }

    /**
     * 申请参加活动
     * @param userId
     * @param eventId
     * @return
     */
    @Override
    public ServerResponse applyJoinEvent(int userId, int eventId) {
        if (applyParticipationMapper.selectNotWith2(userId,eventId)!=null){
            return ServerResponse.createBySuccessMessage("您已申请报名参加此活动，请勿重复提交申请！" +
                    "请等待审核或者查看审核状态");
        }
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

    /**
     * 查看已加入的社团以及对应社团的通知
     * @param memberId
     * @return
     */
    @Override
    public ServerResponse getHasJoinedAssocAndNotice(Integer memberId) {
        List<AssocMemberVO> assocMemberVOList = assocMember2AssocMemberVO(assocMemberMapper.getByMemberId(memberId));
        return ServerResponse.createBySuccessMessage("获取已加入的社团以及社团的通知",assocMemberVOList);
    }

    /**
     * AssocMember转化成AssocMemberVO，增加社团和通知详细
     * @param assocMemberList
     * @return
     */
    public List<AssocMemberVO> assocMember2AssocMemberVO(List<AssocMember> assocMemberList){
        List<AssocMemberVO> assocMemberVOList = new ArrayList<>();
        for (AssocMember assocMember:assocMemberList){
            AssocMemberVO assocMemberVO = new AssocMemberVO();
            BeanUtils.copyProperties(assocMember,assocMemberVO);
            assocMemberVO.setNotice(noticeMapper.selectByAssocId(assocMember.getAssocId()));
            assocMemberVO.setAssociation(associationMapper.selectByPrimaryKey(assocMember.getAssocId()));
            assocMemberVOList.add(assocMemberVO);
        }
        return assocMemberVOList;
    }

    /**
     * 查看所有申请参加活动的申请状态，包含审核和未审核
     * @param participantId
     * @return
     */
    @Override
    public ServerResponse getApplyJoinEventByUserId(Integer participantId) {

        List<ApplyParticipationVO> applyParticipationVOList = applyParticipation2ApplyParticipationVO(applyParticipationMapper.getAllByParticipantId(participantId));
        int size = applyParticipationVOList.size();
        return size==0?ServerResponse.createBySuccessMessage("您还未报名过任何活动！")
                :ServerResponse.createBySuccessMessage("您报名参加过的活动有"+size+"个",applyParticipationVOList);
    }

    /**
     * ApplyParticipation转化成ApplyParticipationVO，增加UserVO，也就是对应的用户详细信息
     * @param applyParticipationList
     * @return
     */
    public List<ApplyParticipationVO> applyParticipation2ApplyParticipationVO(List<ApplyParticipation> applyParticipationList){
        List<ApplyParticipationVO> applyParticipationVOList = new ArrayList<>();
        for (ApplyParticipation participation :applyParticipationList){
            ApplyParticipationVO participationVO = new ApplyParticipationVO();

            User participant = userMapper.selectByPrimaryKey(participation.getParticipantId());
            UserVO userVO = user2UserVO(participant);
            switch (participation.getEventAppStatus()){
                case 0:
                    participationVO.setStatusParticipate("未审核");
                    break;
                case 1:
                    participationVO.setStatusParticipate("已审核通过");
                    break;
                case 2:
                    participationVO.setStatusParticipate("已审核不通过");
                    break;
            }

            BeanUtils.copyProperties(participation,participationVO);
            participationVO.setParticipant(userVO);
            applyParticipationVOList.add(participationVO);
        }
        return applyParticipationVOList;
    }

    /**
     * 将User转换成UserVO，添加学院专业班级
     * @param user
     * @return
     */
    public UserVO user2UserVO(User user){
        UserVO userVO = new UserVO();
//        String college = academyMapper.selectByPrimaryKey(user.getCollege()).getAcademyName();
//        String major = majorMapper.selectByPrimaryKey(user.getMajor()).getMajorName();
//        Integer classNumber = classesMapper.selectByPrimaryKey(user.getClassFrom()).getClassNumber();
        BeanUtils.copyProperties(user,userVO);
//        userVO.setCollegeStr(college);
//        userVO.setMajorStr(major);
//        userVO.setClassNumber(classNumber);
        userVO.setGenderStr(user.getGender()==0?"男":"女");
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
        return userVO;
    }



    /**
     * 查看所有申请加入社团的申请状态，包含审核未审核
     * @param applicantId
     * @return
     */
    @Override
    public ServerResponse getApplyJoinAssocByUserId(Integer applicantId) {
        List<ApplyJoinAssoc> applyJoinAssoc = applyJoinAssocMapper.getByUserIdAndAssocId(applicantId);
        List<ApplyJoinAssocVO> applyJoinAssocVO = applyJoinAssoc2ApplyJoinAssocVO(applyJoinAssoc);
        for (ApplyJoinAssocVO a:applyJoinAssocVO){
            a.setTimeApply(DateUtil.dateToStr(a.getAppTime()));
            a.setAssocName(associationMapper.getNameById(a.getAssocId()));
            a.setApplicant(userMapper.selectByPrimaryKey(applicantId));
            switch (a.getAppStatus()){
                case 0:
                    a.setStatus("未审核");
                    break;
                case 1:
                    a.setStatus("已审核通过");
                    break;
                case 2:
                    a.setStatus("已审核不通过");
                    break;
            }
        }
        return ServerResponse.createBySuccessMessage("获取申请加入社团的申请状态",applyJoinAssocVO);
    }


    /**
     * 获取申请新社团的状态信息
     * @param applicantId
     * @return
     */
    @Override
    public ServerResponse getApplyNewAssocByApplicantId(Integer applicantId) {
        List<ApplyAssociationVO> applyAssociationVOList = applyAssociation2ApplyAssociationVO(
                applyAssociationMapper.getByApplicantId(applicantId));
        return ServerResponse.createBySuccessMessage("获取申请新社团的状态信息",applyAssociationVOList);
    }

    /**
     * ApplyJoinAssoc 和 ApplyJoinAssocVO的转换，将时间转成String类型
     * @param applyJoinAssocList
     * @return
     */
    public List<ApplyJoinAssocVO> applyJoinAssoc2ApplyJoinAssocVO(List<ApplyJoinAssoc> applyJoinAssocList){
        List<ApplyJoinAssocVO> applyJoinAssocVOList = new ArrayList<>();
        for (ApplyJoinAssoc applyJoinAssoc:applyJoinAssocList){
            String timeApply;
            ApplyJoinAssocVO applyJoinAssocVO = new ApplyJoinAssocVO();
//            User applicant = userMapper.selectByPrimaryKey(applyJoinAssoc.getApplicantId());
//            UserVO userVO = user2UserVO(applicant);

            timeApply=DateUtil.dateToStr(applyJoinAssoc.getAppTime());

            BeanUtils.copyProperties(applyJoinAssoc,applyJoinAssocVO);

//            applyJoinAssocVO.setApplicant(userVO);
            applyJoinAssocVO.setTimeApply(timeApply);
            applyJoinAssocVOList.add(applyJoinAssocVO);

        }
        return applyJoinAssocVOList;
    }

    /**
     * ApplyAssociation转称ApplyAssociationVO，增加字符串确认时间
     * @param applyAssociationList
     * @return
     */
    public List<ApplyAssociationVO> applyAssociation2ApplyAssociationVO(List<ApplyAssociation> applyAssociationList){
        List<ApplyAssociationVO> applyAssociationVOList = new ArrayList<>();
        for (ApplyAssociation applyAssociation:applyAssociationList){
            ApplyAssociationVO applyAssociationVO = new ApplyAssociationVO();
            BeanUtils.copyProperties(applyAssociation,applyAssociationVO);
            applyAssociationVO.setDateTime(DateUtil.dateToStr(applyAssociation.getVerifyTime()));
            switch (applyAssociation.getApplyStatus()){
                case 0:
                    applyAssociationVO.setStatus("未审核");
                    break;
                case 1:
                    applyAssociationVO.setStatus("已审核通过");
                    break;
                case 2:
                    applyAssociationVO.setStatus("已审核不通过");
                    break;
            }
            applyAssociationVOList.add(applyAssociationVO);
        }
        return applyAssociationVOList;
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
