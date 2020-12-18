package com.example.association.controller;

import com.example.association.common.ServerResponse;
import com.example.association.pojo.ApplyEvent;
import com.example.association.pojo.Association;
import com.example.association.pojo.Notice;
import com.example.association.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin")
@Api(tags = "社团负责人专用请求接口")
public class AdminController {

    @Autowired
    IAdminService adminService;

    //获取社团负责人的社团信息列表
    @GetMapping("/getAllAssociationsByAadminId")
    @ApiOperation(value = "获取社团负责人的社团信息列表")
    public ServerResponse getAllAssociations(@ApiParam(name = "adminId",value = "社团负责人的id",required = true)Integer adminId){
        return adminService.getAllAssociations(adminId);
    }

    //获取社团负责人名下某个社团的详细信息
    @GetMapping("/getCurrentAssociation")
    @ApiOperation(value = "社团负责人获取自己某个社团的详细信息",notes = "传入一个社团id，获取某个社团的详细信息")
    public ServerResponse getCurrentAssociation(@ApiParam(name = "associationId",value = "社团id",required = true)Integer associationId){
        return adminService.getAssociationInfor(associationId);
    }


    //申请举办活动
    @PostMapping("/applyEvent")
    @ApiOperation(value = "社团负责人申请举办活动",notes = "传入一个ApplyEvent实体，进行申请活动的表单提交，开始结束时间要注意先后")
    public ServerResponse applyEvent(@ApiParam(name = "applyEventInfo",value = "申请活动的表单实体",required = true)
                                     @RequestBody ApplyEvent applyEventInfo){
        return adminService.applyEvent(applyEventInfo);
    }

    //获取某社团所有成员信息
    @GetMapping("/getMembersInfor")
    @ApiOperation(value = "获取某社团所有成员",notes = "传入一个社团id，获取社团成员信息")
    public ServerResponse getMember(@ApiParam(name = "associationId",value = "社团id",required = true)
                                                Integer associationId){
        return adminService.getMember(associationId);
    }

    //发布社团通知
    @PostMapping("/publishNoticeByAssocId")
    @ApiOperation(value = "发布社团通知",notes = "传入社团通知的title，内容noticeContent，社团的id associationId")
    public ServerResponse publishNotice(@ApiParam(name = "notice",value = "通知实体",required = true)
                                            @RequestBody Notice notice){
        return adminService.publishNotice(notice);
    }


    //修改社团信息
    @PostMapping("/modifyAssociation")
    @ApiOperation(value = "修改某个社团信息",
            notes = "传入一个社团实体association修改社团信息,用json传入一个association对象")
    public ServerResponse modifyAssociation(@ApiParam(name = "association",value = "社团实体",required = true)
                                                @RequestBody Association association){
        return adminService.modifyAssociation(association);
    }

    //获取所有已申请活动（包含未审核、已审核通过、未通过审核的活动）,可以点进去看某活动的报名人员名单
    @GetMapping("/getAppliedEvents")
    @ApiOperation(value = "获取所有已申请的活动（包含未审核、已审核通过、未通过审核的活动）",
            notes = "传入社团负责人id，获取该社团负责人申请的所有活动，包括所有状态的活动。可以作为浏览的列表，其中已审核通过的可以点击查看报名参加的情况")
    public ServerResponse getAppliedEvents(@ApiParam(name = "applicantId",value = "活动申请者id，也就是对应社团负责人",required = true)
                                                       Integer applicantId){
        return adminService.getAppliedEvents(applicantId);
    }

    //获取某已通过审核的活动的报名参加人员名单，包括未审核和已审核
    @GetMapping("/getAllApplyParticipantsByEventId")
    @ApiOperation(value = "获取某个已通过审核的活动的报名参加人员名单",
            notes = "用活动id获取已审核的活动的报名参加人员名单，包括未审核和已审核的报名信息，也在此审核")
    public ServerResponse getAllApplyParticipantsByEventId(@ApiParam(name = "eventId",value = "活动id",required = true) Integer eventId){
        return adminService.getAllApplyParticipantsByEventId(eventId);
    }
    //获取某活动的报名参加人员名单，只包含成功报名的，以便于打印签到表
    @GetMapping("/getParticipantsOfPermitByEventId")
    @ApiOperation(value = "取某活动的报名参加人员名单，只包含成功报名的",
            notes = "只看已成功报名的人员，统计人员用于打印签到表")
    public ServerResponse getParticipantsOfPermitByEventId(@ApiParam(name = "eventId",value = "活动id",required = true)Integer eventId){
        return adminService.getParticipantsOfPermitByEventId(eventId);
    }

    //允许某人参加某项活动
    @GetMapping("/checkParticipateEventToPermit")
    @ApiOperation(value = "允许某人参加当前社团的某项活动",
            notes = "传入申请参加活动记录的id、活动id、报名参加者id，允许报名通过")
    public ServerResponse checkParticipateEventTo1(@ApiParam(name = "id",value = "申请参加活动记录的id",required = true)Integer id,
                                                   @ApiParam(name = "eventId",value = "活动id",required = true)Integer eventId,
                                                   @ApiParam(name = "participantId",value = "报名参加者id",required = true)Integer participantId){
        return adminService.checkParticipateEventTo1(id,eventId,participantId);
    }

    //拒绝某人参加某项活动
    @GetMapping("/checkParticipateEventToDeny")
    @ApiOperation(value = "传入申请参加活动记录的id、活动id、报名参加者id，拒绝报名通过",notes = "拒绝某人参加某项活动")
    public ServerResponse checkParticipateEventTo2(@ApiParam(name = "id",value = "申请参加活动记录的id",required = true)Integer id,
                                                   @ApiParam(name = "eventId",value = "活动id",required = true)Integer eventId,
                                                   @ApiParam(name = "participantId",value = "报名参加者id",required = true)Integer participantId){
        return adminService.checkParticipateEventTo2(id,eventId,participantId);
    }

    //打印某项活动的签到表
    @GetMapping("/getSignInExcelByEventId")
    @ApiOperation(value = "打印当前社团中的某个活动的签到表",
            notes = "传入活动id和HttpServletResponse，对某项活动的已经报名参加人员进行统计形成签到表excel，" +
                    "用文件流的形式通过response的OutputStream把excel文件回传给前端，前端对文件流进行解析保存")
    public void printSignInExcelByEventId(@ApiParam(name = "eventId",value = "已通过审核的活动的id",required = true) Integer eventId,
                                          HttpServletResponse response){
        adminService.getSignInExcelByEventId(eventId,response);
    }



    //显示申请加入社团的人员列表
    @GetMapping("/getApplyJoinAssocList")
    @ApiOperation(value = "获取申请加入社团的报名列表，包含个人详细信息",
            notes = "传入社团id，获取申请加入社团的人员列表")
    public ServerResponse getApplyJoinAssocList(@ApiParam(name = "associationId",value = "社团id",required = true)
                                                            Integer associationId){
        return adminService.getApplyJoinAssocList(associationId);
    }



    //移除某个社团成员
    @GetMapping("/removeOneMemberById")
    @ApiOperation(value = "移除某个社团中的某个成员",
                notes = "通过用户的id和associationId，移除某位社团的成员")
    public ServerResponse removeOneMemberById(@ApiParam(name = "memberId",value = "成员id，也就是用户id",required = true) Integer memberId,
                                              @ApiParam(name ="associationId",value ="成员所在社团的id" ,required = true) Integer associationId){
        return adminService.removeOneMemberById(memberId,associationId);
    }

    //允许某人加入社团
    @GetMapping("/checkApplyJoinToPermit")
    @ApiOperation(value = "允许某人加入社团",
                notes = "传入申请加入社团记录的id、申请者id、社团id对加入社团的申请进行 确认允许加入")
    public ServerResponse checkApplyJoinTo1(@ApiParam(name = "applyJoinAssocId",value = "申请加入社团表的记录id",required = true) Integer applyJoinAssocId,
                                            @ApiParam(name = "applicantId",value = "申请者id（用户id）",required = true) Integer applicantId,
                                            @ApiParam(name = "associationId",value = "社团id",required = true) Integer associationId){
        return adminService.checkApplyJoinTo1(applyJoinAssocId,applicantId,associationId);
    }

    //拒绝某人加入社团
    @GetMapping("/checkApplyJoinToDeny")
    @ApiOperation(value = "拒绝某人加入社团",
            notes = "传入申请加入社团记录的id、申请者id、社团id对加入社团的申请进行 拒绝加入社团")
    public ServerResponse checkApplyJoinTo2(@ApiParam(name = "applyJoinAssocId",value = "申请加入社团表的记录id",required = true) Integer applyJoinAssocId,
                                            @ApiParam(name = "applicantId",value = "申请者id（用户id）",required = true) Integer applicantId,
                                            @ApiParam(name = "associationId",value = "社团id",required = true) Integer associationId){
        return adminService.checkApplyJoinTo2(applyJoinAssocId,applicantId,associationId);
    }


}
