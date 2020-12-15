package com.example.association.service.impl;

import com.example.association.common.ServerResponse;
import com.example.association.dao.*;
import com.example.association.pojo.*;
import com.example.association.service.IAdminService;
import com.example.association.utils.DateUtil;
import com.example.association.utils.ExcelUtil;
import com.example.association.vo.ApplyEventVO;
import com.example.association.vo.ApplyJoinAssocVO;
import com.example.association.vo.ApplyParticipationVO;
import com.example.association.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdminService implements IAdminService {

    @Autowired
    ApplyEventMapper applyEventMapper;

    @Autowired
    AssocMemberMapper assocMemberMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    NoticeMapper noticeMapper;

    @Autowired
    AssociationMapper associationMapper;

    @Autowired
    ApplyJoinAssocMapper applyJoinAssocMapper;

    @Autowired
    ApplyParticipationMapper applyParticipationMapper;

    @Autowired
    AcademyMapper academyMapper;

    @Autowired
    MajorMapper majorMapper;

    @Autowired
    ClassesMapper classesMapper;


    @Override
    public ServerResponse getAllAssociations(Integer adminId) {
        return ServerResponse.createBySuccessMessage("成功获取当前社团负责人的所有社团信息列表",
                associationMapper.selectAssociationByAdminId(adminId));
    }

    /**
     * 获取某个社团详细信息
     * @param associationId
     * @return
     */
    @Override
    public ServerResponse getAssociationInfor(Integer associationId) {
        return ServerResponse.createBySuccessMessage("成功获取当前社团的详细信息",associationMapper.selectByPrimaryKey(associationId));
    }

    /**
     * 申请一个活动，提交申请表，此时申请表的状态为“未审核”
     * @param applyEventInfor
     * @return
     */
    @Override
    public ServerResponse applyEvent(ApplyEvent applyEventInfor) {
        applyEventInfor.setApplyTime(new Date());
        int row = applyEventMapper.insertSelective(applyEventInfor);
        return ServerResponse.createBySuccessMessage("申请活动"+row+"个，待审核。");
    }

    /**
     * 获取社团所有成员的信息
     * @param associationId
     * @return
     */
    @Override
    public ServerResponse getMember(Integer associationId) {
        List<User> memberList = new ArrayList<>();
        List<Integer> assocMembersId = new ArrayList<>();
        assocMembersId = assocMemberMapper.getAllAssocMembersId(associationId);
        for(Integer memberId:assocMembersId){
            memberList.add(userMapper.selectByPrimaryKey(memberId));
        }
        return ServerResponse.createBySuccessMessage("当前社团的所有成员信息：",memberList);
    }

    /**
     * 移除某位社团成员，社团总人数-1
     * @param memberId
     * @param associationId
     * @return
     */
    @Override
    public ServerResponse removeOneMemberById(Integer memberId,Integer associationId) {
        String username = userMapper.getNameById(memberId);
        int row = assocMemberMapper.removeMemberById(memberId,associationId);
        associationMapper.decreaseMemberNumberByAssocId(associationId, 1);
        return ServerResponse.createBySuccessMessage("移除"+row+"位社团成员，用户名为："+username+",社团总人数-1");
    }

    /**
     * 社团负责人发布通知
     * 先检查该社团是否已发布过通知：
     *      否，增加新通知；是，更新通知
     * @param notice
     * @return
     */
    @Override
    public ServerResponse publishNotice(Notice notice) {
        notice.setPublishTime(new Date());
        if (noticeMapper.selectByAssocId(notice.getAssocId())==null){
            return ServerResponse.createBySuccessMessage("成功发布"+noticeMapper.insertSelective(notice)
                    +"条通知！");
        }
        return ServerResponse.createBySuccessMessage("成功发布"+noticeMapper.updateNoticeByAssociationId(notice)
                +"条通知！");
    }

    /**
     * 查看所有已申请的活动（包含未审核、已通过审核、未通过审核)
     * @param applicantId
     * @return
     */
    @Override
    public ServerResponse getAppliedEvents(Integer applicantId) {
        List<ApplyEventVO> applyEventVOList =
                applyEvent2ApplyEventVO(applyEventMapper.selectByApplicantId(applicantId));
        return ServerResponse.createBySuccessMessage("当前申请的活动列表：",applyEventVOList);
    }

    /**
     * 修改某个社团信息
     * @param association
     * @return
     */
    @Override
    public ServerResponse modifyAssociation(Association association) {
        return ServerResponse.createBySuccessMessage("成功修改"+
                associationMapper.updateByPrimaryKeySelective(association)+"条社团信息。");
    }


    /**
     *  获取申请加入该社团的信息列表
     * @param associationId
     * @return
     */
    @Override
    public ServerResponse getApplyJoinAssocList(Integer associationId) {
        List<ApplyJoinAssocVO> applyJoinAssocVOList =applyJoinAssoc2ApplyJoinAssocVO(
                applyJoinAssocMapper.getApplyJoinAssocList(associationId));
        return ServerResponse.createBySuccessMessage("获取当前社团所有申请加入的信息列表"
                ,applyJoinAssocVOList);
    }

    /**
     * 允许某人加入社团，修改申请状态为1、增加对应社团成员、增加社团总人数
     * @param applyJoinAssocId
     * @param applicantId
     * @param associationId
     * @return
     */
    @Override
    public ServerResponse checkApplyJoinTo1(Integer applyJoinAssocId, Integer applicantId, Integer associationId) {
        int rowCheck=0;
        int rowInserAssocMember=0;
        int rowUpdateAssocMember=0;
        if (applyJoinAssocId!=null&&applicantId!=null&&associationId!=null){
            if (assocMemberMapper.getByMemberId(applicantId,associationId)==null){
                rowInserAssocMember = assocMemberMapper.insertSelective(new AssocMember(applicantId,associationId));
            }else {
                return ServerResponse.createBySuccessMessage("此人已在该社团中！请勿重复申请！");
            }
            rowCheck = applyJoinAssocMapper.checkApplyJoinTo1(applyJoinAssocId,applicantId,associationId);
            rowUpdateAssocMember = associationMapper.increaseMemberNumByAssocId(associationId,1);
        }
        return ServerResponse.createBySuccessMessage("成功允许"+rowCheck+"名人员加入社团，用户ID是"+applicantId+"。");
    }

    /**
     * 拒绝某人加入社团，修改申请状态为2
     * @param applyJoinAssocId
     * @param applicantId
     * @param associationId
     * @return
     */
    @Override
    public ServerResponse checkApplyJoinTo2(Integer applyJoinAssocId, Integer applicantId, Integer associationId) {
        int rowCheck=0;
        if (applyJoinAssocId!=null&&applicantId!=null&&associationId!=null){
            if (assocMemberMapper.getByMemberId(applicantId,associationId)==null){
                assocMemberMapper.insertSelective(new AssocMember(applicantId,associationId));
            }else {
                return ServerResponse.createBySuccessMessage("此人已在该社团中！请勿重复申请加入该社团！");
            }
            rowCheck = applyJoinAssocMapper.checkApplyJoinTo2(applyJoinAssocId,applicantId,associationId);
        }
        return ServerResponse.createBySuccessMessage("拒绝此人加入社团，用户ID是"+applicantId+"。");
    }

    /**
     * 允许某人参加某项活动，将申请状态改为1
     * @param id
     * @param eventId
     * @param participantId
     * @return
     */
    @Override
    public ServerResponse checkParticipateEventTo1(Integer id, Integer eventId, Integer participantId) {
        ApplyEvent applyEvent= applyEventMapper.selectByPrimaryKey(eventId);
        Date currentDate = new Date();
        int rowCheck=0;
        if (currentDate.compareTo(applyEvent.getStartTime())<=0){
            rowCheck = applyParticipationMapper.updateStatusByIdTo1(id,eventId,participantId);
        }else {
            applyParticipationMapper.updateStatusByIdTo2(id,eventId,participantId);
            return ServerResponse.createByErrorMessage("已过活动开始举办时间，无法允许此人参加活动！");
        }
        return ServerResponse.createBySuccessMessage("已允许ID为"+participantId+"的用户参加此活动");
    }

    /**
     * 拒绝某人参加某项活动，将申请状态改为2
     * @param id
     * @param eventId
     * @param participantId
     * @return
     */
    @Override
    public ServerResponse checkParticipateEventTo2(Integer id, Integer eventId, Integer participantId) {
        int rowCheck=applyParticipationMapper.updateStatusByIdTo2(id,eventId,participantId);;
        return ServerResponse.createBySuccessMessage("已拒绝"+rowCheck+"人参加此活动,此人ID为"+participantId);
    }

    /**
     * 获取当前活动的人员名单（所有人员都是已申请参加活动并且已通过负责人审核）
     * @param eventId
     * @param response
     * @return
     */
    @Override
    public void getSignInExcelByEventId(Integer eventId, HttpServletResponse response) {
        String rootPath = System.getProperty("user.dir");//获取当前项目根目录
        ApplyEvent applyEvent = applyEventMapper.selectByPrimaryKey(eventId);
        String eventName = applyEvent.getEventName();
        String eventPlace = applyEvent.getEventPlace();
        List<ApplyParticipation> participationList = applyParticipationMapper.getOneEventParticipantsWithPermit(eventId);
        List<ApplyParticipationVO> participationVOList = applyParticipation2ApplyParticipationVO(participationList);
        String[] titles = {"活动名称","活动地点","用户名","姓名","性别","学院","专业","班级","签到","签退"};
        String path = ExcelUtil.exportSignInList(titles,eventName,eventPlace,participationVOList,rootPath,response);
    }

    /**
     * 获取某活动的所有报名参加人员的名单，包括未审核和已审核
     * @param eventId
     * @return
     */
    @Override
    public ServerResponse getAllApplyParticipantsByEventId(Integer eventId) {
        List<ApplyParticipation> participationList = applyParticipationMapper.getAllApplyParticipantsByEventId(eventId);
        List<ApplyParticipationVO> participationVOList = applyParticipation2ApplyParticipationVO(participationList);
        return ServerResponse.createBySuccessMessage("获取某活动所有报名参加人员，包括未审核与拒绝参加的人",participationVOList);
    }

    /**
     * 获取某活动的报名参加人员名单，只包含成功报名的，以便于打印签到表
     * @param eventId
     * @return
     */
    @Override
    public ServerResponse getParticipantsOfPermitByEventId(Integer eventId) {
        List<ApplyParticipationVO> participationVOList =applyParticipation2ApplyParticipationVO(
                applyParticipationMapper.getOneEventParticipantsWithPermit(eventId));
        return ServerResponse.createBySuccessMessage("获取成功报名参加某活动的人员名单",participationVOList);
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
            userVO.setClassFrom(classNumber);
            userVO.setGenderStr(gender);

            userVOList.add(userVO);
        }
        return userVOList;
    }

    /**
     * 将User转换成UserVO，添加
     * @param user
     * @return
     */
    public UserVO user2UserVO(User user){
            UserVO userVO = new UserVO();
            String college = academyMapper.selectByPrimaryKey(user.getCollege()).getAcademyName();
            String major = majorMapper.selectByPrimaryKey(user.getMajor()).getMajorName();
            Integer classNumber = classesMapper.selectByPrimaryKey(user.getClassFrom()).getClassNumber();
            BeanUtils.copyProperties(user,userVO);
            userVO.setCollegeStr(college);
            userVO.setMajorStr(major);
            userVO.setClassFrom(classNumber);
            userVO.setGenderStr(user.getGender()==0?"男":"女");
        return userVO;
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

            BeanUtils.copyProperties(participation,participationVO);

            participationVO.setParticipant(userVO);
            applyParticipationVOList.add(participationVO);
        }
        return applyParticipationVOList;
    }



    /**
     * ApplyJoinAssoc 和 ApplyJoinAssocVO的转换，增加 User，将时间转成String类型
     * @param applyJoinAssocList
     * @return
     */
    public List<ApplyJoinAssocVO> applyJoinAssoc2ApplyJoinAssocVO(List<ApplyJoinAssoc> applyJoinAssocList){
        List<ApplyJoinAssocVO> applyJoinAssocVOList = new ArrayList<>();
        for (ApplyJoinAssoc applyJoinAssoc:applyJoinAssocList){
            String timeApply;
            ApplyJoinAssocVO applyJoinAssocVO = new ApplyJoinAssocVO();
            User applicant = userMapper.selectByPrimaryKey(applyJoinAssoc.getApplicantId());
            timeApply=DateUtil.dateToStr(applyJoinAssoc.getAppTime());

            BeanUtils.copyProperties(applyJoinAssoc,applyJoinAssocVO);

            applyJoinAssocVO.setApplicant(applicant);
            applyJoinAssocVO.setTimeApply(timeApply);
            applyJoinAssocVOList.add(applyJoinAssocVO);

        }
        return applyJoinAssocVOList;
    }


    /**
     * ApplyEvent 和 ApplyEvent 类型转化，所有时间类型转化为String
     * @param applyEventList
     * @return
     */
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