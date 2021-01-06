package com.example.association.service.impl;

import com.example.association.common.ServerResponse;
import com.example.association.dao.*;
import com.example.association.pojo.*;
import com.example.association.service.IAdminService;
import com.example.association.utils.DateUtil;
import com.example.association.utils.ExcelUtil;
import com.example.association.vo.*;
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
        AssociationVO associationVO = association2AssociationVO(associationMapper.selectByPrimaryKey(associationId));
        return ServerResponse.createBySuccessMessage("成功获取当前社团的详细信息",associationVO);
    }

    /**
     * Association转化成AssociationVO，增加字符串时间
     * @param association
     * @return
     */
    public AssociationVO association2AssociationVO(Association association){
        AssociationVO associationVO = new AssociationVO();
        BeanUtils.copyProperties(association,associationVO);
        String time = DateUtil.dateToStr(association.getEstabTime());
        associationVO.setDateTime(time);
        return associationVO;
    }


    /**
     * 申请一个活动，提交申请表，此时申请表的状态为“未审核”，检查是否存在重复名字，若重复检查地点，
     * 若地点重复检查时间是否存在重叠部分
     * @param applyEventInfor
     * @return
     */
    @Override
    public ServerResponse applyEvent(ApplyEvent applyEventInfor) {
        //对输入的开始和结束时间进行归一化的处理，使得Date类型的时间的mills为0
        String dateStartStr = DateUtil.dateToStr(applyEventInfor.getStartTime());
        String dateEndStr = DateUtil.dateToStr(applyEventInfor.getEndTime());
        Date dateStart = DateUtil.strToDate(dateStartStr);
        Date dateEnd = DateUtil.strToDate(dateEndStr);

        //如果申请活动的开始时间是在今天时间之前，那就不可以申请
        if (new Date().compareTo(dateStart)>0){
            return ServerResponse.createBySuccessMessage("申请活动的开始时间比现在早，不可以通过申请！");
        }

        //获取已经申请并且没有被拒绝通过的活动
        List<ApplyEvent> applyEventList = applyEventMapper.selectByEventName(applyEventInfor.getEventName());
        for (ApplyEvent applyEvent:applyEventList){
            if (applyEventMapper.selectByEventName(applyEventInfor.getEventName())!=null){
                //若活动名称相同并且地点还相同，比较时间的先后或包含关系
                if (applyEvent.getEventPlace().equals(applyEventInfor.getEventPlace())){
                    //原有活动的时间范围包含了申请的活动时间范围
                    if (applyEvent.getStartTime().compareTo(dateStart)<0&&applyEvent.getEndTime().compareTo(dateEnd)>0){
                        return ServerResponse.createBySuccessMessage("同一个活动时间出现重叠部分！请重新设定时间！" +
                                "原有活动的时间范围包含了申请的活动时间范围");
                    }
                    //申请的活动时间范围包含了原有活动的时间范围
                    else if (applyEvent.getStartTime().compareTo(dateStart)>0&&applyEvent.getEndTime().compareTo(dateEnd)<0){
                        return ServerResponse.createBySuccessMessage("同一个活动时间出现重叠部分！请重新设定时间！" +
                                "申请的活动时间范围包含了原有活动的时间范围");
                    }
                    //申请的活动开始时间比原有活动开始时间早，但结束时间被包含于原有时间范围内
                    else if (applyEvent.getStartTime().compareTo(dateStart)>0&&applyEvent.getEndTime().compareTo(dateEnd)>0&&
                            applyEvent.getStartTime().compareTo(dateEnd)<0){
                        return ServerResponse.createBySuccessMessage("同一个活动时间出现重叠部分！请重新设定时间！" +
                                "申请的活动开始时间比原有活动开始时间早，但结束时间被包含于原有时间范围内");
                    }
                    //申请的活动结束时间比原有活动结束时间晚，但开始时间被包含于原有时间范围内
                    else if (applyEvent.getStartTime().compareTo(dateStart)<0&&applyEvent.getEndTime().compareTo(dateStart)>0&&
                            applyEvent.getEndTime().compareTo(dateEnd)<0){
                        return ServerResponse.createBySuccessMessage("同一个活动时间出现重叠部分！请重新设定时间！" +
                                "申请的活动结束时间比原有活动结束时间晚，但开始时间被包含于原有时间范围内");
                    }
                    //同样起始时间不同结束时间
                    else if (applyEvent.getStartTime().compareTo(dateStart)==0&&
                            applyEvent.getEndTime().compareTo(dateEnd)!=0){
                        return ServerResponse.createBySuccessMessage("申请活动或成功申请活动中存在同样起始时间但不同结束时间的记录" +
                                "，请重新设定起始时间");
                    }
                    //同样结束时间不同开始时间
                    else if (applyEvent.getEndTime().compareTo(dateEnd)==0&&
                            applyEvent.getStartTime().compareTo(dateStart)!=0){
                        return ServerResponse.createBySuccessMessage("申请活动或成功申请活动中存在同样结束时间但不同起始时间的记录" +
                                "，请重新设定结束时间");
                    }
                    //存在开始和结束时间一样的已申请活动记录
                    else if (applyEvent.getStartTime().compareTo(dateStart)==0&&
                            applyEvent.getEndTime().compareTo(dateEnd)==0){
                        return ServerResponse.createBySuccessMessage("存在同名活动，并且时间相同。");
                    }
                }
            }
        }
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
        List<com.example.association.vo.UserVO>memberInforList = userList2UserVOList(memberList);
        return ServerResponse.createBySuccessMessage("当前社团的所有成员信息：",memberInforList);
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
     * 查看某个社团的通知，若没有发布过则返回提示
     * @param assocId
     * @return
     */
    @Override
    public ServerResponse getNoticeByAssocId(Integer assocId) {
        Notice notice = noticeMapper.selectByAssocId(assocId);
        if (notice==null){
            return ServerResponse.createBySuccessMessage("本社团未发过通知");
        }
        return ServerResponse.createBySuccessMessage("本社团已发布的通知",notice);
    }

    /**
     * Notice转化成NoticeVO，增加字符串时间
     * @param notice
     * @return
     */
    public NoticeVO notice2NoticeVO(Notice notice){
        NoticeVO noticeVO = new NoticeVO();
        String timePublish="";
        BeanUtils.copyProperties(notice,noticeVO);
        noticeVO.setTimePublish(timePublish);
        noticeVO.setTimePublish(timePublish);
        return noticeVO;
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
            if (assocMemberMapper.getByMemberIdAndAssocId(applicantId,associationId)==null){
                rowInserAssocMember = assocMemberMapper.insertSelective(new AssocMember(applicantId,associationId));
            }else {
                applyJoinAssocMapper.checkApplyJoinTo2(applyJoinAssocId,applicantId,associationId);
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
            if (assocMemberMapper.getByMemberIdAndAssocId(applicantId,associationId)!=null){
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
        return rowCheck!=0?ServerResponse.createBySuccessMessage("已允许"+rowCheck+"名用户参加此活动ID为"+participantId)
                :ServerResponse.createBySuccessMessage("输入数据有误，无法修改记录！修改"+rowCheck+"行！");
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
     * 将User转换成UserVO，添加学院专业班级
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
        userVO.setClassNumber(classNumber);
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
            UserVO userVO = user2UserVO(applicant);

            timeApply=DateUtil.dateToStr(applyJoinAssoc.getAppTime());

            BeanUtils.copyProperties(applyJoinAssoc,applyJoinAssocVO);

            applyJoinAssocVO.setApplicant(userVO);
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
            String status="未审核";
            Boolean disable=true;

            switch (applyEvent.getEventStatus()){
                case 0:
                    status = "未审核";
                    disable=false;
                    break;
                case 1:
                    status = "已成功通过审核";
                    break;
                case 2:
                    status = "审核不通过";
                    disable=false;
                    break;
            }

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
            applyEventVO.setStatus(status);
            applyEventVO.setDisable(disable);
            applyEventVOList.add(applyEventVO);

        }
        return applyEventVOList;
    }

}
