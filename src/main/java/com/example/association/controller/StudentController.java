package com.example.association.controller;

import com.example.association.service.IStudentService;
import com.example.association.common.ServerResponse;
import com.example.association.pojo.ApplyAssociation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "学生功能的接口")
@RequestMapping("/student")
public class StudentController {

    @Autowired
    IStudentService iStudentService;


    //申请社团
    @PostMapping("/applyAssociation")
    @ApiOperation(value = "申请新社团，先检查是否已发起申请；不提交id、applyStatus、adminId、verifyTime",
            notes = "申请新社团，需要传入一个申请社团的实体，但不提交id、applyStatus、adminId、verifyTime")
    public ServerResponse applyCreateAssociation(@ApiParam(name = "applyAssociation",value = "申请社团的实体",required = true)
                                                 @RequestBody ApplyAssociation applyAssociation){
        return iStudentService.apply(applyAssociation);
    }

    //查看已有社团
    @GetMapping("/getAssociationList")
    @ApiOperation(value = "查看已有社团",notes = "不传入参数时获取列表全部，传入时查询")
    public ServerResponse getAssociationList(@ApiParam(name = "assocName",value = "社团的模糊名称",required = false) String assocName){
        return iStudentService.getAssociationList(assocName);
    }

    //申请加入社团
    @GetMapping("/applyJoinAssociation")
    @ApiOperation(value = "申请加入社团，先检查是否已发起申请",notes = "传入个人id，和社团id")
    public ServerResponse applyJoinAssociation(@ApiParam(name = "userId",value = "用户id",required = true) Integer userId,
                                               @ApiParam(name = "associationId",value = "社团id",required = true) Integer associationId){
        return iStudentService.applyJoinAssociation(userId,associationId);
    }

    //申请参加活动
    @GetMapping("/applyJoinEvent")
    @ApiOperation(value = "申请加入活动，先检查是否已发起申请",notes ="传入用户id和活动id" )
    public ServerResponse applyJoinEvent(@ApiParam(name = "userId",value = "用户id",required = true)int userId,
                                         @ApiParam(name = "eventId",value = "活动id",required = true)int eventId){
        return iStudentService.applyJoinEvent(userId,eventId);
    }

    //查看已有活动
    @GetMapping("/getCheckedEvent")
    @ApiOperation(value = "查看所有活动",notes ="无需传参数" )
    public ServerResponse getCheckedEvent(){
        return iStudentService.getCheckedEvent();
    }

    //查看申请的新社团所处于申请状态
    @GetMapping("/getApplyNewAssocByApplicantId")
    @ApiOperation(value = "查看申请成立社团的状态信息",notes = "传入申请者的ID，获取已发起申请的社团状态信息，包含未审核、审核通过、通过")
    public ServerResponse getApplyNewAssocByApplicantId(@ApiParam(name = "applicantId",value = "用户id",required = true)Integer applicantId){
        return iStudentService.getApplyNewAssocByApplicantId(applicantId);
    }

    //查看申请加入社团的申请状态
    @GetMapping("/getApplyJoinAssocByUserId")
    @ApiOperation(value = "查看发起申请加入社团的状态信息",notes = "传入申请者的ID和社团的id，获取已发起申请加入社团的状态信息，包含未审核、审核通过、不通过")
    public ServerResponse getApplyJoinAssocByUserId(@ApiParam(name = "applicantId",value = "用户id",required = true)Integer applicantId){
        return iStudentService.getApplyJoinAssocByUserId(applicantId);
    }

    //查看已加入的社团以及对应的社团的通知
    @GetMapping("/getHasJoinedAssocAndNotice")
    @ApiOperation(value = "查看已加入的社团以及对应的社团的通知",notes = "传入当前用户id，也就是memberId，查看与其关联的社团")
    public ServerResponse getHasJoinedAssocAndNotice(@ApiParam(name = "memberId",value = "用户id",required = true)Integer memberId){
        return iStudentService.getHasJoinedAssocAndNotice(memberId);
    }

    //查看已申请参加活动的申请状态
    @GetMapping("/getApplyJoinEventByUserId")
    @ApiOperation(value = "查看发起申请参加活动的状态信息",notes = "传入当前用户id，也就是participantId，查看与其关联的申请参加活动信息")
    public ServerResponse getApplyJoinEventByUserId(@ApiParam(name = "participantId",value = "用户id",required = true)
                                                            Integer participantId){
        return iStudentService.getApplyJoinEventByUserId(participantId);
    }
}
